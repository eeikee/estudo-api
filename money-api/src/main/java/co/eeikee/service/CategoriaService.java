package co.eeikee.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.eeikee.model.Categoria;
import co.eeikee.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	CategoriaRepository cr;
	
	public Categoria atualizarCategoria(Long id, Categoria categoria) {
		BeanUtils.copyProperties(categoria,  cr.getOne(id),"id");
		return cr.save( cr.getOne(id));
	}
}
