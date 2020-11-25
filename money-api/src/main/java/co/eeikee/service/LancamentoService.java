package co.eeikee.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import co.eeikee.model.Lancamento;
import co.eeikee.repository.LancamentoRepository;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lr;

	private Lancamento buscaLancamentoId(Long id) {
		try {
			Lancamento lancamentoSalvo = lr.getOne(id);
			return lancamentoSalvo;
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
	}

	public Lancamento atualizar(Long id, Lancamento lancamento) {
		BeanUtils.copyProperties(lancamento, buscaLancamentoId(id), "id");
		return lr.save(buscaLancamentoId(id));
	}
}
