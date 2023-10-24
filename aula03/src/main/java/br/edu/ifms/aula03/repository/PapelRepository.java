package br.edu.ifms.aula03.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.aula03.orm.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long> {
	Papel findByPapel(String login);
}
