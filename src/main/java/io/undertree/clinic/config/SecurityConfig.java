package io.undertree.clinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("patient1").password(passwordEncoder().encode("Password1")).roles("PATIENT").and()
                .withUser("patient2").password(passwordEncoder().encode("Password2")).roles("PATIENT").and()
                .withUser("patient3").password(passwordEncoder().encode("Password3")).roles("PATIENT").and()
                .withUser("doctor1").password(passwordEncoder().encode("Password1")).roles("DOCTOR").and()
                .withUser("doctor2").password(passwordEncoder().encode("Password2")).roles("DOCTOR").and()
                .withUser("doctor3").password(passwordEncoder().encode("Password3")).roles("DOCTOR").and()
                .withUser("admin1").password(passwordEncoder().encode("Password1")).roles("ADMIN");
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**").and().httpBasic();
    }
}
