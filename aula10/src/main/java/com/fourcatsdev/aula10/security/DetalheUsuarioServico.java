package com.fourcatsdev.aula10.security;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fourcatsdev.aula10.modelo.Papel;
import com.fourcatsdev.aula10.modelo.Usuario;
import com.fourcatsdev.aula10.repository.UsuarioRepository;



@Service
@Transactional
public class DetalheUsuarioServico implements UserDetailsService{

	//injeta pelo construtor
	private UsuarioRepository usuarioRepository;
		
	public DetalheUsuarioServico(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	//https://stackoverflow.com/questions/10852703/when-loaduserbyusername-is-invoked-spring-security
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByLogin(username);
			
		if(usuario != null && usuario.isAtivo()) {
			Set<GrantedAuthority> papeis = getPapeis(usuario);
			User user = new User(usuario.getLogin(), usuario.getPassword(), papeis);
			return user;
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
	}
	
	private Set<GrantedAuthority> getPapeis(Usuario usuario	){
		Set<GrantedAuthority> papeis = new HashSet<GrantedAuthority>();
		for(Papel papel: usuario.getPapeis()) {
			GrantedAuthority pp = new SimpleGrantedAuthority(papel.getPapel());
			papeis.add(pp);
		}
		return papeis;
	}

}