package br.edu.ifms.aula03.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.aula03.orm.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Usuario findByLogin(String login);
}
