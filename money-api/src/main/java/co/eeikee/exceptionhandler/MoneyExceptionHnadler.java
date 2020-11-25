package co.eeikee.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MoneyExceptionHnadler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String msgUser = messageSource.getMessage("mensagem.invalida",null,LocaleContextHolder.getLocale());
		String msgDev = ex.getCause() != null ? ex.getClass().toString() : ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(msgUser,msgDev));
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Erro> erros = criarListaDeErro(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleEmptyResultDataAccessException(RuntimeException rx, WebRequest request) {
		String msgUser = messageSource.getMessage("recurso.nao-encontrado",null,LocaleContextHolder.getLocale());
		String msgDev = rx.toString();
		List<Erro> erros = Arrays.asList(new Erro(msgUser,msgDev));
		return handleExceptionInternal(rx, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	private List<Erro> criarListaDeErro(BindingResult br){
		List<Erro> erros = new ArrayList<>();
		for(FieldError fe: br.getFieldErrors()) {
		String msgUser = messageSource.getMessage(fe, LocaleContextHolder.getLocale());
		String msgDev = fe.toString();
		erros.add(new Erro(msgUser, msgDev));
		}
		return erros;
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> HandleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest wr){
		String msgUser = messageSource.getMessage("recurso.operacao-nao-permitida",null,LocaleContextHolder.getLocale());
		String msgDev = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(msgUser,msgDev));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, wr);
	}
	
	public static class Erro {
		private String msgDev;
		public String getMsgDev() {
			return msgDev;
		}
		public Erro(String msgUser, String msgDev) {
			super();
			this.msgDev = msgDev;
			this.msgUser = msgUser;
		}
		public void setMsgDev(String msgDev) {
			this.msgDev = msgDev;
		}
		public String getMsgUser() {
			return msgUser;
		}
		public void setMsgUser(String msgUser) {
			this.msgUser = msgUser;
		}
		private String msgUser;
	}
}
