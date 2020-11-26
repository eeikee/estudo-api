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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Pessoas")
@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaRepository pr;
	
	
	@Autowired
	private ApplicationEventPublisher aep;
	
	@Autowired
	private PessoaService ps;
	
	@ApiOperation("Listar todas as pessoas")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@GetMapping
	public List<Pessoa> listarPessoa(){
		 return pr.findAll();
	}
	
	@ApiOperation("Salvar uma nova pessoa")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Pessoa> criar(@ApiParam(name = "Corpo",value = "Representação de uma nova pessoa")@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pr.save(pessoa);
		aep.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@ApiOperation("Buscar pessoas pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@GetMapping("/{id}")
	public ResponseEntity<Pessoa> buscarPeloId(@ApiParam(value = "Id de uma pessoa", example = "1")@PathVariable Long id) {
		return !pr.findById(id).isEmpty() ? ResponseEntity.ok(pr.getOne(id)): ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Deletar uma pessoa pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@ApiParam(value = "Id de uma pessoa", example = "1")@PathVariable Long id){
		pr.deleteById(id);
	}
	
	@ApiOperation("Alterar uma pessoa pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PutMapping("/{id}")
	public ResponseEntity<Pessoa> atualizar(@ApiParam(value = "Id de uma pessoa", example = "1")@PathVariable Long id, @ApiParam(name = "Corpo",value = "Representação de uma nova pessoa")@Validated @RequestBody Pessoa pessoa){
		return ResponseEntity.ok(ps.atualizar(id, pessoa));
	}
	
	@ApiOperation("Alterar a propriedade 'ativo' pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@ApiParam(value = "Id de uma pessoa",example = "1")@PathVariable Long id, @ApiParam(value = "Ativo de uma pessoa", example = "true")@RequestBody Boolean ativo) {
		ps.atualizarPropriedadeAtivo(id, ativo);
	}
}
