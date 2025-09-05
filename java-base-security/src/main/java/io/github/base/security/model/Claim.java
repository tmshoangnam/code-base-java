package io.github.base.security.model;

import java.util.Objects;

/**
 * Represents a claim in a security token.
 * A claim is a key-value pair that contains information about the subject.
 */
public final class Claim {
    
    private final String name;
    private final Object value;
    private final ClaimType type;
    
    /**
     * Creates a new claim with the specified name and value.
     *
     * @param name the claim name, must not be null or empty
     * @param value the claim value
     */
    public Claim(String name, Object value) {
        this(name, value, ClaimType.STRING);
    }
    
    /**
     * Creates a new claim with the specified name, value, and type.
     *
     * @param name the claim name, must not be null or empty
     * @param value the claim value
     * @param type the claim type
     */
    public Claim(String name, Object value, ClaimType type) {
        this.name = Objects.requireNonNull(name, "Claim name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Claim name cannot be empty");
        }
        this.value = value;
        this.type = type != null ? type : ClaimType.STRING;
    }
    
    /**
     * Gets the claim name.
     *
     * @return the claim name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the claim value.
     *
     * @return the claim value
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * Gets the claim type.
     *
     * @return the claim type
     */
    public ClaimType getType() {
        return type;
    }
    
    /**
     * Gets the claim value as a string.
     *
     * @return the claim value as a string, or null if the value is null
     */
    public String getStringValue() {
        return value != null ? value.toString() : null;
    }
    
    /**
     * Gets the claim value as a boolean.
     *
     * @return the claim value as a boolean, or false if the value is null or not a boolean
     */
    public boolean getBooleanValue() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return false;
    }
    
    /**
     * Gets the claim value as an integer.
     *
     * @return the claim value as an integer, or 0 if the value is null or not a number
     */
    public int getIntValue() {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    /**
     * Gets the claim value as a long.
     *
     * @return the claim value as a long, or 0L if the value is null or not a number
     */
    public long getLongValue() {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        return 0L;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Claim claim = (Claim) o;
        return Objects.equals(name, claim.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return "Claim{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
    
    /**
     * Enum representing the type of a claim.
     */
    public enum ClaimType {
        STRING,
        BOOLEAN,
        INTEGER,
        LONG,
        ARRAY,
        OBJECT
    }
}
