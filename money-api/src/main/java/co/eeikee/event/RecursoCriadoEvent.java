package co.eeikee.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

public class RecursoCriadoEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HttpServletResponse hsr;
	private Long id;

	public RecursoCriadoEvent(Object source, HttpServletResponse hsr, Long id) {
		super(source);
		this.hsr = hsr;
		this.id = id;
	}

	public HttpServletResponse getHsr() {
		return hsr;
	}

	public Long getId() {
		return id;
	}

}
