package com.itos.talktalk.api.config;

import com.itos.talktalk.api.security.AccessTokenProvider;
import com.itos.talktalk.api.security.UnauthorizedHandler;
import com.itos.talktalk.api.security.AuthenticationFilter;
import com.itos.talktalk.api.security.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserPrincipalService userPrincipalService;

    private AccessTokenProvider accessTokenProvider;

    private UnauthorizedHandler unauthorizedHandler;

    @Autowired
    public SecurityConfig(
            UserPrincipalService userPrincipalService,
            AccessTokenProvider accessTokenProvider,
            UnauthorizedHandler unauthorizedHandler) {

        this.userPrincipalService = userPrincipalService;
        this.accessTokenProvider = accessTokenProvider;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(accessTokenProvider, userPrincipalService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userPrincipalService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                    .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/v2/api-docs",
                            "/swagger-resources/configuration/ui",
                            "/swagger-resources",
                            "/swagger-resources/configuration/security",
                            "/swagger-ui.html",
                            "/webjars/**")
                        .permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/auth")
                        .anonymous()
                    .antMatchers(HttpMethod.POST, "/api/v1/users")
                        .permitAll()
                    .anyRequest()
                        .authenticated();

        httpSecurity.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
