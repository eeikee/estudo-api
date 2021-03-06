package co.eeikee.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import co.eeikee.model.Lancamento;
import co.eeikee.repository.LancamentoRepository;
import co.eeikee.repository.filter.LancamentoFilter;
import co.eeikee.service.LancamentoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Lançamentos")
@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lr;

	@Autowired
	private ApplicationEventPublisher aep;

	@Autowired
	private LancamentoService ls;

	@ApiOperation("Listar todos os lançamentos")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@GetMapping
	public Page<Lancamento> pesquisarLancamentos(@ApiParam(name = "Filtro",value = "Filtro para busca de um lançamento", example = "nome")LancamentoFilter filter, @ApiParam(name = "Paginação",value = "Representação de uma nova pessoa", example = "1")Pageable pageable) {
		return lr.filtrar(filter, pageable);
	}

	@ApiOperation("Salvar um novo lançamentos")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Lancamento> criar(@ApiParam(name = "Corpo",value = "Representação de uma novo lançamento")@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalva = lr.save(lancamento);
		aep.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalva);
	}

	@ApiOperation("Buscar lançamentos pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> buscarPeloId(@ApiParam(value = "Id de um lançamento", example = "1")@PathVariable Long id) {
		return !lr.findById(id).isEmpty() ? ResponseEntity.ok(lr.getOne(id)) : ResponseEntity.notFound().build();
	}

	@ApiOperation("Alterar lançamento pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@PutMapping("/{id}")
	public ResponseEntity<Lancamento> atualizar(@ApiParam(value = "Id de um lançamento", example = "1")@PathVariable Long id,@ApiParam(name = "Corpo",value = "Representação de um lançamento") @Validated @RequestBody Lancamento lancamento) {
		return ResponseEntity.ok(ls.atualizar(id, lancamento));
	}

	@ApiOperation("Remover lançamentos pelo seu ID")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true,
			allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@ApiParam(value = "Id de um lançamento", example = "1")@PathVariable Long id) {
		lr.deleteById(id);
	}
}
