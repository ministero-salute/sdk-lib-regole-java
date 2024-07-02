/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDR072")
public class RegolaDR072 extends RegolaGenerica{

	public RegolaDR072(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * "DR072_N05_VerificaAnagrafeActTakenCode:
	 * se sampIdoneo = Y e ACTTAKENCODE è valorizzato
	 * allora:
	 * Se Il campo ACTTAKENCODE viene splittato dal '$', per ogni risultato dello split deve essere splittato per '_'.
	 * per ogni elemento dell'array di stringhe splittato per '_', ci sono due possibilità:
	 *
	 * lunghezza <= 2 --> verificare il valore nell'anagrafica ACTION;
	 * lunghezza >=  2 verificare la prima parte (i primi due caratteri) in anagrafica ACTION e la seconda in anagrafica UNIT"
	 *
	 * A1_2_JDDJK993$DR4FFFFF1 esempio di stringa che matcha con la regex di acctakencode nell'xsd di SALM
	 *
	 *
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

		try {
			String acttakencode = (String) recordDtoGenerico.getCampo(nomeCampo);
			String nomeTabellaAction = this.getParametri().getParametriMap().get("nomeTabellaAction");
			String nomeTabellaUnit = this.getParametri().getParametriMap().get("nomeTabellaUnit");
			String sampidoneo = (String) recordDtoGenerico.getCampo("sampidoneo");

			log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

			log.debug("if acttakencode is null or sampidoneo is not Y -> OK");

			if(Objects.isNull(acttakencode) || !Objects.equals(sampidoneo, "Y")){
				log.debug("{}.valida() - check acttakenCode{} - sampidoneo{} - OK", this.getClass().getName(), acttakencode, sampidoneo);
				return List.of(creaEsitoOk(nomeCampo));
			}

			log.debug("{}.valida() - acttakenCode{} - VALIDATION", this.getClass().getName(), acttakencode);
			String[] acttakencodeArray = acttakencode.split("\\$");

			if(!isActtakencodeVerified(acttakencodeArray, nomeTabellaAction, nomeTabellaUnit)){
				return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
			}
			return List.of(creaEsitoOk(nomeCampo));

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedRegistryException | RegistryNotFoundException e) {
			log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}

	}

	private boolean isActtakencodeVerified(String[] acttakencodeArray, String nomeTabellaAction, String nomeTabellaUnit) throws MalformedRegistryException, RegistryNotFoundException {

		log.debug("VERIFICA ACTTAKENCODE - BEGIN");
		List<Boolean> verificaList = new ArrayList<>();

		List<String> listaAction = richiediListaAnagrafica(nomeTabellaAction);
		List<String> listaUnit = richiediListaAnagrafica(nomeTabellaUnit);

		for( String s : acttakencodeArray )
		{
			for( String s2p : s.split("_") )
			{
				checkActtakncode(verificaList, listaAction, listaUnit, s2p);
			}
		}
		log.debug("VERIFICA ACTTAKENCODE - END");
		return !verificaList.contains(Boolean.FALSE);

	}

	private void checkActtakncode(List<Boolean> verificaList, List<String> listaAction, List<String> listaUnit,
			String s2p) {
		if(s2p.length() <= 2){
			verificaList.add(listaAction.contains(s2p) ? Boolean.TRUE : Boolean.FALSE);
		}else{
			verificaList.add(listaAction.contains(s2p.substring(0,2)) ? Boolean.TRUE : Boolean.FALSE);
			verificaList.add(listaUnit.contains(s2p.substring(2)) ? Boolean.TRUE : Boolean.FALSE);
		}
	}

	private List<String> richiediListaAnagrafica(String nomeTabella) throws MalformedRegistryException, RegistryNotFoundException {

		log.debug("{}.richiediAnagrafica() - Richiesta Anagrafica per la tabella {}", this.getClass().getName(), nomeTabella);

		GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
		List<RecordAnagrafica> listaValori =
				gestoreAnagrafica.richiediAnagrafica(nomeTabella, false).getRecordsAnagrafica();

		return listaValori
				.stream()
				.filter(ra -> (ra.getValidoDa()!= null && ra.getValidoA()!=null &&
						(ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0) ||
						(ra.getValidoDa()==null && ra.getValidoA()==null)
				)
				.map(RecordAnagrafica::getDato)
				.collect(Collectors.toList());
	}



}
