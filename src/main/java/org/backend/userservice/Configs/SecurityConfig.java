package org.backend.userservice.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeRequests) ->{
            try{
                authorizeRequests.anyRequest().permitAll()
                        .and().cors().disable()
                        .csrf().disable();
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        });
        return http.build();
    }

}
