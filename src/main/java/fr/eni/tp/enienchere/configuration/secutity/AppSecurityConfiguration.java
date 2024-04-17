package fr.eni.tp.enienchere.configuration.secutity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.io.IOException;

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

        httpSecurity
                .sessionManagement(session -> session
                        .sessionFixation(fixation -> fixation.none())
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                            @Override
                            public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
                                HttpServletRequest request = event.getRequest();
                                if (!isRememberMeCookiePresent(request)) {
                                    // Supprimez le userSession uniquement si le cookie "remember-me" n'est pas présent
                                    request.getSession().removeAttribute("userSession");
                                    // Redirigez l'utilisateur vers la page de connexion ou effectuez d'autres actions de déconnexion
                                    event.getResponse().sendRedirect("/login");
                                }
                            }
                        })
                );
        return httpSecurity.build();
    }

    private boolean isRememberMeCookiePresent(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SPRING_SECURITY_REMEMBER_ME_COOKIE")) {
                    return true;
                }
            }
        }
        return false;
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
