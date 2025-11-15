package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.CreateRoleRequest;
import com.ecommerce.userservice.dto.response.RoleResponse;
import com.ecommerce.userservice.enums.Permission;

import java.util.List;
import java.util.Set;

/**
 * Role and permission management service
 */
public interface RoleService {
    
    // Role CRUD
    RoleResponse createRole(Long companyId, CreateRoleRequest request);
    RoleResponse updateRole(Long roleId, CreateRoleRequest request);
    void deleteRole(Long roleId);
    RoleResponse getRoleById(Long roleId);
    RoleResponse getRoleByName(String name, Long companyId);
    List<RoleResponse> getAllRoles(Long companyId);
    List<RoleResponse> getSystemRoles();
    
    // Permission management
    RoleResponse addPermission(Long roleId, Permission permission);
    RoleResponse removePermission(Long roleId, Permission permission);
    RoleResponse setPermissions(Long roleId, Set<Permission> permissions);
    boolean hasPermission(Long roleId, Permission permission);
    Set<Permission> getRolePermissions(Long roleId);
    
    // User-Role assignment
    void assignRoleToUser(Long userId, Long roleId, Long assignedBy);
    void removeRoleFromUser(Long userId, Long roleId);
    void setUserRoles(Long userId, List<Long> roleIds, Long assignedBy);
    List<RoleResponse> getUserRoles(Long userId);
    Set<Permission> getUserPermissions(Long userId);
    boolean userHasPermission(Long userId, Permission permission);
    boolean userHasRole(Long userId, String roleName);
    
    // Bulk operations
    void assignRoleToUsers(List<Long> userIds, Long roleId, Long assignedBy);
    void removeRoleFromUsers(List<Long> userIds, Long roleId);
    
    // Validation
    boolean canAssignRole(Long userId, Long roleId);
    List<String> getValidationErrors(CreateRoleRequest request);
}
