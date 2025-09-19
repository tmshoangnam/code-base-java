package io.github.base.core.qrcode.model;

import java.awt.Color;
import java.util.Objects;

/**
 * Additional options for QR Code generation.
 * 
 * <p>This class provides advanced configuration options for QR Code generation,
 * including colors, logo embedding, and other visual customizations.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * QrCodeOptions options = QrCodeOptions.builder()
 *     .foregroundColor(Color.BLACK)
 *     .backgroundColor(Color.WHITE)
 *     .logoPath("/path/to/logo.png")
 *     .logoSize(50)
 *     .build();
 * }</pre>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public final class QrCodeOptions {
    
    private final Color foregroundColor;
    private final Color backgroundColor;
    private final String logoPath;
    private final int logoSize;
    private final boolean logoCentered;
    private final String customData;
    
    private QrCodeOptions(Builder builder) {
        this.foregroundColor = builder.foregroundColor;
        this.backgroundColor = builder.backgroundColor;
        this.logoPath = builder.logoPath;
        this.logoSize = builder.logoSize;
        this.logoCentered = builder.logoCentered;
        this.customData = builder.customData;
    }
    
    /**
     * Gets the foreground color of the QR Code.
     * 
     * @return the foreground color
     */
    public Color getForegroundColor() {
        return foregroundColor;
    }
    
    /**
     * Gets the background color of the QR Code.
     * 
     * @return the background color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    /**
     * Gets the path to the logo file to embed in the QR Code.
     * 
     * @return the logo path
     */
    public String getLogoPath() {
        return logoPath;
    }
    
    /**
     * Gets the size of the logo in pixels.
     * 
     * @return the logo size
     */
    public int getLogoSize() {
        return logoSize;
    }
    
    /**
     * Checks if the logo should be centered in the QR Code.
     * 
     * @return true if the logo is centered
     */
    public boolean isLogoCentered() {
        return logoCentered;
    }
    
    /**
     * Gets custom data associated with this QR Code.
     * 
     * @return the custom data
     */
    public String getCustomData() {
        return customData;
    }
    
    /**
     * Checks if a logo is configured.
     * 
     * @return true if a logo is configured
     */
    public boolean hasLogo() {
        return logoPath != null && !logoPath.trim().isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QrCodeOptions that = (QrCodeOptions) o;
        return logoSize == that.logoSize &&
               logoCentered == that.logoCentered &&
               Objects.equals(foregroundColor, that.foregroundColor) &&
               Objects.equals(backgroundColor, that.backgroundColor) &&
               Objects.equals(logoPath, that.logoPath) &&
               Objects.equals(customData, that.customData);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(foregroundColor, backgroundColor, logoPath, logoSize, logoCentered, customData);
    }
    
    @Override
    public String toString() {
        return "QrCodeOptions{" +
               "foregroundColor=" + foregroundColor +
               ", backgroundColor=" + backgroundColor +
               ", logoPath='" + logoPath + '\'' +
               ", logoSize=" + logoSize +
               ", logoCentered=" + logoCentered +
               ", customData='" + customData + '\'' +
               '}';
    }
    
    /**
     * Builder for creating QrCodeOptions instances.
     */
    public static class Builder {
        private Color foregroundColor = Color.BLACK;
        private Color backgroundColor = Color.WHITE;
        private String logoPath;
        private int logoSize = 50;
        private boolean logoCentered = true;
        private String customData;
        
        /**
         * Sets the foreground color of the QR Code.
         * 
         * @param foregroundColor the foreground color
         * @return this builder
         * @throws IllegalArgumentException if foregroundColor is null
         */
        public Builder foregroundColor(Color foregroundColor) {
            if (foregroundColor == null) {
                throw new IllegalArgumentException("Foreground color cannot be null");
            }
            this.foregroundColor = foregroundColor;
            return this;
        }
        
        /**
         * Sets the background color of the QR Code.
         * 
         * @param backgroundColor the background color
         * @return this builder
         * @throws IllegalArgumentException if backgroundColor is null
         */
        public Builder backgroundColor(Color backgroundColor) {
            if (backgroundColor == null) {
                throw new IllegalArgumentException("Background color cannot be null");
            }
            this.backgroundColor = backgroundColor;
            return this;
        }
        
        /**
         * Sets the path to the logo file to embed in the QR Code.
         * 
         * @param logoPath the logo path
         * @return this builder
         */
        public Builder logoPath(String logoPath) {
            this.logoPath = logoPath;
            return this;
        }
        
        /**
         * Sets the size of the logo in pixels.
         * 
         * @param logoSize the logo size
         * @return this builder
         * @throws IllegalArgumentException if logoSize is not positive
         */
        public Builder logoSize(int logoSize) {
            if (logoSize <= 0) {
                throw new IllegalArgumentException("Logo size must be positive");
            }
            this.logoSize = logoSize;
            return this;
        }
        
        /**
         * Sets whether the logo should be centered in the QR Code.
         * 
         * @param logoCentered true if the logo should be centered
         * @return this builder
         */
        public Builder logoCentered(boolean logoCentered) {
            this.logoCentered = logoCentered;
            return this;
        }
        
        /**
         * Sets custom data associated with this QR Code.
         * 
         * @param customData the custom data
         * @return this builder
         */
        public Builder customData(String customData) {
            this.customData = customData;
            return this;
        }
        
        /**
         * Builds the QrCodeOptions instance.
         * 
         * @return the built options
         */
        public QrCodeOptions build() {
            return new QrCodeOptions(this);
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
