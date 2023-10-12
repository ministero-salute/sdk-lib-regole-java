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
@XmlDiscriminatorValue("regolaUguaglianzaCrossCampo")
public class RegolaUguaglianzaCrossCampo extends RegolaGenerica{

	public RegolaUguaglianzaCrossCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * verifica che il valore del campo in input sia uguale al valore di un altro campo del DTO.
	 * si assume che, sia il campo in input e che l'altro campo del DTO, siano valorizzati (non null)
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {
			String daValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
			String validatore = this.getParametri().getParametriMap().get("campoValidatore");
			String comparante = String.valueOf(recordDtoGenerico.getCampo(validatore));
			if(daValidare.equals(comparante)){
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}else{
				listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
						//.withDescrizione("Il campo : " + nomeCampo + " e campo : " + validatore + " non congruenti")
			}
		

		} catch (InvocationTargetException | NoSuchMethodException |IllegalAccessException  e) {
			log.error("Impossibile validare la regola UguaglianzaCrossCampo per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola UguaglianzaCrossCampo per il campo " + nomeCampo );
		}
		return listaEsiti;
	}



}
