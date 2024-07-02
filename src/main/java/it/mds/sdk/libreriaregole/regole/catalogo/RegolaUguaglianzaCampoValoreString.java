/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

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
@XmlDiscriminatorValue("regolaUguaglianzaCampoValoreString")
public class RegolaUguaglianzaCampoValoreString extends RegolaGenerica{


	public RegolaUguaglianzaCampoValoreString(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 **
	 * Verifica che la stringa in ingresso sia uguale ad valore prefissato.
	 * Si assume che il campo in ingresso sia valorizzato, non null
	 *
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */

	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {


			String daValidare = (String)recordDtoGenerico.getCampo(nomeCampo);
			String valoreConfronto = this.getParametri().getParametriMap().get("valoreConfronto");

			if(valoreConfronto.equals(daValidare)){
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}else{
				listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
					//	.withDescrizione("Il campo : " + nomeCampo + " e il campo : " + valoreConfronto + " non sono congruenti")

			}
		} catch (IllegalAccessException| NoSuchMethodException| InvocationTargetException e) {
			log.error("Impossibile validare la regola Uguaglianza Campo Valore del campo " + nomeCampo, e);
			throw new ValidazioneImpossibileException("Impossibile validare la regola Uguaglianza Campo Valore del campo  " + nomeCampo);
		}
		return listaEsiti;
	}

}
