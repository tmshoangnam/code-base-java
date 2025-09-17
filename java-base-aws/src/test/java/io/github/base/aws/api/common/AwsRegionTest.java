package io.github.base.aws.api.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link AwsRegion}.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("AwsRegion Tests")
class AwsRegionTest {

    private static final String REGION_NAME = "us-east-1";
    private static final String DISPLAY_NAME = "US East (N. Virginia)";
    private static final List<String> AVAILABILITY_ZONES = List.of("us-east-1a", "us-east-1b", "us-east-1c");

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create AwsRegion with all parameters")
        void shouldCreateAwsRegionWithAllParameters() {
            // When
            AwsRegion region = new AwsRegion(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // Then
            assertThat(region.name()).isEqualTo(REGION_NAME);
            assertThat(region.displayName()).isEqualTo(DISPLAY_NAME);
            assertThat(region.availabilityZones()).isEqualTo(AVAILABILITY_ZONES);
        }

        @Test
        @DisplayName("Should create AwsRegion with null values")
        void shouldCreateAwsRegionWithNullValues() {
            // When
            AwsRegion region = new AwsRegion(null, null, null);

            // Then
            assertThat(region.name()).isNull();
            assertThat(region.displayName()).isNull();
            assertThat(region.availabilityZones()).isNull();
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("of method with name should create region")
        void ofMethodWithNameShouldCreateRegion() {
            // When
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // Then
            assertThat(region.name()).isEqualTo(REGION_NAME);
            assertThat(region.displayName()).isNull();
            assertThat(region.availabilityZones()).isEmpty();
        }

        @Test
        @DisplayName("of method with all parameters should create region")
        void ofMethodWithAllParametersShouldCreateRegion() {
            // When
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // Then
            assertThat(region.name()).isEqualTo(REGION_NAME);
            assertThat(region.displayName()).isEqualTo(DISPLAY_NAME);
            assertThat(region.availabilityZones()).isEqualTo(AVAILABILITY_ZONES);
        }

        @Test
        @DisplayName("of method should trim whitespace from name")
        void ofMethodShouldTrimWhitespaceFromName() {
            // Given
            String nameWithWhitespace = "  " + REGION_NAME + "  ";

            // When
            AwsRegion region = AwsRegion.of(nameWithWhitespace);

            // Then
            assertThat(region.name()).isEqualTo(REGION_NAME);
        }

        @Test
        @DisplayName("of method should handle null availability zones")
        void ofMethodShouldHandleNullAvailabilityZones() {
            // When
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, null);

            // Then
            assertThat(region.availabilityZones()).isEmpty();
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for null name")
        void ofMethodShouldThrowIllegalArgumentExceptionForNullName() {
            // When & Then
            assertThatThrownBy(() -> AwsRegion.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Region name cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for empty name")
        void ofMethodShouldThrowIllegalArgumentExceptionForEmptyName() {
            // When & Then
            assertThatThrownBy(() -> AwsRegion.of(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Region name cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for blank name")
        void ofMethodShouldThrowIllegalArgumentExceptionForBlankName() {
            // When & Then
            assertThatThrownBy(() -> AwsRegion.of("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Region name cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Builder Method Tests")
    class BuilderMethodTests {

        @Test
        @DisplayName("withDisplayName should create new region with updated display name")
        void withDisplayNameShouldCreateNewRegionWithUpdatedDisplayName() {
            // Given
            AwsRegion original = AwsRegion.of(REGION_NAME);
            String newDisplayName = "New Display Name";

            // When
            AwsRegion updated = original.withDisplayName(newDisplayName);

            // Then
            assertThat(updated.name()).isEqualTo(original.name());
            assertThat(updated.displayName()).isEqualTo(newDisplayName);
            assertThat(updated.availabilityZones()).isEqualTo(original.availabilityZones());
            assertThat(original.displayName()).isNull(); // Original should be unchanged
        }

        @Test
        @DisplayName("withAvailabilityZones should create new region with updated availability zones")
        void withAvailabilityZonesShouldCreateNewRegionWithUpdatedAvailabilityZones() {
            // Given
            AwsRegion original = AwsRegion.of(REGION_NAME);
            List<String> newAvailabilityZones = List.of("us-east-1a", "us-east-1b");

            // When
            AwsRegion updated = original.withAvailabilityZones(newAvailabilityZones);

            // Then
            assertThat(updated.name()).isEqualTo(original.name());
            assertThat(updated.displayName()).isEqualTo(original.displayName());
            assertThat(updated.availabilityZones()).isEqualTo(newAvailabilityZones);
            assertThat(original.availabilityZones()).isEmpty(); // Original should be unchanged
        }

        @Test
        @DisplayName("withAvailabilityZones should handle null availability zones")
        void withAvailabilityZonesShouldHandleNullAvailabilityZones() {
            // Given
            AwsRegion original = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // When
            AwsRegion updated = original.withAvailabilityZones(null);

            // Then
            assertThat(updated.availabilityZones()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Status Check Tests")
    class StatusCheckTests {

        @Test
        @DisplayName("hasDisplayName should return true when display name is present")
        void hasDisplayNameShouldReturnTrueWhenDisplayNameIsPresent() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // When & Then
            assertThat(region.hasDisplayName()).isTrue();
        }

        @Test
        @DisplayName("hasDisplayName should return false when display name is null")
        void hasDisplayNameShouldReturnFalseWhenDisplayNameIsNull() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When & Then
            assertThat(region.hasDisplayName()).isFalse();
        }

        @Test
        @DisplayName("hasDisplayName should return false when display name is empty")
        void hasDisplayNameShouldReturnFalseWhenDisplayNameIsEmpty() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, "", AVAILABILITY_ZONES);

            // When & Then
            assertThat(region.hasDisplayName()).isFalse();
        }

        @Test
        @DisplayName("hasDisplayName should return false when display name is blank")
        void hasDisplayNameShouldReturnFalseWhenDisplayNameIsBlank() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, "   ", AVAILABILITY_ZONES);

            // When & Then
            assertThat(region.hasDisplayName()).isFalse();
        }

        @Test
        @DisplayName("hasAvailabilityZones should return true when availability zones are present")
        void hasAvailabilityZonesShouldReturnTrueWhenAvailabilityZonesArePresent() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // When & Then
            assertThat(region.hasAvailabilityZones()).isTrue();
        }

        @Test
        @DisplayName("hasAvailabilityZones should return false when availability zones are null")
        void hasAvailabilityZonesShouldReturnFalseWhenAvailabilityZonesAreNull() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When & Then
            assertThat(region.hasAvailabilityZones()).isFalse();
        }

        @Test
        @DisplayName("hasAvailabilityZones should return false when availability zones are empty")
        void hasAvailabilityZonesShouldReturnFalseWhenAvailabilityZonesAreEmpty() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, List.of());

            // When & Then
            assertThat(region.hasAvailabilityZones()).isFalse();
        }

        @Test
        @DisplayName("getAvailabilityZoneCount should return correct count")
        void getAvailabilityZoneCountShouldReturnCorrectCount() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // When
            int count = region.getAvailabilityZoneCount();

            // Then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("getAvailabilityZoneCount should return zero for null availability zones")
        void getAvailabilityZoneCountShouldReturnZeroForNullAvailabilityZones() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When
            int count = region.getAvailabilityZoneCount();

            // Then
            assertThat(count).isZero();
        }
    }

    @Nested
    @DisplayName("Region Type Tests")
    class RegionTypeTests {

        @Test
        @DisplayName("isRegion should return true for matching region name")
        void isRegionShouldReturnTrueForMatchingRegionName() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When & Then
            assertThat(region.isRegion(REGION_NAME)).isTrue();
        }

        @Test
        @DisplayName("isRegion should return false for non-matching region name")
        void isRegionShouldReturnFalseForNonMatchingRegionName() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When & Then
            assertThat(region.isRegion("us-west-2")).isFalse();
        }

