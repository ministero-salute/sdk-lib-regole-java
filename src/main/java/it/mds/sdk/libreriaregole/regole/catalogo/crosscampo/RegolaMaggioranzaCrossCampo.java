package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaMaggioranzaCrossCampo")
public class RegolaMaggioranzaCrossCampo extends RegolaGenerica{


	public RegolaMaggioranzaCrossCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * Controlla che il valore del campo passato in input sia maggiore del valore di un altro campo all'interno dello stesso dto.
	 * campoValidatore é il parametro che contiene il nome del record del DTO con cui voglio comparare il dato in input
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {
			Integer campoDaValidare = (Integer)recordDtoGenerico.getCampo(nomeCampo);
			String nomeCampoValidatore = this.getParametri().getParametriMap().get("campoValidatore");
			Integer comparante = (Integer)recordDtoGenerico.getCampo(nomeCampoValidatore);

			if(campoDaValidare.compareTo(comparante) > 0){
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}else{
				listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
						//.withDescrizione("Il campo : " + nomeCampo + " deve essere maggiore del campo : " + nomeCampoValidatore )

			}
		} catch (InvocationTargetException | NoSuchMethodException |IllegalAccessException  e) {
			log.error("Impossibile validare la regola MaggioranzaCrossCampo per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola MaggioranzaCrossCampo per il campo " + nomeCampo );
		}
		return listaEsiti;
	}


}
