package com.TourismApp.TourismApplication.Configuration;

import com.TourismApp.TourismApplication.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter authFilter;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    //authentication
    public UserDetailsService userDetailsService() {

        return new UserInfoUserDetailsService();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/api/users/add").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/auth/authenticate/**").permitAll()
                .requestMatchers("/auth/signup/**").permitAll()
                .requestMatchers("/api/users/all",
                        "/api/users/delete/**", "/api/users/approve/**", "/api/users/unapproved/**",
                        "/api/users/archive/**" , "/api/users/isarchive", "/api/contenus/approve/**",
                        "/api/contenus/unapproved/**", "/api/contenus/approved/**",
                        "/api/contenus/update/**","/api/contenus/archiveContenu/**",
                        "/api/contenus/isarchiveContenu/**"
                       ,"/api/programs/updateProgram/**"
                        ,"/api/programs/archiveProgram/**"
                        ,"/api/programs/isarchiveProgram/**","/api/programs/deleteProgram/**")
                .hasAuthority("admin")
                .requestMatchers("/api/contenus/all", "/api/contenus/contenu/**","/api/users/approved",
                        "/api/contenus/videos/**", "/api/programs/gelAllPrograms/**",
                        "/api/programs/program/**","/api/programs/program/**","/api/contenus/getContenusByIds"
                        , "/api/items/**","/api/contenus/isunarchiveContenu/**","/api/users/by/**",
                        "/api/users/byFirstName/**","/api/users/currentUser/**","/api/users/update/**",
                        "api/users/update-profile/**","api/users/images/**","/api/contacts/contact")
                .permitAll()
                .requestMatchers( "/api/contenus/delete/**")
                .hasAuthority("createur")


                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                });


        return http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    //
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
