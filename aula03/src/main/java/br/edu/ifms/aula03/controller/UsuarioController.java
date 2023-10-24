package br.edu.ifms.aula03.controller;

import java.security.Principal;
import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifms.aula03.orm.Papel;
import br.edu.ifms.aula03.orm.Usuario;
import br.edu.ifms.aula03.repository.PapelRepository;
import br.edu.ifms.aula03.repository.UsuarioRepository;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PapelRepository papelRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private boolean temAutorizacao(Usuario usuario, String papel) {
		for (Papel pp : usuario.getPapeis()) {
			if (pp.getPapel().equals(papel)) {
				return true;
			}
	    }
		return false;
	}
	
	@RequestMapping("/index")
	public String index(Principal principal, Model model) {
		String login = principal.getName();
		Usuario usuario = usuarioRepository.findByLogin(login);		
		model.addAttribute("usuario", usuario);
		
		String redirectURL = "";
		if (temAutorizacao(usuario, "ADMIN")) {
            redirectURL = "/auth/admin/admin-index";
        } else if (temAutorizacao(usuario, "USER")) {
            redirectURL = "/auth/user/user-index";
        }
		return redirectURL;		
	}
	
	@RequestMapping("/admin/listar")
	public String listarUsuario(Model model) {
		model.addAttribute("usuarios", usuarioRepository.findAll());		
		return "/auth/admin/admin-listar-usuario";		
	}
	 
	@GetMapping("/novo")
	public String adicionarUsuario(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "/publica-criar-usuario";
	}
	
	@PostMapping("/salvar")
	public String salvarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "/publica-criar-usuario";
		}	
		
		Papel papelUsuario = papelRepository.findByPapel("USER");
		if (papelUsuario != null) {
			usuario.setPapeis(Arrays.asList(papelUsuario));
			String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
			usuario.setPassword(senhaCriptografada);
			usuario.setAtivo(true);
			usuarioRepository.save(usuario);			
		}
		attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
		return "redirect:/usuario/novo";
	}
	
	@GetMapping("/editar")
	public String editarUsuario(Principal principal, Model model) {
		String login = principal.getName();
		Usuario usuario = usuarioRepository.findByLogin(login);
		if (usuario == null) {
			throw new IllegalArgumentException("Login inválido:" + login);
		}
		usuario.setPassword(null);
	    model.addAttribute("usuario", usuario);
	    return "/auth/user/user-alterar-usuario";
	}
	
	@PostMapping("/editar/{id}")
	public String editarUsuario(@PathVariable("id") long id, @Valid Usuario usuario, 
	  BindingResult result, Model model) {
	    if (result.hasErrors()) {
	    	usuario.setId(id);
	        return "/auth/admin/admin-alterar-usuario";
	    }	    
	    Usuario oldUsuario = usuarioRepository.getById(id);
	   
	    if (oldUsuario != null) {
            usuario.setPapeis(oldUsuario.getPapeis());
            String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
    		usuario.setPassword(senhaCriptografada);
    		usuarioRepository.save(usuario);
        }else {
        	throw new IllegalArgumentException("usuário inválido");
        }	    
	    return "redirect:/usuario/index";
	}
	    
	@GetMapping("/admin/apagar/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		Usuario usuario = usuarioRepository.findById(id)
			      .orElseThrow(() -> new IllegalArgumentException("Id inválido:" + id));
		usuarioRepository.delete(usuario);
	    return "redirect:/usuario/admin/listar";
	}
	
}