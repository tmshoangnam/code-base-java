package io.github.base.core.qrcode.model;

import io.github.base.core.qrcode.api.QrCodeFormat;
import io.github.base.core.utils.ValidationUtils;

import java.util.Objects;

/**
 * Request model for QR Code encoding operations.
 * 
 * <p>This immutable class contains all the necessary information to encode a QR Code,
 * including the content to encode and various formatting options.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * QrCodeRequest request = QrCodeRequest.builder()
 *     .content("https://example.com")
 *     .format(QrCodeFormat.PNG)
 *     .size(300)
 *     .margin(4)
 *     .errorCorrectionLevel(ErrorCorrectionLevel.M)
 *     .build();
 * }</pre>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public final class QrCodeRequest {
    
    private final String content;
    private final QrCodeFormat format;
    private final int size;
    private final int margin;
    private final ErrorCorrectionLevel errorCorrectionLevel;
    private final QrCodeOptions options;
    
    private QrCodeRequest(Builder builder) {
        this.content = builder.content;
        this.format = builder.format;
        this.size = builder.size;
        this.margin = builder.margin;
        this.errorCorrectionLevel = builder.errorCorrectionLevel;
        this.options = builder.options;
    }
    
    /**
     * Gets the content to encode in the QR Code.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Gets the output format for the QR Code.
     * 
     * @return the format
     */
    public QrCodeFormat getFormat() {
        return format;
    }
    
    /**
     * Gets the size of the QR Code in pixels.
     * 
     * @return the size
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Gets the margin around the QR Code in pixels.
     * 
     * @return the margin
     */
    public int getMargin() {
        return margin;
    }
    
    /**
     * Gets the error correction level for the QR Code.
     * 
     * @return the error correction level
     */
    public ErrorCorrectionLevel getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }
    
    /**
     * Gets the additional options for the QR Code.
     * 
     * @return the options
     */
    public QrCodeOptions getOptions() {
        return options;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QrCodeRequest that = (QrCodeRequest) o;
        return size == that.size &&
               margin == that.margin &&
               Objects.equals(content, that.content) &&
               format == that.format &&
               errorCorrectionLevel == that.errorCorrectionLevel &&
               Objects.equals(options, that.options);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(content, format, size, margin, errorCorrectionLevel, options);
    }
    
    @Override
    public String toString() {
        return "QrCodeRequest{" +
               "content='" + content + '\'' +
               ", format=" + format +
               ", size=" + size +
               ", margin=" + margin +
               ", errorCorrectionLevel=" + errorCorrectionLevel +
               ", options=" + options +
               '}';
    }
    
    /**
     * Builder for creating QrCodeRequest instances.
     */
    public static class Builder {
        private String content;
        private QrCodeFormat format = QrCodeFormat.PNG;
        private int size = 300;
        private int margin = 4;
        private ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.M;
        private QrCodeOptions options;
        
        /**
         * Sets the content to encode.
         * 
         * @param content the content
         * @return this builder
         * @throws IllegalArgumentException if content is null or empty
         */
        public Builder content(String content) {
            ValidationUtils.requireNonBlank(content, "Content cannot be null or empty");
            this.content = content;
            return this;
        }
        
        /**
         * Sets the output format.
         * 
         * @param format the format
         * @return this builder
         * @throws IllegalArgumentException if format is null
         */
        public Builder format(QrCodeFormat format) {
            ValidationUtils.requireNonNull(format, "Format cannot be null");
            this.format = format;
            return this;
        }
        
        /**
         * Sets the size of the QR Code.
         * 
         * @param size the size in pixels
         * @return this builder
         * @throws IllegalArgumentException if size is not positive
         */
        public Builder size(int size) {
            ValidationUtils.requirePositive(size, "Size must be positive");
            this.size = size;
            return this;
        }
        
        /**
         * Sets the margin around the QR Code.
         * 
         * @param margin the margin in pixels
         * @return this builder
         * @throws IllegalArgumentException if margin is negative
         */
        public Builder margin(int margin) {
            ValidationUtils.requireNonNegative(margin, "Margin cannot be negative");
            this.margin = margin;
            return this;
        }
        
        /**
         * Sets the error correction level.
         * 
         * @param errorCorrectionLevel the error correction level
         * @return this builder
         * @throws IllegalArgumentException if errorCorrectionLevel is null
         */
        public Builder errorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
            ValidationUtils.requireNonNull(errorCorrectionLevel, "Error correction level cannot be null");
            this.errorCorrectionLevel = errorCorrectionLevel;
            return this;
        }
        
        /**
         * Sets additional options.
         * 
         * @param options the options
         * @return this builder
         */
        public Builder options(QrCodeOptions options) {
            this.options = options;
            return this;
        }
        
        /**
         * Builds the QrCodeRequest instance.
         * 
         * @return the built request
         * @throws IllegalStateException if required fields are not set
         */
        public QrCodeRequest build() {
            ValidationUtils.requireNonBlank(content, "Content is required");
            return new QrCodeRequest(this);
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
