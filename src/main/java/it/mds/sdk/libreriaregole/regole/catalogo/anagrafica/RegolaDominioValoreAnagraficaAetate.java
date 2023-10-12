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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("RegolaDominioValoreAnagraficaAetate")
public class RegolaDominioValoreAnagraficaAetate extends RegolaGenerica {
    public RegolaDominioValoreAnagraficaAetate(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo in input "nomeCampo"  sia obbligatorio
     * se progId=PNR e se SampMatCodeBuilding è presente all'interno della lista
     * estratta dalla query Anagrafica SAMPMATCODE_TAB12 altrimenti va in KO
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try{
            String progId = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("progId"));
            String sampMatCodeBuilding = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("sampMatCodeBuilding"));
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            String paramPNR = (String) this.getParametri().getParametriMap().get("PNR");

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
            List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella,false).getRecordsAnagrafica();

            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa()!= null && ra.getValidoA()!=null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa()==null && ra.getValidoA()==null) )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            if(progId.equals(paramPNR) && listaDominio.contains(sampMatCodeBuilding) && campoDaValidare == null){
                    return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
    MalformedRegistryException | RegistryNotFoundException e) {
        log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e);
        throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
    }
    }

}
