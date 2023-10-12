package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro")
public class RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro extends RegolaDominioValoriAnagraficaConParametro {

	public RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * Se nomeCampo è valorizzato allora controlla che il valore dei parametri in listaCampiDaConcatenare sia presente nell'anagrafica di riferimento
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
		try {
			String valoreCampoInput = (String)recordDtoGenerico.getCampo(nomeCampo);
			if(valoreCampoInput==null || valoreCampoInput.isEmpty()){
				return  Collections.singletonList(creaEsitoOk(nomeCampo));
			}

			return super.valida(nomeCampo,recordDtoGenerico);

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}

	}


}
