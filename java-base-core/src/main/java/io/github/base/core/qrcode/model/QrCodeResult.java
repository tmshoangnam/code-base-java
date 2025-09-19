package io.github.base.core.qrcode.model;

import io.github.base.core.qrcode.api.QrCodeFormat;

import java.util.Base64;
import java.util.Objects;

/**
 * Result model for QR Code encoding operations.
 * 
 * <p>This immutable class contains the encoded QR Code data along with metadata
 * about the format and encoding details.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public final class QrCodeResult {
    
    private final byte[] bytes;
    private final QrCodeFormat format;
    private final String mimeType;
    private final int width;
    private final int height;
    private final String base64String;
    
    private QrCodeResult(Builder builder) {
        this.bytes = builder.bytes;
        this.format = builder.format;
        this.mimeType = builder.mimeType;
        this.width = builder.width;
        this.height = builder.height;
        this.base64String = builder.base64String;
    }
    
    /**
     * Gets the raw bytes of the encoded QR Code.
     * 
     * @return the bytes
     */
    public byte[] getBytes() {
        return bytes.clone(); // Defensive copy
    }
    
    /**
     * Gets the format of the encoded QR Code.
     * 
     * @return the format
     */
    public QrCodeFormat getFormat() {
        return format;
    }
    
    /**
     * Gets the MIME type of the encoded QR Code.
     * 
     * @return the MIME type
     */
    public String getMimeType() {
        return mimeType;
    }
    
    /**
     * Gets the width of the encoded QR Code in pixels.
     * 
     * @return the width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the encoded QR Code in pixels.
     * 
     * @return the height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the Base64 encoded string representation of the QR Code.
     * 
     * @return the Base64 string
     */
    public String getBase64String() {
        return base64String;
    }
    
    /**
     * Gets the size of the encoded data in bytes.
     * 
     * @return the size in bytes
     */
    public int getSizeInBytes() {
        return bytes.length;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QrCodeResult that = (QrCodeResult) o;
        return width == that.width &&
               height == that.height &&
               Objects.equals(format, that.format) &&
               Objects.equals(mimeType, that.mimeType) &&
               Objects.equals(base64String, that.base64String);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(format, mimeType, width, height, base64String);
    }
    
    @Override
    public String toString() {
        return "QrCodeResult{" +
               "format=" + format +
               ", mimeType='" + mimeType + '\'' +
               ", width=" + width +
               ", height=" + height +
               ", sizeInBytes=" + getSizeInBytes() +
               '}';
    }
    
    /**
     * Builder for creating QrCodeResult instances.
     */
    public static class Builder {
        private byte[] bytes;
        private QrCodeFormat format;
        private String mimeType;
        private int width;
        private int height;
        private String base64String;
        
        /**
         * Sets the raw bytes of the encoded QR Code.
         * 
         * @param bytes the bytes
         * @return this builder
         * @throws IllegalArgumentException if bytes is null or empty
         */
        public Builder bytes(byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                throw new IllegalArgumentException("Bytes cannot be null or empty");
            }
            this.bytes = bytes.clone(); // Defensive copy
            return this;
        }
        
        /**
         * Sets the format of the encoded QR Code.
         * 
         * @param format the format
         * @return this builder
         * @throws IllegalArgumentException if format is null
         */
        public Builder format(QrCodeFormat format) {
            if (format == null) {
                throw new IllegalArgumentException("Format cannot be null");
            }
            this.format = format;
            this.mimeType = format.getMimeType();
            return this;
        }
        
        /**
         * Sets the dimensions of the encoded QR Code.
         * 
         * @param width the width in pixels
         * @param height the height in pixels
         * @return this builder
         * @throws IllegalArgumentException if width or height is not positive
         */
        public Builder dimensions(int width, int height) {
            if (width <= 0) {
                throw new IllegalArgumentException("Width must be positive");
            }
            if (height <= 0) {
                throw new IllegalArgumentException("Height must be positive");
            }
            this.width = width;
            this.height = height;
            return this;
        }
        
        /**
         * Sets the MIME type directly (overrides format MIME type).
         * 
         * @param mimeType the MIME type
         * @return this builder
         * @throws IllegalArgumentException if mimeType is null or empty
         */
        public Builder mimeType(String mimeType) {
            if (mimeType == null || mimeType.trim().isEmpty()) {
                throw new IllegalArgumentException("MIME type cannot be null or empty");
            }
            this.mimeType = mimeType;
            return this;
        }
        
        /**
         * Builds the QrCodeResult instance.
         * 
         * @return the built result
         * @throws IllegalStateException if required fields are not set
         */
        public QrCodeResult build() {
            if (bytes == null || bytes.length == 0) {
                throw new IllegalStateException("Bytes are required");
            }
            if (format == null) {
                throw new IllegalStateException("Format is required");
            }
            if (width <= 0 || height <= 0) {
                throw new IllegalStateException("Valid dimensions are required");
            }
            
            // Generate Base64 string if not already set
            if (base64String == null) {
                this.base64String = Base64.getEncoder().encodeToString(bytes);
            }
            
            return new QrCodeResult(this);
        }
    }
    
    /**
     * Creates a new builder instance.
     * 
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }
}
