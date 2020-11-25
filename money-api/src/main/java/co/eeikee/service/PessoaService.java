package co.eeikee.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import co.eeikee.model.Pessoa;
import co.eeikee.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired PessoaRepository pr;
	
	public Pessoa atualizar(Long id, Pessoa pessoa) {
			BeanUtils.copyProperties(pessoa, buscaPessoaId(id),"id");
			return pr.save(buscaPessoaId(id));
	}

	private Pessoa buscaPessoaId(Long id) {
		try {
			Pessoa pessoaSalva = pr.getOne(id);
			return pessoaSalva;
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		try {
			buscaPessoaId(id).setAtivo(ativo);
			pr.save(buscaPessoaId(id));
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
	}
}
