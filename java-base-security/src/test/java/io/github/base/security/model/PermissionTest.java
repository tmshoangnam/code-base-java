package io.github.base.security.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PermissionTest {
    
    @Test
    void shouldCreatePermissionWithName() {
        Permission permission = new Permission("read");
        
        assertThat(permission.getName()).isEqualTo("read");
        assertThat(permission.getResource()).isNull();
        assertThat(permission.getAction()).isNull();
        assertThat(permission.getDescription()).isNull();
    }
    
    @Test
    void shouldCreatePermissionWithNameAndResource() {
        Permission permission = new Permission("read", "users", "read");
        
        assertThat(permission.getName()).isEqualTo("read");
        assertThat(permission.getResource()).isEqualTo("users");
        assertThat(permission.getAction()).isEqualTo("read");
    }
    
    @Test
    void shouldCreatePermissionWithAllProperties() {
        Permission permission = new Permission("read", "users", "read", "Read user data");
        
        assertThat(permission.getName()).isEqualTo("read");
        assertThat(permission.getResource()).isEqualTo("users");
        assertThat(permission.getAction()).isEqualTo("read");
        assertThat(permission.getDescription()).isEqualTo("Read user data");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new Permission(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Permission name cannot be null");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThatThrownBy(() -> new Permission(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Permission name cannot be empty");
    }
    
    @Test
    void shouldMatchResourceAndAction() {
        Permission permission = new Permission("read", "users", "read");
        
        assertThat(permission.matches("users", "read")).isTrue();
        assertThat(permission.matches("users", "write")).isFalse();
        assertThat(permission.matches("orders", "read")).isFalse();
        assertThat(permission.matches("orders", "write")).isFalse();
    }
    
    @Test
    void shouldMatchResourceOnly() {
        Permission permission = new Permission("read", "users", null);
        
        assertThat(permission.matchesResource("users")).isTrue();
        assertThat(permission.matchesResource("orders")).isFalse();
    }
    
    @Test
    void shouldMatchActionOnly() {
        Permission permission = new Permission("read", null, "read");
        
        assertThat(permission.matchesAction("read")).isTrue();
        assertThat(permission.matchesAction("write")).isFalse();
    }
    
    @Test
    void shouldBeEqualBasedOnName() {
        Permission permission1 = new Permission("read");
        Permission permission2 = new Permission("read");
        Permission permission3 = new Permission("write");
        
        assertThat(permission1).isEqualTo(permission2);
        assertThat(permission1).isNotEqualTo(permission3);
        assertThat(permission1.hashCode()).isEqualTo(permission2.hashCode());
    }
}
