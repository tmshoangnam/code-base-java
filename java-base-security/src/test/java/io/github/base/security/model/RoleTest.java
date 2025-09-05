package io.github.base.security.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleTest {
    
    @Test
    void shouldCreateRoleWithName() {
        Role role = new Role("admin");
        
        assertThat(role.getName()).isEqualTo("admin");
        assertThat(role.getDescription()).isNull();
        assertThat(role.getPermissions()).isEmpty();
        assertThat(role.getParentRoles()).isEmpty();
    }
    
    @Test
    void shouldCreateRoleWithNameAndPermissions() {
        Permission permission1 = new Permission("read");
        Permission permission2 = new Permission("write");
        Role role = new Role("admin", Set.of(permission1, permission2));
        
        assertThat(role.getName()).isEqualTo("admin");
        assertThat(role.getPermissions()).containsExactlyInAnyOrder(permission1, permission2);
    }
    
    @Test
    void shouldCreateRoleWithAllProperties() {
        Permission permission = new Permission("read");
        Role role = new Role("admin", "Administrator role", Set.of(permission), Set.of("super-admin"));
        
        assertThat(role.getName()).isEqualTo("admin");
        assertThat(role.getDescription()).isEqualTo("Administrator role");
        assertThat(role.getPermissions()).containsExactly(permission);
        assertThat(role.getParentRoles()).containsExactly("super-admin");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new Role(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Role name cannot be null");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThatThrownBy(() -> new Role(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Role name cannot be empty");
    }
    
    @Test
    void shouldCheckPermission() {
        Permission permission = new Permission("read");
        Role role = new Role("admin", Set.of(permission));
        
        assertThat(role.hasPermission(permission)).isTrue();
        assertThat(role.hasPermission("read")).isTrue();
        assertThat(role.hasPermission("write")).isFalse();
    }
    
    @Test
    void shouldBeEqualBasedOnName() {
        Role role1 = new Role("admin");
        Role role2 = new Role("admin");
        Role role3 = new Role("user");
        
        assertThat(role1).isEqualTo(role2);
        assertThat(role1).isNotEqualTo(role3);
        assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
    }
}
