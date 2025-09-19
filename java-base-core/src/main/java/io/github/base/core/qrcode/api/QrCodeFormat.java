package io.github.base.core.qrcode.api;

/**
 * Supported QR Code output formats.
 * 
 * <p>This enum defines the standard formats supported by QR Code services.
 * Each format has associated MIME type and file extension information.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public enum QrCodeFormat {
    
    /**
     * Portable Network Graphics format.
     * <p>MIME type: image/png</p>
     * <p>Extension: .png</p>
     */
    PNG("image/png", "png"),
    
    /**
     * JPEG format.
     * <p>MIME type: image/jpeg</p>
     * <p>Extension: .jpg</p>
     */
    JPEG("image/jpeg", "jpg"),
    
    /**
     * Scalable Vector Graphics format.
     * <p>MIME type: image/svg+xml</p>
     * <p>Extension: .svg</p>
     */
    SVG("image/svg+xml", "svg"),
    
    /**
     * Base64 encoded string format.
     * <p>MIME type: text/plain</p>
     * <p>Extension: .txt</p>
     */
    BASE64("text/plain", "txt");
    
    private final String mimeType;
    private final String fileExtension;
    
    QrCodeFormat(String mimeType, String fileExtension) {
        this.mimeType = mimeType;
        this.fileExtension = fileExtension;
    }
    
    /**
     * Gets the MIME type for this format.
     * 
     * @return the MIME type
     */
    public String getMimeType() {
        return mimeType;
    }
    
    /**
     * Gets the file extension for this format.
     * 
     * @return the file extension (without the dot)
     */
    public String getFileExtension() {
        return fileExtension;
    }
    
    /**
     * Gets the file extension with leading dot.
     * 
     * @return the file extension with leading dot
     */
    public String getFileExtensionWithDot() {
        return "." + fileExtension;
    }
}
