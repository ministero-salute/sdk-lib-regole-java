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
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaCampoUguale")
public class RegolaDominioValoreAnagraficaCampoUguale extends RegolaGenerica {

    public RegolaDominioValoreAnagraficaCampoUguale(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }
    /**
     Se:
     -	il valore del campo PARAMCODE del tracciato di input è contenuto
     all’interno della colonna CODE della query Anagrafica PARAM2 (paragrafo 1.5)
     Allora:
     -	 il valore del campo EVALLIMITTYPE deve essere:	W005A

     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            //paramCode
            String valoreCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("valoreCondizionante"));
            //evallowlimit
            String campoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);
            //w005a
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante");

            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
            List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella,false).getRecordsAnagrafica();

            // listaDominio = list code
            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa()!= null && ra.getValidoA()!=null &&
                            (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa()==null && ra.getValidoA()==null)
                    )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            if(listaDominio.contains(valoreCondizionante) && !parametroCondizionante.equals(campoDaValidare)){
                return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }

}
