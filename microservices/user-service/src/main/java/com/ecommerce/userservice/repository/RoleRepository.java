package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.Role;
import com.ecommerce.userservice.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(String name);
    
    Optional<Role> findByNameAndCompanyId(String name, Long companyId);
    
    List<Role> findByCompanyId(Long companyId);
    
    List<Role> findByIsSystemRole(Boolean isSystemRole);
    
    List<Role> findByCompanyIdAndIsActive(Long companyId, Boolean isActive);
    
    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p = :permission")
    List<Role> findRolesWithPermission(@Param("permission") Permission permission);
    
    boolean existsByNameAndCompanyId(String name, Long companyId);
}
