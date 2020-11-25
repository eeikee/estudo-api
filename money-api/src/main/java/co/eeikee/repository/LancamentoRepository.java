package co.eeikee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.eeikee.model.Lancamento;
import co.eeikee.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{

}
