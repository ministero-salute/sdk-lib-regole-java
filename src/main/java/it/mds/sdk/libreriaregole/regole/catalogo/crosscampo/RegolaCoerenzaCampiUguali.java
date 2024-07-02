/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.util.Locale;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCoerenzaCampiUguali")
public class RegolaCoerenzaCampiUguali extends RegolaGenerica{

	public RegolaCoerenzaCampiUguali(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * Se un campo ha un determinato valore, allora un altro campo non può essere uguale a un valore non definito.
	 *
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

		try {
			String valoreCampo = this.getParametri().getParametriMap().get("valoreCampo");

			String nomeCampoConfronto =  this.getParametri().getParametriMap().get("campoDipendente");
			String valoreCampoDipendente = String.valueOf(recordDtoGenerico.getCampo(nomeCampoConfronto));
			String valoreConfronto = this.getParametri().getParametriMap().get("valoreDipendente");

			Object campoDaValidare = recordDtoGenerico.getCampo(nomeCampo);

			if(Objects.equals(valoreCampo.toLowerCase(),String.valueOf(campoDaValidare).toLowerCase()) &&
					!Objects.equals(valoreCampoDipendente.toLowerCase(), valoreConfronto.toLowerCase()))
			{
				return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
			}
			return List.of(creaEsitoOk(nomeCampo));

		} catch (InvocationTargetException | NoSuchMethodException |IllegalAccessException  e) {
			log.error("Impossibile validare la regola diversitaValoreCrossCampo per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola diversitaValoreCrossCampo per il campo "  + nomeCampo );
		}
	}

}
