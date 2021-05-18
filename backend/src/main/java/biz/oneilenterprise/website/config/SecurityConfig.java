package biz.oneilenterprise.website.config;

import static biz.oneilenterprise.website.security.SecurityConstants.SIGN_UP_URL;

import biz.oneilenterprise.website.security.JWTAuthenticationFilter;
import biz.oneilenterprise.website.security.JWTAuthorizationFilter;
import biz.oneilenterprise.website.security.RestAccessDeniedHandler;
import biz.oneilenterprise.website.security.RestAuthenticationEntryPoint;
import biz.oneilenterprise.website.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }

    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JWTAuthorizationFilter(authenticationManagerBean());
    }

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .requiresChannel().anyRequest().requiresSecure()
            .and()
            .formLogin()
            .usernameParameter("email")
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .accessDeniedHandler(restAccessDeniedHandler)
            .and()
            .authorizeRequests().expressionHandler(webExpressionHandler())
            .antMatchers(SIGN_UP_URL).permitAll()
            .antMatchers("/gallery/upload").access("(hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_LIMITED_API'))")
            .antMatchers("/gallery/image/**", "/gallery/video/**", "/gallery/medias/**", "/auth/**", "/services/public/**",
                "/token/**", "/gallery/album/**", "/download/**", "/file/dl/*", "/info/*").permitAll()
            .antMatchers("/services/user/**").hasRole("USER")
            .antMatchers("/admin/**", "/services/admin/**", "/gallery/admin/**", "/user/admin/**", "/**/admin/**", "/actuator/**").access("hasRole('ROLE_ADMIN')")
            .anyRequest().hasAnyRole("ADMIN","USER", "UNREGISTERED")
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(jwtAuthorizationFilter())
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_OWNER > ROLE_ADMIN\nROLE_ADMIN > ROLE_TRUSTED_USER\nROLE_TRUSTED_USER > ROLE_USER");
        return roleHierarchy;
    }
}
