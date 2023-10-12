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
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaSplitConParametro")
public class RegolaDominioValoriAnagraficaSplitConParametro extends RegolaGenerica{

	public RegolaDominioValoriAnagraficaSplitConParametro(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * Controlla che il valore del campo in input "nomeCampo"
	 * sia contenuto in un dominio di valori di anagrafica splittato per #
	 * E' PASSATO UN PARAMETRO parametroSplitter CHE INDICA QUALE PARTE DELLO SPLIT CONFRONTARE
	 * CON IL VALORE IN INGRESSO
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {
			String campoDaValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
			String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
			String primoParametro = this.getParametri().getParametriMap().get("primoParametro");
			String valoreprimoParametro = String.valueOf(recordDtoGenerico.getCampo(primoParametro));
			String secondoParametro = this.getParametri().getParametriMap().get("secondoParametro");
			String valoresecondoParametro = String.valueOf(recordDtoGenerico.getCampo(secondoParametro));
			String terzoParametro = this.getParametri().getParametriMap().get("terzoParametro");
			String valoreterzoParametro = String.valueOf(recordDtoGenerico.getCampo(terzoParametro));
			GestoreAnagrafica gestoreAnagrafica = getGestoreAnagrafica();
			String valoreDaValidareConcatenato = valoreprimoParametro + "#" + valoresecondoParametro + "#" + valoreterzoParametro + "#" +  campoDaValidare;
			List<RecordAnagrafica> valoriTabella = gestoreAnagrafica.richiediAnagrafica(nomeTabella, valoreDaValidareConcatenato, false).getRecordsAnagrafica();
			//recupero il dominio dei valori validi
			List<String> listaDominio;
			log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
			listaDominio =valoriTabella
					.stream()
					.filter(ra -> (ra.getValidoDa()!= null && ra.getValidoA()!=null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
							|| (ra.getValidoDa()==null && ra.getValidoA()==null) )
					.map(RecordAnagrafica::getDato)
					.collect(Collectors.toList());
			log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());
			if(listaDominio.contains(valoreDaValidareConcatenato)){
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}else{
				listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
			}
		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedRegistryException | RegistryNotFoundException e) {
			log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}
		return listaEsiti;

	}
	public GestoreAnagrafica getGestoreAnagrafica(){
		return new GestoreAnagrafica();
	}
}
