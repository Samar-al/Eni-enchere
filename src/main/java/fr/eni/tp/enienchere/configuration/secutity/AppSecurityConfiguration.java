package fr.eni.tp.enienchere.configuration.secutity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AppSecurityConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth-> {

            auth.requestMatchers("/").permitAll();
            auth.requestMatchers("/encheres/").permitAll();
            auth.requestMatchers("/css/**").permitAll();
            auth.requestMatchers("/images/**").permitAll();
            auth.requestMatchers("/js/*").permitAll();
            auth.requestMatchers("/inscription").permitAll();
            auth.requestMatchers("/encheres/search").permitAll();
            auth.requestMatchers("/encheres/reset-password").permitAll();
            auth.requestMatchers("/encheres/forgot-password").permitAll();
            auth.anyRequest().authenticated();


        });
        httpSecurity.csrf(Customizer.withDefaults());

        httpSecurity.formLogin(form-> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/login/details", true);
            form.failureUrl("/login-error");
        });

        httpSecurity
                .rememberMe(rememberMe -> rememberMe
                        .tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(Integer.MAX_VALUE) // Lifetime environ 68 ans
                        .rememberMeParameter("saveMe")
                );

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
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // Vous pouvez personnaliser le schéma de la table en définissant les propriétés suivantes :
        // tokenRepository.setCreateTableOnStartup(false);
        // tokenRepository.setTableName("persistent_logins");
        return tokenRepository;
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
