package com.wing.forutona.SpringSecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        http
                .addFilterAfter(bearerTokenAuthenticationFilter(), SecurityContextPersistenceFilter.class);

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/Notice**")
                .permitAll();

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/termsConditions**")
                .permitAll();

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/v1/FUserInfo/JoinUser")
                .permitAll();


        http.
                authorizeRequests()
                .antMatchers(HttpMethod.GET,"/v1/FUserInfo/CheckNickNameDuplication","/v1/FUserInfo/UserNickNameWithFullTextMatchIndex")
                .permitAll();

        http.
                authorizeRequests()
                .antMatchers("/v1/FUserInfo/SnsUserJoinCheckInfo")
                .permitAll();

        http
                .authorizeRequests()
                .antMatchers("/v1/FUserInfo**","/v1/FUserInfo/**")
                .authenticated();
    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        return new DefaultBearerTokenResolver();
    }

    @Bean
    public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter() throws Exception {
        return new BearerTokenAuthenticationFilter(authenticationManager());
    }

}
