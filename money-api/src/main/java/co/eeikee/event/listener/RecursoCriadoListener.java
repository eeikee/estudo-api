package co.eeikee.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import co.eeikee.event.RecursoCriadoEvent;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent>{

	@Override
	public void onApplicationEvent(RecursoCriadoEvent event) {
		HttpServletResponse response = event.getHsr();
		Long id = event.getId();
		
		adicionarHeaderLocation(id);
	}

	private void adicionarHeaderLocation(Long id) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").
				buildAndExpand(id).toUri();
	}

}
