package br.edu.ifms.aula03.security;

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

import br.edu.ifms.aula03.orm.Papel;
//import com.example.securityteste.model.Papel;
import br.edu.ifms.aula03.orm.Usuario;
import br.edu.ifms.aula03.repository.UsuarioRepository;

@Service
@Transactional
public class DetalheUsuarioServico implements UserDetailsService{

	//injeta pelo construtor
	private UsuarioRepository usuarioRepository;
		
	public DetalheUsuarioServico(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Usuario usuario = usuarioRepository.findByLogin(username);
			if (usuario == null)
				return null;
			Set<GrantedAuthority> papeis = getPapeis(usuario);
			User user = new User(usuario.getLogin(), usuario.getPassword(), papeis);
			return user;
		} catch (Exception e) {
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

