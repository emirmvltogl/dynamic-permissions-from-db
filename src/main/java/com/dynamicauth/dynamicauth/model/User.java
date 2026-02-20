package com.dynamicauth.dynamicauth.model;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "users")
public class User {

  // not : lombok kullanıyorsan @Data kullanma çünkü equals ve hashcode metodları
  // id'ye göre oluşturulmaz, bu da sorunlara yol açabilir. Bu yüzden @Data yerine
  // @Getter, @Setter, @AllArgsConstructor ve
  // @EqualsAndHashCode(onlyExplicitlyIncluded = true) kullanarak sadece id'ye
  // göre equals ve hashcode oluşturuyoruz. hibernate ile çalışırken bu önemlidir
  // çünkü entity'ler genellikle id'ye göre tanımlanır ve id'ye göre
  // karşılaştırılır. eğer @Data kullanırsan, equals ve hashcode metodları tüm
  // alanlara göre oluşturulur ve bu da hibernate ile çalışırken sorunlara yol
  // açabilir. bu yüzden @Data yerine @Getter, @Setter, @AllArgsConstructor ve
  // @EqualsAndHashCode(onlyExplicitlyIncluded = true) kullanarak sadece id'ye
  // göre equals ve hashcode oluşturuyoruz. |......::::::ÖNEMLİ::::::......|

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "reset_token")
  private String resetToken;

  @Column(name = "reset_token_expiry")
  private LocalDateTime resetTokenExpiry;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  public User() {
  }

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public User(String username, String password, String email, Set<Role> roles) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.roles = roles;
  }

}