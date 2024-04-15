package fr.eni.tp.enienchere.configuration.secutity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AppSecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth-> {

            auth.requestMatchers("/").permitAll();
            auth.requestMatchers("/encheres/").permitAll();
            auth.requestMatchers("/css/**").permitAll();
            auth.requestMatchers("/images/**").permitAll();
            auth.requestMatchers("/inscription").permitAll();
            auth.anyRequest().authenticated();


        });
        httpSecurity.csrf(Customizer.withDefaults());

        httpSecurity.formLogin(form-> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/login/details", true);
            form.failureUrl("/login-error");
        });



        httpSecurity.logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/encheres/")
                .permitAll()
        );

        return httpSecurity.build();
    }

    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource) {
        String selectUserByPseudo = "SELECT username, password, 1 FROM USERS WHERE username = ?";
        String selectRolesByAdmin = "SELECT username, admin FROM USERS WHERE username = ?";


        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(selectUserByPseudo);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(selectRolesByAdmin);

        return jdbcUserDetailsManager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
