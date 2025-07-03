package com.kenchiku_estimator.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public MvcRequestMatcher.Builder mvcRequestMatcherBuilder(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

		//ログイン不要ページの設定
		http.authorizeHttpRequests(authorize -> authorize //直リンクOK
				.requestMatchers(mvc.pattern("/index")).permitAll()//直リンクOK
				.requestMatchers(mvc.pattern("/error")).permitAll()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.anyRequest().authenticated());

		// ログイン処理
		http.formLogin(login -> login
				.loginProcessingUrl("/login")
				.loginPage("/index")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/estimates", true)
				.permitAll())


				//ログアウト処理
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/index"))

				.headers(headers -> headers
						.frameOptions(FrameOptionsConfig::disable));

		/* CSRF 対策を無効に設定 (一時的) 
		http.csrf(csrf -> csrf.disable());*/

		return http.build();
	}
}


