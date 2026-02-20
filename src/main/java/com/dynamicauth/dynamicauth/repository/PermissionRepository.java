package com.dynamicauth.dynamicauth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dynamicauth.dynamicauth.model.Permission;
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

  //findAllByRoles_Id burda _Id ekleyerek role id ye göre izinleri buluyoruz. Eğer role name e göre bulmak istersek findAllByRoles_Name kullanabiliriz. 
  //findByEndpointAndMethod ise endpoint ve method a göre izin bulmak için kullanılır. Bu da özellikle hasPermission metodunda kullanışlı olabilir.

  Optional<Permission> findByEndpointAndMethod(String endpoint, String method);

  // Find all permissions for a role by role id
  List<Permission> findAllByRoles_Id(Long roleId);

  // Or find all permissions for a role by role name
  List<Permission> findAllByRoles_Name(String roleName);

}
