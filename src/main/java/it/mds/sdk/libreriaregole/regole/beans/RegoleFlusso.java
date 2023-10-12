package it.mds.sdk.libreriaregole.regole.beans;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@XmlRootElement(name ="flusso")
@Value
@NoArgsConstructor(force = true)
public class RegoleFlusso {

	@Builder(setterPrefix = "with")
	public RegoleFlusso(List<Campo> campi) {
		this.campi = campi;
	}
	@XmlElement(name="campo")
	private  List<Campo> campi;

}
