package io.github.base.aws.api.common;

import java.util.Objects;

/**
 * Immutable record representing an AWS region.
 * 
 * <p>This record contains information about an AWS region including its name,
 * display name, and availability zones.
 * 
 * @param name the region name (e.g., "us-east-1")
 * @param displayName the display name (e.g., "US East (N. Virginia)")
 * @param availabilityZones list of availability zones in the region
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record AwsRegion(
    String name,
    String displayName,
    java.util.List<String> availabilityZones
) {
    
    /**
     * Creates an AWS region with basic information.
     * 
     * @param name the region name
     * @return new AwsRegion instance
     * @throws IllegalArgumentException if name is null or empty
     */
    public static AwsRegion of(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Region name cannot be null or empty");
        }
        
        return new AwsRegion(
            name.trim(),
            null,
            java.util.List.of()
        );
    }
    
    /**
     * Creates an AWS region with all information.
     * 
     * @param name the region name
     * @param displayName the display name
     * @param availabilityZones list of availability zones
     * @return new AwsRegion instance
     * @throws IllegalArgumentException if name is null or empty
     */
    public static AwsRegion of(String name, String displayName, java.util.List<String> availabilityZones) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Region name cannot be null or empty");
        }
        
        return new AwsRegion(
            name.trim(),
            displayName,
            availabilityZones != null ? availabilityZones : java.util.List.of()
        );
    }
    
    /**
     * Creates a copy of this AwsRegion with updated display name.
     * 
     * @param displayName new display name
     * @return new AwsRegion instance with updated display name
     */
    public AwsRegion withDisplayName(String displayName) {
        return new AwsRegion(
            this.name,
            displayName,
            this.availabilityZones
        );
    }
    
    /**
     * Creates a copy of this AwsRegion with updated availability zones.
     * 
     * @param availabilityZones new availability zones list
     * @return new AwsRegion instance with updated availability zones
     */
    public AwsRegion withAvailabilityZones(java.util.List<String> availabilityZones) {
        return new AwsRegion(
            this.name,
            this.displayName,
            availabilityZones != null ? availabilityZones : java.util.List.of()
        );
    }
    
    /**
     * Checks if this region has a display name.
     * 
     * @return true if display name is set, false otherwise
     */
    public boolean hasDisplayName() {
        return displayName != null && !displayName.trim().isEmpty();
    }
    
    /**
     * Checks if this region has availability zones.
     * 
     * @return true if availability zones are set, false otherwise
     */
    public boolean hasAvailabilityZones() {
        return availabilityZones != null && !availabilityZones.isEmpty();
    }
    
    /**
     * Gets the number of availability zones in this region.
     * 
     * @return number of availability zones
     */
    public int getAvailabilityZoneCount() {
        return availabilityZones != null ? availabilityZones.size() : 0;
    }
    
    /**
     * Checks if this region is a specific region.
     * 
     * @param regionName the region name to check
     * @return true if this region matches the specified name, false otherwise
     */
    public boolean isRegion(String regionName) {
        if (regionName == null) {
            return false;
        }
        return name.equals(regionName.trim());
    }
    
    /**
     * Checks if this region is in the US.
     * 
     * @return true if region name starts with "us-", false otherwise
     */
    public boolean isUSRegion() {
        return name.startsWith("us-");
    }
    
    /**
     * Checks if this region is in Europe.
     * 
     * @return true if region name starts with "eu-", false otherwise
     */
    public boolean isEuropeRegion() {
        return name.startsWith("eu-");
    }
    
    /**
     * Checks if this region is in Asia Pacific.
     * 
     * @return true if region name starts with "ap-", false otherwise
     */
    public boolean isAsiaPacificRegion() {
        return name.startsWith("ap-");
    }
    
    /**
     * Checks if this region is in Canada.
     * 
     * @return true if region name starts with "ca-", false otherwise
     */
    public boolean isCanadaRegion() {
        return name.startsWith("ca-");
    }
    
    /**
     * Checks if this region is in South America.
     * 
     * @return true if region name starts with "sa-", false otherwise
     */
    public boolean isSouthAmericaRegion() {
        return name.startsWith("sa-");
    }
    
    /**
     * Checks if this region is in Africa.
     * 
     * @return true if region name starts with "af-", false otherwise
     */
    public boolean isAfricaRegion() {
        return name.startsWith("af-");
    }
    
    /**
     * Checks if this region is in the Middle East.
     * 
     * @return true if region name starts with "me-", false otherwise
     */
    public boolean isMiddleEastRegion() {
        return name.startsWith("me-");
    }
    
    /**
     * Checks if this region is a government region.
     * 
     * @return true if region name contains "gov", false otherwise
     */
    public boolean isGovernmentRegion() {
        return name.contains("gov");
    }
    
    /**
     * Checks if this region is a China region.
     * 
     * @return true if region name starts with "cn-", false otherwise
     */
    public boolean isChinaRegion() {
        return name.startsWith("cn-");
    }
    
    /**
     * Gets the continent of this region.
     * 
     * @return continent name or null if unknown
     */
    public String getContinent() {
        if (isUSRegion() || isCanadaRegion()) {
            return "North America";
        } else if (isEuropeRegion()) {
            return "Europe";
        } else if (isAsiaPacificRegion()) {
            return "Asia Pacific";
        } else if (isSouthAmericaRegion()) {
            return "South America";
        } else if (isAfricaRegion()) {
            return "Africa";
        } else if (isMiddleEastRegion()) {
            return "Middle East";
        } else if (isChinaRegion()) {
            return "China";
        } else {
            return null;
        }
    }
    
    /**
     * Gets the region name for display purposes.
     * 
     * @return display name if available, otherwise region name
     */
    public String getDisplayName() {
        return hasDisplayName() ? displayName : name;
    }
    
    /**
     * Gets a formatted string representation of this region.
     * 
     * @return formatted string
     */
    public String getFormattedString() {
        if (hasDisplayName()) {
            return displayName + " (" + name + ")";
        } else {
            return name;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AwsRegion that = (AwsRegion) obj;
        return Objects.equals(name, that.name) &&
               Objects.equals(displayName, that.displayName) &&
               Objects.equals(availabilityZones, that.availabilityZones);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, displayName, availabilityZones);
    }
    
    @Override
    public String toString() {
        return "AwsRegion{" +
               "name='" + name + '\'' +
               ", displayName='" + displayName + '\'' +
               ", availabilityZones=" + availabilityZones +
               '}';
    }
}
