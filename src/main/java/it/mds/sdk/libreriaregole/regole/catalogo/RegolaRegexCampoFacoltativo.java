/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaRegexCampoFacoltativo")
public class RegolaRegexCampoFacoltativo extends RegolaGenerica {

	public RegolaRegexCampoFacoltativo(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * verifica che campo in ingresso corrisponda alla regular expression passata
	 * nel parametro regex
	 *
	 * Ad esempio se necessario verificare se tutte le cifre sono numeri (0-9) o no,
	 * la regex potrebbe essere ^[0-9]+$
	 *
	 * @param nomeCampo         campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {
			String regularExpression = this.getParametri().getParametriMap().get("regex");
			String valoreDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

			if (StringUtils.isNotBlank(valoreDaValidare)) {

				Pattern pattern = Pattern.compile(regularExpression);
				Matcher matcher = pattern.matcher(valoreDaValidare);
				if (matcher.matches()) {
					listaEsiti.add(creaEsitoOk(nomeCampo));
				} else {
					listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
					// .withDescrizione("Il formato del campo " + nomeCampo + " è errato ")
				}

			} else {
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassCastException e) {
			log.error("Impossibile validare Regular Expression del campo  " + nomeCampo, e);
			throw new ValidazioneImpossibileException(
					"Impossibile validare Regular Expression del campo  " + nomeCampo);
		}
		return listaEsiti;
	}

}