        @Test
        @DisplayName("isRegion should return false for null region name")
        void isRegionShouldReturnFalseForNullRegionName() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When & Then
            assertThat(region.isRegion(null)).isFalse();
        }

        @Test
        @DisplayName("isRegion should trim whitespace from region name")
        void isRegionShouldTrimWhitespaceFromRegionName() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When & Then
            assertThat(region.isRegion("  " + REGION_NAME + "  ")).isTrue();
        }

        @Test
        @DisplayName("isUSRegion should return true for US regions")
        void isUSRegionShouldReturnTrueForUSRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isUSRegion()).isTrue();
        }

        @Test
        @DisplayName("isUSRegion should return false for non-US regions")
        void isUSRegionShouldReturnFalseForNonUSRegions() {
            // Given
            AwsRegion region = AwsRegion.of("eu-west-1");

            // When & Then
            assertThat(region.isUSRegion()).isFalse();
        }

        @Test
        @DisplayName("isEuropeRegion should return true for Europe regions")
        void isEuropeRegionShouldReturnTrueForEuropeRegions() {
            // Given
            AwsRegion region = AwsRegion.of("eu-west-1");

            // When & Then
            assertThat(region.isEuropeRegion()).isTrue();
        }

        @Test
        @DisplayName("isEuropeRegion should return false for non-Europe regions")
        void isEuropeRegionShouldReturnFalseForNonEuropeRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isEuropeRegion()).isFalse();
        }

        @Test
        @DisplayName("isAsiaPacificRegion should return true for Asia Pacific regions")
        void isAsiaPacificRegionShouldReturnTrueForAsiaPacificRegions() {
            // Given
            AwsRegion region = AwsRegion.of("ap-southeast-1");

            // When & Then
            assertThat(region.isAsiaPacificRegion()).isTrue();
        }

        @Test
        @DisplayName("isAsiaPacificRegion should return false for non-Asia Pacific regions")
        void isAsiaPacificRegionShouldReturnFalseForNonAsiaPacificRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isAsiaPacificRegion()).isFalse();
        }

        @Test
        @DisplayName("isCanadaRegion should return true for Canada regions")
        void isCanadaRegionShouldReturnTrueForCanadaRegions() {
            // Given
            AwsRegion region = AwsRegion.of("ca-central-1");

            // When & Then
            assertThat(region.isCanadaRegion()).isTrue();
        }

        @Test
        @DisplayName("isCanadaRegion should return false for non-Canada regions")
        void isCanadaRegionShouldReturnFalseForNonCanadaRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isCanadaRegion()).isFalse();
        }

        @Test
        @DisplayName("isSouthAmericaRegion should return true for South America regions")
        void isSouthAmericaRegionShouldReturnTrueForSouthAmericaRegions() {
            // Given
            AwsRegion region = AwsRegion.of("sa-east-1");

            // When & Then
            assertThat(region.isSouthAmericaRegion()).isTrue();
        }

        @Test
        @DisplayName("isSouthAmericaRegion should return false for non-South America regions")
        void isSouthAmericaRegionShouldReturnFalseForNonSouthAmericaRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isSouthAmericaRegion()).isFalse();
        }

        @Test
        @DisplayName("isAfricaRegion should return true for Africa regions")
        void isAfricaRegionShouldReturnTrueForAfricaRegions() {
            // Given
            AwsRegion region = AwsRegion.of("af-south-1");

            // When & Then
            assertThat(region.isAfricaRegion()).isTrue();
        }

        @Test
        @DisplayName("isAfricaRegion should return false for non-Africa regions")
        void isAfricaRegionShouldReturnFalseForNonAfricaRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isAfricaRegion()).isFalse();
        }

        @Test
        @DisplayName("isMiddleEastRegion should return true for Middle East regions")
        void isMiddleEastRegionShouldReturnTrueForMiddleEastRegions() {
            // Given
            AwsRegion region = AwsRegion.of("me-south-1");

            // When & Then
            assertThat(region.isMiddleEastRegion()).isTrue();
        }

        @Test
        @DisplayName("isMiddleEastRegion should return false for non-Middle East regions")
        void isMiddleEastRegionShouldReturnFalseForNonMiddleEastRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isMiddleEastRegion()).isFalse();
        }

        @Test
        @DisplayName("isGovernmentRegion should return true for government regions")
        void isGovernmentRegionShouldReturnTrueForGovernmentRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-gov-east-1");

            // When & Then
            assertThat(region.isGovernmentRegion()).isTrue();
        }

        @Test
        @DisplayName("isGovernmentRegion should return false for non-government regions")
        void isGovernmentRegionShouldReturnFalseForNonGovernmentRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isGovernmentRegion()).isFalse();
        }

        @Test
        @DisplayName("isChinaRegion should return true for China regions")
        void isChinaRegionShouldReturnTrueForChinaRegions() {
            // Given
            AwsRegion region = AwsRegion.of("cn-north-1");

            // When & Then
            assertThat(region.isChinaRegion()).isTrue();
        }

        @Test
        @DisplayName("isChinaRegion should return false for non-China regions")
        void isChinaRegionShouldReturnFalseForNonChinaRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When & Then
            assertThat(region.isChinaRegion()).isFalse();
        }
    }

    @Nested
    @DisplayName("Continent Tests")
    class ContinentTests {

        @Test
        @DisplayName("getContinent should return North America for US regions")
        void getContinentShouldReturnNorthAmericaForUSRegions() {
            // Given
            AwsRegion region = AwsRegion.of("us-east-1");

            // When
            String continent = region.getContinent();

            // Then
            assertThat(continent).isEqualTo("North America");
        }

    }

    @Nested
    @DisplayName("Display Method Tests")
    class DisplayMethodTests {

        @Test
        @DisplayName("getDisplayName should return display name when available")
        void getDisplayNameShouldReturnDisplayNameWhenAvailable() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // When
            String displayName = region.getDisplayName();

            // Then
            assertThat(displayName).isEqualTo(DISPLAY_NAME);
        }

        @Test
        @DisplayName("getDisplayName should return region name when display name is not available")
        void getDisplayNameShouldReturnRegionNameWhenDisplayNameIsNotAvailable() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When
            String displayName = region.getDisplayName();

            // Then
            assertThat(displayName).isEqualTo(REGION_NAME);
        }

        @Test
        @DisplayName("getFormattedString should return formatted string with display name")
        void getFormattedStringShouldReturnFormattedStringWithDisplayName() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // When
            String formatted = region.getFormattedString();

            // Then
            assertThat(formatted).isEqualTo(DISPLAY_NAME + " (" + REGION_NAME + ")");
        }

        @Test
        @DisplayName("getFormattedString should return region name when display name is not available")
        void getFormattedStringShouldReturnRegionNameWhenDisplayNameIsNotAvailable() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When
            String formatted = region.getFormattedString();

            // Then
            assertThat(formatted).isEqualTo(REGION_NAME);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical AwsRegion")
        void shouldBeEqualToIdenticalAwsRegion() {
            // Given
            AwsRegion region1 = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);
            AwsRegion region2 = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // When & Then
            assertThat(region1).isEqualTo(region2);
            assertThat(region1.hashCode()).hasSameHashCodeAs(region2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different AwsRegion")
        void shouldNotBeEqualToDifferentAwsRegion() {
            // Given
            AwsRegion region1 = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);
            AwsRegion region2 = AwsRegion.of("us-west-2", DISPLAY_NAME, AVAILABILITY_ZONES);

            // When & Then
            assertThat(region1).isNotEqualTo(region2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);

            // When & Then
            assertThat(region).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME);
            String differentType = "not-region";

            // When & Then
            assertThat(region).isNotEqualTo(differentType);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should contain all fields")
        void toStringShouldContainAllFields() {
            // Given
            AwsRegion region = AwsRegion.of(REGION_NAME, DISPLAY_NAME, AVAILABILITY_ZONES);

            // When
            String string = region.toString();

            // Then
            assertThat(string).contains("AwsRegion{")
            .contains("name='" + REGION_NAME + "'")
            .contains("displayName='" + DISPLAY_NAME + "'")
            .contains("availabilityZones=" + AVAILABILITY_ZONES);
        }

        @Test
        @DisplayName("toString should handle null values")
        void toStringShouldHandleNullValues() {
            // Given
            AwsRegion region = new AwsRegion(null, null, null);

            // When
            String string = region.toString();

            // Then
            assertThat(string).contains("name='null'")
            .contains("displayName='null'")
            .contains("availabilityZones=null");
        }
    }
}
