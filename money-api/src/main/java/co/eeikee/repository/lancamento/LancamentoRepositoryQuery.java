package co.eeikee.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import co.eeikee.model.Lancamento;
import co.eeikee.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery{
public Page<Lancamento> filtrar(LancamentoFilter lf, Pageable pageable);
}
