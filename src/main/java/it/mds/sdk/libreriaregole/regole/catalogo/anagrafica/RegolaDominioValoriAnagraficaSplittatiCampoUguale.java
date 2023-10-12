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
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaSplittatiCampoUguale")
public class RegolaDominioValoriAnagraficaSplittatiCampoUguale extends RegolaGenerica {

    public RegolaDominioValoriAnagraficaSplittatiCampoUguale(
            String nome, String codErrore, String desErrore, Parametri parametri
    ) {
        super(nome, codErrore, desErrore, parametri);
    }
    /**
     Se il valore del campo PROGID del tracciato di input è uguale alla stringa PNR allora:
     -	i valori dei campi SAMPPOINT e SAMPUNITTYPE del tracciato di input
     devono essere presenti sullo stesso record e rispettivamente sulle colonne SAMPPOINT e SAMPUNITTYPE
     della query Anagrafica REL_SAMPPOINT_SAMPUNTY (paragrafo 1.36).

     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            //progId
            String campoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);
            //samppoint
            String valoreCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("valoreCondizionante"));
            //sampunittype
            String valoreCondizionante2 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("valoreCondizionante2"));

            //PNR
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

            // controllo che la lista estratta effettuando la query contenga
            // i valori condizionanti SULLO STESSO RECORD unendoli con # ed eseguendo l'uguaglianza
            // essendo che i valori estratti dalla query
            // saranno passati in un unico campo "dato" splittati da #

            if(parametroCondizionante.equals(campoDaValidare) &&
                    !listaDominio.contains( valoreCondizionante + "#" + valoreCondizionante2)
            ){
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
