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

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaOmissioneCrossCampo")
public class RegolaOmissioneCrossCampo
		extends RegolaGenerica{

	public RegolaOmissioneCrossCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * Il campo in ingresso deve essere omesso (non presente) se un campo del DTO "campoDipendente"
	 * assume un certo valore "valoreDipendente"
	 *
	 * Es. Se "modalitaDiTrasmissione" vale TV allora il campo in ingresso "dataInvio" deve essere nullo
	 *
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();

		try {

			String nomeCampoConfronto = this.getParametri().getParametriMap().get("campoDipendente");
			String valoreCampoDipendente = String.valueOf(recordDtoGenerico.getCampo(nomeCampoConfronto));
			String valoreConfronto = this.getParametri().getParametriMap().get("valoreDipendente");
			
			if(valoreCampoDipendente!= null && valoreCampoDipendente.equals(valoreConfronto)){
				if (recordDtoGenerico.getCampo(nomeCampo)!=null) {
					listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
							//.withDescrizione("Se il campo : " + nomeCampoConfronto + " vale " + valoreConfronto +
							//		" il campo  " + nomeCampo + " deve essere nullo ")
				} else {
					listaEsiti.add(creaEsitoOk(nomeCampo));

				}
			}else{
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}

		} catch (InvocationTargetException | NoSuchMethodException |IllegalAccessException  e) {
			throw new ValidazioneImpossibileException("Impossibile validare la regola diversitaValoreCrossCampo per il campo "  + nomeCampo );
		}
		return listaEsiti;
	}

}
