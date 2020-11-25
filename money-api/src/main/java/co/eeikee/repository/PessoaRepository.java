package co.eeikee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.eeikee.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
