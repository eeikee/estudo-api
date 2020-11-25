package co.eeikee.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoLancamento {
RECEITA("Receita"),DESPESA("Despesa");
	
	private String descricao;

	@JsonValue
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	private TipoLancamento(String descricao) {
		this.descricao = descricao;
	}
	
	public static String comparar(String descricao)
	{
	    for(TipoLancamento tipo : TipoLancamento.values()) {
	    	 if(tipo.getDescricao().equals(descricao)){
	    		  return tipo.getDescricao();
	    	 }       
	    }
	    return null;
	}
}


