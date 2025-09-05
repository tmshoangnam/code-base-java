package io.github.base.security.model;

import java.util.Objects;

/**
 * Represents a permission in the security system.
 * A permission defines a specific action that can be performed on a resource.
 */
public final class Permission {
    
    private final String name;
    private final String resource;
    private final String action;
    private final String description;
    
    /**
     * Creates a new permission with the specified name, resource, and action.
     *
     * @param name the permission name, must not be null or empty
     * @param resource the resource this permission applies to
     * @param action the action this permission allows
     * @param description the permission description
     */
    public Permission(String name, String resource, String action, String description) {
        this.name = Objects.requireNonNull(name, "Permission name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Permission name cannot be empty");
        }
        this.resource = resource;
        this.action = action;
        this.description = description;
    }
    
    /**
     * Creates a new permission with the specified name, resource, and action.
     *
     * @param name the permission name, must not be null or empty
     * @param resource the resource this permission applies to
     * @param action the action this permission allows
     */
    public Permission(String name, String resource, String action) {
        this(name, resource, action, null);
    }
    
    /**
     * Creates a new permission with only the specified name.
     *
     * @param name the permission name, must not be null or empty
     */
    public Permission(String name) {
        this(name, null, null, null);
    }
    
    /**
     * Gets the permission name.
     *
     * @return the permission name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the resource this permission applies to.
     *
     * @return the resource, may be null
     */
    public String getResource() {
        return resource;
    }
    
    /**
     * Gets the action this permission allows.
     *
     * @return the action, may be null
     */
    public String getAction() {
        return action;
    }
    
    /**
     * Gets the permission description.
     *
     * @return the permission description, may be null
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this permission matches the specified resource and action.
     *
     * @param resource the resource to check
     * @param action the action to check
     * @return true if this permission matches the resource and action, false otherwise
     */
    public boolean matches(String resource, String action) {
        boolean resourceMatches = this.resource == null || 
                (resource != null && this.resource.equals(resource));
        boolean actionMatches = this.action == null || 
                (action != null && this.action.equals(action));
        return resourceMatches && actionMatches;
    }
    
    /**
     * Checks if this permission matches the specified resource.
     *
     * @param resource the resource to check
     * @return true if this permission matches the resource, false otherwise
     */
    public boolean matchesResource(String resource) {
        return this.resource == null || 
                (resource != null && this.resource.equals(resource));
    }
    
    /**
     * Checks if this permission matches the specified action.
     *
     * @param action the action to check
     * @return true if this permission matches the action, false otherwise
     */
    public boolean matchesAction(String action) {
        return this.action == null || 
                (action != null && this.action.equals(action));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                ", resource='" + resource + '\'' +
                ", action='" + action + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
