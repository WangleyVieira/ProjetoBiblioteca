package com.fourcatsdev.aula10.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fourcatsdev.aula10.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Usuario findByLogin(String login);
}
