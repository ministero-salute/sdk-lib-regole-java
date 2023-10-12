package it.mds.sdk.libreriaregole.regole.beans;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;


public class Campo {
	@XmlElement(name="regola")
	private List<RegolaGenerica> regole;
	@XmlAttribute(name="nomecampo")
	private String nomeCampo;

	public Campo() {
		super();
	}

	public Campo(List<RegolaGenerica> regole) {
		super();
		this.regole = regole;
	}

	public Campo(List<RegolaGenerica> regole, String nomeCampo) {
		super();
		this.regole = regole;
		this.setNomeCampo(nomeCampo);
	}

	public List<RegolaGenerica> getRegole() {
		return regole;
	}

	public void setRegole(List<RegolaGenerica> regole) {
		this.regole = regole;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}
	
}
