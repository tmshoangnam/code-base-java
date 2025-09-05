package io.github.base.security.model;

import java.util.Objects;
import java.util.Set;

/**
 * Represents a role in the security system.
 * A role can contain multiple permissions and can be hierarchical.
 */
public final class Role {
    
    private final String name;
    private final String description;
    private final Set<Permission> permissions;
    private final Set<String> parentRoles;
    
    /**
     * Creates a new role with the specified name and permissions.
     *
     * @param name the role name, must not be null or empty
     * @param description the role description
     * @param permissions the set of permissions for this role
     * @param parentRoles the set of parent role names
     */
    public Role(String name, String description, Set<Permission> permissions, Set<String> parentRoles) {
        this.name = Objects.requireNonNull(name, "Role name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }
        this.description = description;
        this.permissions = permissions != null ? Set.copyOf(permissions) : Set.of();
        this.parentRoles = parentRoles != null ? Set.copyOf(parentRoles) : Set.of();
    }
    
    /**
     * Creates a new role with the specified name and permissions.
     *
     * @param name the role name, must not be null or empty
     * @param permissions the set of permissions for this role
     */
    public Role(String name, Set<Permission> permissions) {
        this(name, null, permissions, null);
    }
    
    /**
     * Creates a new role with only the specified name.
     *
     * @param name the role name, must not be null or empty
     */
    public Role(String name) {
        this(name, null, null, null);
    }
    
    /**
     * Gets the role name.
     *
     * @return the role name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the role description.
     *
     * @return the role description, may be null
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the permissions associated with this role.
     *
     * @return an immutable set of permissions
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    /**
     * Gets the parent role names.
     *
     * @return an immutable set of parent role names
     */
    public Set<String> getParentRoles() {
        return parentRoles;
    }
    
    /**
     * Checks if this role has the specified permission.
     *
     * @param permission the permission to check
     * @return true if this role has the permission, false otherwise
     */
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
    
    /**
     * Checks if this role has a permission with the specified name.
     *
     * @param permissionName the permission name to check
     * @return true if this role has a permission with the specified name, false otherwise
     */
    public boolean hasPermission(String permissionName) {
        return permissions.stream()
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", permissions=" + permissions +
                ", parentRoles=" + parentRoles +
                '}';
    }
}
