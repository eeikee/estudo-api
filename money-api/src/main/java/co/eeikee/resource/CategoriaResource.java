package co.eeikee.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.eeikee.event.RecursoCriadoEvent;
import co.eeikee.model.Categoria;
import co.eeikee.repository.CategoriaRepository;
import co.eeikee.service.CategoriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Categorias")
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository cr;
	
	@Autowired
	private ApplicationEventPublisher aep;
	
	@Autowired
	private CategoriaService cs;
	
	@ApiOperation("Listar todas as Categorias")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@GetMapping
	public List<Categoria> listarCategoria(){
		 return cr.findAll();
	}
	
	@ApiOperation("Salvar uma nova categoria")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Categoria> criar(@ApiParam(name = "Corpo",value = "Representação de uma nova categoria")@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = cr.save(categoria);
		aep.publishEvent(new RecursoCriadoEvent(this, response, categoria.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	@ApiOperation("Buscar categorias pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> buscarPeloId(@ApiParam(value = "Id de uma nova categoria", example = "1")@PathVariable Long id) {
		return !cr.findById(id).isEmpty() ? ResponseEntity.ok(cr.getOne(id)): ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Remover categorias pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@ApiParam(value = "Id de uma categoria", example = "1")@PathVariable Long id){
		cr.deleteById(id);
	}
	
	@ApiOperation("Alterar categorias pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PutMapping("/{id}")
	public ResponseEntity<Categoria> atualizar(@ApiParam(value = "Id de uma categoria", example = "1")@PathVariable Long id,@ApiParam(name = "Corpo",value = "Representação de uma categoria")  @Validated @RequestBody Categoria categoria){
		return ResponseEntity.ok(cs.atualizarCategoria(id, categoria));
	}
}
