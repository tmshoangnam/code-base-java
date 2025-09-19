package io.github.base.core.qrcode.model;

/**
 * Error correction levels for QR Codes.
 * 
 * <p>QR Codes support four levels of error correction, allowing the code to be read
 * even if it is partially damaged or obscured. Higher levels provide more error correction
 * but require more space and may not be suitable for very small QR Codes.</p>
 * 
 * <p>Error correction levels:</p>
 * <ul>
 *   <li>L (Low): ~7% error correction</li>
 *   <li>M (Medium): ~15% error correction</li>
 *   <li>Q (Quartile): ~25% error correction</li>
 *   <li>H (High): ~30% error correction</li>
 * </ul>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public enum ErrorCorrectionLevel {
    
    /**
     * Low error correction level (~7%).
     * <p>Best for clean environments with minimal risk of damage.</p>
     */
    L(1, "Low"),
    
    /**
     * Medium error correction level (~15%).
     * <p>Good balance between error correction and data capacity. Recommended for most use cases.</p>
     */
    M(0, "Medium"),
    
    /**
     * Quartile error correction level (~25%).
     * <p>Higher error correction for environments where the QR Code might be partially damaged.</p>
     */
    Q(3, "Quartile"),
    
    /**
     * High error correction level (~30%).
     * <p>Maximum error correction for harsh environments or when maximum reliability is required.</p>
     */
    H(2, "High");
    
    private final int numericValue;
    private final String description;
    
    ErrorCorrectionLevel(int numericValue, String description) {
        this.numericValue = numericValue;
        this.description = description;
    }
    
    /**
     * Gets the numeric value used by QR Code libraries.
     * 
     * @return the numeric value
     */
    public int getNumericValue() {
        return numericValue;
    }
    
    /**
     * Gets the human-readable description of this error correction level.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the error correction percentage for this level.
     * 
     * @return the error correction percentage
     */
    public double getErrorCorrectionPercentage() {
        return switch (this) {
            case L -> 7.0;
            case M -> 15.0;
            case Q -> 25.0;
            case H -> 30.0;
        };
    }
    
    /**
     * Gets the error correction level from its numeric value.
     * 
     * @param numericValue the numeric value
     * @return the corresponding error correction level
     * @throws IllegalArgumentException if the numeric value is not valid
     */
    public static ErrorCorrectionLevel fromNumericValue(int numericValue) {
        return switch (numericValue) {
            case 0 -> M;
            case 1 -> L;
            case 2 -> H;
            case 3 -> Q;
            default -> throw new IllegalArgumentException("Invalid error correction level: " + numericValue);
        };
    }
}
