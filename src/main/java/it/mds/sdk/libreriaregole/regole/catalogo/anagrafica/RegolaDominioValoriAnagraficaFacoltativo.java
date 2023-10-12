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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaFacoltativo")
public class RegolaDominioValoriAnagraficaFacoltativo extends RegolaGenerica {

	public RegolaDominioValoriAnagraficaFacoltativo(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * Controlla che il valore del campo in input "nomeCampo"  sia contenuto in un dominio di valori di anagrafica
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {			
			String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

			ArrayList<Object> listaValoriCampoDaValidare = new ArrayList<Object>();
        	
        	if (recordDtoGenerico.getCampo(nomeCampo) instanceof List) {
        		listaValoriCampoDaValidare = (ArrayList<Object>) recordDtoGenerico.getCampo(nomeCampo);
        	} else if(recordDtoGenerico.getCampo(nomeCampo) instanceof String) {
        		String valoreCampo = (String) recordDtoGenerico.getCampo(nomeCampo);
        		listaValoriCampoDaValidare.add(valoreCampo);
        	}
			
        	ArrayList<Object>lstValoriDaValidare1 = new ArrayList<Object>();			
			lstValoriDaValidare1.addAll(listaValoriCampoDaValidare);
			
			GestoreAnagrafica gestoreAnagrafica = getGestoreAnagrafica();
            List<RecordAnagrafica> listaValori = new ArrayList<>();
            for (Object campoDaValidare : lstValoriDaValidare1) {
                String campoDaValidareString = (String) campoDaValidare;
                List<RecordAnagrafica> listaSingoloValore = gestoreAnagrafica.richiediAnagrafica(nomeTabella,
                        campoDaValidareString, false).getRecordsAnagrafica();
                listaValori.addAll(listaSingoloValore);
            }
			//recupero il dominio dei valori validi
			List<String> listaDominio;
			
			listaDominio =listaValori
					  .stream()
					  .filter(ra -> (ra.getValidoDa()!= null && ra.getValidoA()!=null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
							  		|| (ra.getValidoDa()==null && ra.getValidoA()==null) )
					  .map(RecordAnagrafica::getDato)
					  .collect(Collectors.toList());
			if (listaValoriCampoDaValidare != null && listaValoriCampoDaValidare.size() > 0) {
				if (lstContains(lstValoriDaValidare1, listaDominio)) {				
					listaEsiti.add(creaEsitoOk(nomeCampo));
				}else{
					listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
				}
			}else
				listaEsiti.add(creaEsitoOk(nomeCampo));
			
			
		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedRegistryException | RegistryNotFoundException e) {
			log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}
		return listaEsiti;

	}

	public GestoreAnagrafica getGestoreAnagrafica() {
		return new GestoreAnagrafica();
	}

	private boolean lstContains(ArrayList<Object> lstDaControllare, List<String> lstAnagrafica) {
		boolean ret = false;
		lstDaControllare.removeAll(lstAnagrafica);			
		
		if (lstDaControllare.size() == 0)
			ret = true;
		
		return ret;
	}


}
