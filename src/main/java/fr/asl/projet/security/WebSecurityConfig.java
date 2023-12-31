package fr.asl.projet.security;

import fr.asl.projet.service.ClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/login").anonymous()
                .requestMatchers("/", "/error", "/cart", "/search", "/searchAdvanced", "/addClient", "/addLibrarian").permitAll()
                .requestMatchers("/account", "/addOpinion").hasAnyRole("ADMIN", "CLIENT")
                .requestMatchers("/validateLibrarian", "/category", "/recapCommand").hasRole("ADMIN")
                .requestMatchers("/addBook", "/validateCommand", "/opinion").hasRole("LIBRARIAN")
                .anyRequest().authenticated()
        ).formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
        ).logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(clientDetailsService);
    }
}
