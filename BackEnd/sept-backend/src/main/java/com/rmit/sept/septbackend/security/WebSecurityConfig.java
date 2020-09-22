package com.rmit.sept.septbackend.security;

import com.rmit.sept.septbackend.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.addAllowedOrigin("*");
                corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }).and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/service").hasAuthority(Role.ADMIN.name())
                .antMatchers( "/api/v1/worker").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/api/v1/service").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/api/v1/service").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/api/v1/service").fullyAuthenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/booking").hasAuthority(Role.CUSTOMER.name())
                .antMatchers(HttpMethod.POST, "/api/v1/booking").hasAuthority(Role.CUSTOMER.name())
                .antMatchers(HttpMethod.DELETE, "/api/v1/booking").hasAuthority(Role.CUSTOMER.name())
                .antMatchers(HttpMethod.GET, "/api/v1/business").fullyAuthenticated()
                .antMatchers("/api/test/**").permitAll()
                .anyRequest().authenticated();

        // For the H2 console - can be removed later if needed
        http.headers().frameOptions().disable();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}

