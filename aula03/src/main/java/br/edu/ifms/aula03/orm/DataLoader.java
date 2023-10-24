package br.edu.ifms.aula03.orm;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.edu.ifms.aula03.repository.*;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PapelRepository papelRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		
		Papel papelAdmin = papelRepository.findByPapel("ADMIN");
		Papel papelUsuario = papelRepository.findByPapel("USER");
		if (papelAdmin == null) {
			Papel p = new Papel("ADMIN");
			papelRepository.save(p);
		}
		if (papelUsuario == null) {
			papelRepository.save(new Papel("USER"));
		}
		papelAdmin = papelRepository.findByPapel("ADMIN");
		papelUsuario = papelRepository.findByPapel("USER");
		
		if (usuarioRepository.findByLogin("angelica") == null){
			Usuario usuario2 = new Usuario("Angelica","30868601896",new Date(), "g@gmail.com",1,passwordEncoder.encode("123"),"angelica",true);
			List lista = Arrays.asList(papelAdmin);
			usuario2.setPapeis(lista);
			usuarioRepository.save(usuario2);
		} 
		if (usuarioRepository.findByLogin("gilberto") == null){
			Usuario usuario1 = new Usuario("Gilberto","30868601896",new Date(), "g@gmail.com",1,passwordEncoder.encode("123"),"gilberto",true);
			List lista = Arrays.asList(papelUsuario);
			usuario1.setPapeis(lista);
			usuarioRepository.save(usuario1);
		}
		
	}

}
