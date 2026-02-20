package com.dynamicauth.dynamicauth.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "permissions")
public class Permission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToMany(mappedBy = "permissions")
  private Set<Role> roles;

  @Column(name = "endpoint", nullable = false)
  private String endpoint;

  @Column(name = "method", nullable = false)
  private String method;

  public Permission(){
  }

  public Permission(String endpoint, String method) {
    this.endpoint = endpoint;
    this.method = method;
  }

}
