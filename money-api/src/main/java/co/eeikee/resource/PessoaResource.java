package co.eeikee.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import co.eeikee.model.Pessoa;
import co.eeikee.repository.PessoaRepository;
import co.eeikee.service.PessoaService;
import io.swagger.annotations.ApiImplicitParam;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaRepository pr;
	
	
	@Autowired
	private ApplicationEventPublisher aep;
	
	@Autowired
	private PessoaService ps;
	
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@GetMapping
	public List<Pessoa> listarPessoa(){
		 return pr.findAll();
	}
	
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pr.save(pessoa);
		aep.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@GetMapping("/{id}")
	public ResponseEntity<Pessoa> buscarPeloId(@PathVariable Long id) {
		return !pr.findById(id).isEmpty() ? ResponseEntity.ok(pr.getOne(id)): ResponseEntity.notFound().build();
	}
	
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id){
		pr.deleteById(id);
	}
	
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PutMapping("/{id}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long id, @Validated @RequestBody Pessoa pessoa){
		return ResponseEntity.ok(ps.atualizar(id, pessoa));
	}
	
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
		ps.atualizarPropriedadeAtivo(id, ativo);
	}
}
