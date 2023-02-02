package auth.backend.gateway_client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Slf4j
@Configuration
public class SecurityConfig {

    /**
     * https://www.baeldung.com/spring-webclient-oauth2
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        log.info("Configuring HTTP Security...");
        log.info("\t-Disabling CSRF");

        http.csrf().disable();


        log.info("\t-Authorization Rules:");
        log.info("\t\t-Actuator API requires role ADMIN");
        log.info("\t\t-Gateway route deletion requires authority SCOPE_route.delete");
        log.info("\t\t-Gateway route creation requires authority SCOPE_route.create");
        log.info("\t\t-Gateway route access requires authority SCOPE_route.read");
        log.info("\t\t-Authenticate any exchange");

        http.authorizeExchange((authorize) -> authorize
                .pathMatchers("/actuator/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE,"/actuator/**" ).hasAuthority("SCOPE_route.delete")
                .pathMatchers(HttpMethod.POST,"/actuator/**" ).hasAuthority("SCOPE_route.create")
                .pathMatchers(HttpMethod.GET,"/actuator/**" ).hasAuthority("SCOPE_route.read")
                .anyExchange()
                .authenticated()
        );


        log.info("\t-Enabling OAuth2.0 login");

        http.oauth2Login();


        log.info("Building HTTP Security.");

        return http.build();
    }

}
