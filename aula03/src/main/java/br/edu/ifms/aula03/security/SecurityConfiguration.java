package br.edu.ifms.aula03.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.edu.ifms.aula03.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired 
	private LoginSucesso loginSucesso;
	
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return new DetalheUsuarioServico(usuarioRepository);
	}
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http://localhost:8080/auth/admin/indexAdmin.html
		//http://localhost:8080/auth/user/indexUsuario.html
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/auth/user/*").hasAnyAuthority("USER","ADMIN")
		.antMatchers("/auth/admin/*").hasAnyAuthority("ADMIN")
		.antMatchers("/usuario/admin/*").hasAnyAuthority("ADMIN")
		.and()
		.exceptionHandling().accessDeniedPage("/publica-acesso-negado")
		.and()
		.formLogin().successHandler(loginSucesso)
		.loginPage("/login").permitAll()
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login").permitAll();
		http.csrf().disable();
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
	}
	
}
