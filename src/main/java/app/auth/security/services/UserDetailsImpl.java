package app.auth.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import app.auth.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;

  private String email;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  // Конструктор для создания объекта UserDetailsImpl на основе информации о пользователе
  public UserDetailsImpl(Long id, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  // Статический метод для создания UserDetailsImpl из объекта User
  public static UserDetailsImpl build(User user) {
    List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
            .collect(Collectors.toList());

    return new UserDetailsImpl(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            authorities);
  }

  // Метод, возвращающий список разрешений пользователя
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  // Метод, возвращающий идентификатор пользователя
  public Long getId() {
    return id;
  }

  // Метод, возвращающий адрес электронной почты пользователя
  public String getEmail() {
    return email;
  }

  // Метод, возвращающий пароль пользователя
  @Override
  public String getPassword() {
    return password;
  }

  // Метод, возвращающий имя пользователя
  @Override
  public String getUsername() {
    return username;
  }

  // Методы, определяющие состояние аккаунта пользователя
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  // Метод для сравнения двух объектов UserDetailsImpl
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
