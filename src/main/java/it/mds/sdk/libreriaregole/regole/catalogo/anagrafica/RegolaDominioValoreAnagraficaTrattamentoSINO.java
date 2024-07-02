/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaTrattamentoSINO")
public class RegolaDominioValoreAnagraficaTrattamentoSINO extends RegolaGenerica {

	public RegolaDominioValoreAnagraficaTrattamentoSINO(String nome, String codErrore, String desErrore,
			Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 * Controlla che il valore del campo in input "nomeCampo" sia obbligatorio se la
	 * lista dei termCode estratta dalla tabella PARAM_DESCRIPTOR1VET contiene
	 * paramCode e se samppoint contiene uno dei valori MS e se origCounty = IT
	 * 
	 * @param nomeCampo         campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

		try {
			String origCountry = (String) recordDtoGenerico.getCampo("origcountry");
			String paramCode = (String) recordDtoGenerico.getCampo("paramcode");
			String samppoint = (String) recordDtoGenerico.getCampo("samppoint");

			String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

			String trattamentoSINO = (String) recordDtoGenerico.getCampo(nomeCampo);

			List<String> MS_PARAM_LIST = Arrays
					.stream(this.getParametri().getParametriMap().get("listaValoriAmmessi").split("\\|"))
					.collect(Collectors.toList());

			GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
			List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella, false)
					.getRecordsAnagrafica();

			// listaDominio = list termCode
			var listaDominio = listaValori.stream()
					.filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null
							&& (ra.getValidoDa().compareTo(LocalDateTime.now())
									* LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
							|| (ra.getValidoDa() == null && ra.getValidoA() == null))
					.map(RecordAnagrafica::getDato).collect(Collectors.toList());

			if (listaDominio.contains(paramCode) && "IT".equals(origCountry) && MS_PARAM_LIST.contains(samppoint)
					&& trattamentoSINO == null) {
				return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
			}

			return List.of(creaEsitoOk(nomeCampo));

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedRegistryException
				| RegistryNotFoundException e) {
			throw new ValidazioneImpossibileException(
					"Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
		}

	}
}
