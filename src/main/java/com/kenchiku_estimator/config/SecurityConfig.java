package com.kenchiku_estimator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	UserDetailsService userDetailsService;

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// ログイン不要ページの設定
		http.authorizeHttpRequests(authorize -> authorize // 直リンクOK
				.requestMatchers("/index", "/error").permitAll()// 直リンクOK
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().anyRequest()
				.authenticated());

		// ログイン処理
		http.formLogin(login -> login
				.loginProcessingUrl("/login")
				.loginPage("/index")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/estimates", true)
				.failureUrl("/index?error")
				.permitAll())

				// ログアウト処理
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/index"))

				.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));

		/*
		 * CSRF 対策を無効に設定 (一時的) http.csrf(csrf -> csrf.disable());
		 */

		return http.build();
	}
}
