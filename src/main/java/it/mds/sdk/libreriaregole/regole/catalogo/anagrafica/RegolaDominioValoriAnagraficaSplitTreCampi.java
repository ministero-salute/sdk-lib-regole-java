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
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaSplitTreCampi")
public class RegolaDominioValoriAnagraficaSplitTreCampi extends RegolaGenerica{

	public RegolaDominioValoriAnagraficaSplitTreCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * Se il valore del campo PROGID del tracciato di input è uguale alla stringa ADD allora:
	 *
	 * -	i valori dei campi PARAMCODE, SAMPMATCODE_LEGIS e EVALLIMITTYPE del tracciato di input devono essere presenti sullo stesso record
	 * 	e rispettivamente sulle colonne PARAMCODE, SAMPMATCODE_LEGIS e EVALLIMITTYPE della query Anagrafica TAB_FSA_LEGIS_PARAM_EVALLIMIT (paragrafo 1.31)
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		try {
			String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
			String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

			String paramcode = (String) recordDtoGenerico.getCampo("paramcode");
			String sampmatcodeLegis = (String) recordDtoGenerico.getCampo("sampmatcodeLegis");
			String evallimittype = (String) recordDtoGenerico.getCampo("evallimittype");

			GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
			List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella,campoDaValidare, false).getRecordsAnagrafica();

			//recupero il dominio dei valori validi
			List<String> listaDominio;
			log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
			listaDominio =listaValori
					  .stream()
					  .filter(ra -> (ra.getValidoDa()!= null && ra.getValidoA()!=null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
							  		|| (ra.getValidoDa()==null && ra.getValidoA()==null) )
					  .map(RecordAnagrafica::getDato)
					  .collect(Collectors.toList());
			log.trace("regola dominio valori anagrafica fine stream filter : " + System.nanoTime());

			String comboCampi = paramcode + "#" + sampmatcodeLegis + "#" + evallimittype;

			if(Objects.equals("ADD", campoDaValidare) && !listaDominio.contains(comboCampi)){
				log.debug("{}.valida - la lista dominio non contiene la combinazione paramcode#sampmatcodeLegis#evallimittype - listaDominio{} - comboCampi{}",
						this.getClass().getName(),
						String.join("|", listaDominio),
						comboCampi
				);

				return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
			}
			return List.of(creaEsitoOk(nomeCampo));

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedRegistryException | RegistryNotFoundException e) {
			log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}

	}


}
