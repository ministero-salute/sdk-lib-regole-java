package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@XmlDiscriminatorValue("regolaListaValoriAmmessiCampoFacoltativo")
@NoArgsConstructor
public class RegolaListaValoriAmmessiCampoFacoltativo extends RegolaGenerica {

	public RegolaListaValoriAmmessiCampoFacoltativo(String nome, String codErrore, String desErrore,
			Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * verifica che il valore del campo in ingresso sia all'interno di un dominio
	 * predefinito (parametro listaValoriAmmessi)
	 * 
	 * @param nomeCampo         campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {
			
			String valoreDaValidare = String.valueOf((recordDtoGenerico.getCampo(nomeCampo)));
			if (StringUtils.isNotBlank(valoreDaValidare) && !"null".equals(valoreDaValidare)) {

				List<String> listaValori = Arrays
						.stream(this.getParametri().getParametriMap().get("listaValoriAmmessi").split("\\|"))
						.collect(Collectors.toList());
				if (listaValori.contains(valoreDaValidare)) {
					listaEsiti.add(creaEsitoOk(nomeCampo));
				} else {
					listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
					// .withDescrizione("Valore del campo " + nomeCampo + " non ammesso - i valori
					// ammessi sono" + listaValori.toString())
				}

			} else {
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("Impossibile validare la regola Lista valori ammessi per il campo " + nomeCampo, e);
			throw new ValidazioneImpossibileException(
					"Impossibile validare la regola Lista valori ammessi per il campo " + nomeCampo);
		}
		return listaEsiti;

	}

}
