package co.eeikee.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import co.eeikee.model.Lancamento;
import co.eeikee.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{

	@PersistenceContext
	private EntityManager manager;
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lf, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lf,builder,root);
		criteria.where(predicates);
		
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lf));
	}
	
	private Long total(LancamentoFilter lf) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lf, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroPagina);
		query.setMaxResults(totalRegistrosPorPagina);	
	}

	private Predicate[] criarRestricoes(LancamentoFilter lf, CriteriaBuilder builder, Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (lf.getDescricao() != null) {
			predicates.add(builder.like(builder.lower(root.get("descricao")), "%" + lf.getDescricao().toLowerCase() + "%"));
		}
		if(lf.getDataVencimentoDe() != null) {
			predicates.add(
					builder.greaterThanOrEqualTo(root.get("dataVencimento"), lf.getDataVencimentoDe()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}


}
