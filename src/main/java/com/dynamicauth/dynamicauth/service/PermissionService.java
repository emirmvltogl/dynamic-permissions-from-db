package com.dynamicauth.dynamicauth.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.dynamicauth.dynamicauth.model.Permission;
import com.dynamicauth.dynamicauth.model.Role;
import com.dynamicauth.dynamicauth.model.User;
import com.dynamicauth.dynamicauth.repository.PermissionRepository;
import com.dynamicauth.dynamicauth.repository.RoleRepository;
import com.dynamicauth.dynamicauth.repository.UserRepository;

@Service
public class PermissionService {

  // tüm rol ve kullanıcı izinlerini kontrol edeceğimiz servis olacak. Burada da
  // repositoryleri kullanarak işlemler yapacağız.

  private final PermissionRepository permissionRepository;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  public PermissionService(PermissionRepository permissionRepository, RoleRepository roleRepository,
      UserRepository userRepository) {
    this.permissionRepository = permissionRepository;
  }

  public List<Permission> getPermissionsByRole(Set<Role> roles) {

    if (roles.isEmpty()) {
      throw new RuntimeException("Role not found");
    }
    return roles.stream()
        .flatMap(role -> permissionRepository
            .findAllByRoles_Id(role.getId())
            .stream())
        .distinct()
        .toList();
  }

  public List<Permission> getPermissionsByUser(User user) {
    if (user == null) {
      throw new RuntimeException("User not found");
    }

    Set<Role> roles = user.getRoles();
    if (roles.isEmpty()) {
      throw new RuntimeException("Role not found for the user");
    }

    return getPermissionsByRole(roles);
  }

  public boolean hasPermission(User user, String endpoint, String method) {

    List<Permission> permissions = getPermissionsByUser(user);

    return permissions.stream()
        .anyMatch(permission -> pathMatcher.match(permission.getEndpoint(), endpoint)
            && permission.getMethod().equalsIgnoreCase(method));
  }

}
