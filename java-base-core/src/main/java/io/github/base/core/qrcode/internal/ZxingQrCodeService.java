package io.github.base.core.qrcode.internal;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.Result;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.api.QrCodeFormat;
import io.github.base.core.qrcode.api.QrCodeException;
import io.github.base.core.qrcode.api.ErrorCodes;
import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.model.QrCodeResult;
import io.github.base.core.logging.StructuredLogger;
import io.github.base.core.logging.StructuredLoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * ZXing-based implementation of QR Code service.
 * 
 * <p>This implementation uses the ZXing library for QR Code generation and decoding.
 * It supports multiple output formats including PNG, JPEG, and SVG.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public class ZxingQrCodeService implements QrCodeService {
    
    private static final StructuredLogger logger = StructuredLoggerFactory.getLogger(ZxingQrCodeService.class);
    
    private final QRCodeWriter qrCodeWriter;
    private final MultiFormatReader multiFormatReader;
    
    public ZxingQrCodeService() {
        this.qrCodeWriter = new QRCodeWriter();
        this.multiFormatReader = new MultiFormatReader();
    }
    
    @Override
    public QrCodeResult encode(QrCodeRequest request) throws QrCodeException {
        try {
            logger.debug("Encoding QR Code with content: {}", request.getContent());
            
            // Prepare encoding hints
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.ERROR_CORRECTION, convertErrorCorrectionLevel(request.getErrorCorrectionLevel()));
            hints.put(EncodeHintType.MARGIN, request.getMargin());
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            
            // Generate QR Code matrix
            BitMatrix bitMatrix = qrCodeWriter.encode(
                request.getContent(),
                BarcodeFormat.QR_CODE,
                request.getSize(),
                request.getSize(),
                hints
            );
            
            // Convert to image
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
            // Apply custom options if provided
            if (request.getOptions() != null) {
                image = applyCustomOptions(image, request.getOptions());
            }
            
            // Convert to bytes based on format
            byte[] imageBytes = convertToBytes(image, request.getFormat());
            
            // Create result
            return QrCodeResult.builder()
                .bytes(imageBytes)
                .format(request.getFormat())
                .dimensions(image.getWidth(), image.getHeight())
                .build();
                
        } catch (WriterException e) {
            logger.error("Failed to encode QR Code", e);
            throw new QrCodeException(ErrorCodes.ENCODING_FAILED, "Failed to encode QR Code: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during QR Code encoding", e);
            throw new QrCodeException(ErrorCodes.ENCODING_FAILED, "Unexpected error during QR Code encoding", e);
        }
    }
    
    @Override
    public String decode(byte[] imageBytes) throws QrCodeException {
        try {
            logger.debug("Decoding QR Code from {} bytes", imageBytes.length);
            
            // Convert bytes to BufferedImage
            BufferedImage image = ImageIO.read(new java.io.ByteArrayInputStream(imageBytes));
            if (image == null) {
                throw new QrCodeException(ErrorCodes.INVALID_IMAGE_DATA, "Invalid image data provided");
            }
            
            // Convert to binary bitmap
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            
            // Prepare decoding hints
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            
            // Decode QR Code
            Result result = multiFormatReader.decode(binaryBitmap, hints);
            
            logger.debug("Successfully decoded QR Code: {}", result.getText());
            return result.getText();
            
        } catch (com.google.zxing.NotFoundException e) {
            logger.warn("No QR Code found in image");
            throw new QrCodeException(ErrorCodes.NO_QR_CODE_FOUND, "No QR Code found in the provided image", e);
        } catch (IOException e) {
            logger.error("Failed to read image data", e);
            throw new QrCodeException(ErrorCodes.INVALID_IMAGE_DATA, "Failed to read image data", e);
        } catch (Exception e) {
            logger.error("Unexpected error during QR Code decoding", e);
            throw new QrCodeException(ErrorCodes.DECODING_FAILED, "Unexpected error during QR Code decoding", e);
        }
    }
    
    @Override
    public boolean supportsFormat(QrCodeFormat format) {
        return format == QrCodeFormat.PNG || 
               format == QrCodeFormat.JPEG || 
               format == QrCodeFormat.BASE64;
    }
    
    @Override
    public String getServiceName() {
        return "zxing";
    }
    
    private com.google.zxing.qrcode.decoder.ErrorCorrectionLevel convertErrorCorrectionLevel(io.github.base.core.qrcode.model.ErrorCorrectionLevel level) {
        return switch (level) {
            case L -> com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L;
            case M -> com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M;
            case Q -> com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.Q;
            case H -> com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H;
        };
    }
    
    private BufferedImage applyCustomOptions(BufferedImage originalImage, io.github.base.core.qrcode.model.QrCodeOptions options) {
        if (options == null) {
            return originalImage;
        }
        
        // Create new image with custom colors
        BufferedImage newImage = new BufferedImage(
            originalImage.getWidth(),
            originalImage.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );
        
        Graphics2D g2d = newImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fill background
        g2d.setColor(options.getBackgroundColor());
        g2d.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        
        // Draw QR Code with custom foreground color
        g2d.setColor(options.getForegroundColor());
        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                if (originalImage.getRGB(x, y) == Color.BLACK.getRGB()) {
                    g2d.fillRect(x, y, 1, 1);
                }
            }
        }
        
        g2d.dispose();
        return newImage;
    }
    
    private byte[] convertToBytes(BufferedImage image, QrCodeFormat format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        switch (format) {
            case PNG -> ImageIO.write(image, "PNG", baos);
            case JPEG -> ImageIO.write(image, "JPEG", baos);
            case BASE64 -> {
                ImageIO.write(image, "PNG", baos);
                // Base64 encoding is handled in QrCodeResult
            }
            default -> throw new QrCodeException(ErrorCodes.UNSUPPORTED_FORMAT, "Unsupported format: " + format);
        }
        
        return baos.toByteArray();
    }
}
