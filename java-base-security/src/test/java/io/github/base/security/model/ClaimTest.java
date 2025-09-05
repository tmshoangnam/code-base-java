package io.github.base.security.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClaimTest {
    
    @Test
    void shouldCreateClaimWithNameAndValue() {
        Claim claim = new Claim("sub", "user123");
        
        assertThat(claim.getName()).isEqualTo("sub");
        assertThat(claim.getValue()).isEqualTo("user123");
        assertThat(claim.getType()).isEqualTo(Claim.ClaimType.STRING);
    }
    
    @Test
    void shouldCreateClaimWithNameValueAndType() {
        Claim claim = new Claim("active", true, Claim.ClaimType.BOOLEAN);
        
        assertThat(claim.getName()).isEqualTo("active");
        assertThat(claim.getValue()).isEqualTo(true);
        assertThat(claim.getType()).isEqualTo(Claim.ClaimType.BOOLEAN);
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new Claim(null, "value"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Claim name cannot be null");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThatThrownBy(() -> new Claim("", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Claim name cannot be empty");
    }
    
    @Test
    void shouldGetStringValue() {
        Claim claim = new Claim("sub", "user123");
        
        assertThat(claim.getStringValue()).isEqualTo("user123");
    }
    
    @Test
    void shouldGetBooleanValue() {
        Claim claim = new Claim("active", true, Claim.ClaimType.BOOLEAN);
        
        assertThat(claim.getBooleanValue()).isTrue();
    }
    
    @Test
    void shouldGetBooleanValueFromString() {
        Claim claim = new Claim("active", "true");
        
        assertThat(claim.getBooleanValue()).isTrue();
    }
    
    @Test
    void shouldGetIntValue() {
        Claim claim = new Claim("count", 42, Claim.ClaimType.INTEGER);
        
        assertThat(claim.getIntValue()).isEqualTo(42);
    }
    
    @Test
    void shouldGetIntValueFromString() {
        Claim claim = new Claim("count", "42");
        
        assertThat(claim.getIntValue()).isEqualTo(42);
    }
    
    @Test
    void shouldGetLongValue() {
        Claim claim = new Claim("timestamp", 1234567890L, Claim.ClaimType.LONG);
        
        assertThat(claim.getLongValue()).isEqualTo(1234567890L);
    }
    
    @Test
    void shouldGetLongValueFromString() {
        Claim claim = new Claim("timestamp", "1234567890");
        
        assertThat(claim.getLongValue()).isEqualTo(1234567890L);
    }
    
    @Test
    void shouldBeEqualBasedOnName() {
        Claim claim1 = new Claim("sub", "user123");
        Claim claim2 = new Claim("sub", "user456");
        Claim claim3 = new Claim("name", "user123");
        
        assertThat(claim1).isEqualTo(claim2);
        assertThat(claim1).isNotEqualTo(claim3);
        assertThat(claim1.hashCode()).isEqualTo(claim2.hashCode());
    }
}
