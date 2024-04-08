package fr.eni.tp.enienchere.configuration.secutity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AppSecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth-> {

            auth.anyRequest().permitAll();

        });
      //  httpSecurity.formLogin(Customizer.withDefaults());

        httpSecurity.formLogin(form-> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/encheres");

        });

        httpSecurity.logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
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
}
