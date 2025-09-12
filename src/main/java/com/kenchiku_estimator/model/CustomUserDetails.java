package com.kenchiku_estimator.model;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private Account account;

  public CustomUserDetails(Account account) {
    this.account = account;
  }

  @Override
  public String getUsername() {
    return account.getUsername();
  }

  @Override
  public String getPassword() {
    return account.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String role = account.getRole();
    if (role == null) {
      return List.of();
    }
    return List.of(new SimpleGrantedAuthority("ROLE_" + role));
  }

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
}
