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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCampoSplittatoDominioValoreAnagrafica")
public class RegolaCampoSplittatoDominioValoreAnagrafica extends RegolaGenerica {

    public RegolaCampoSplittatoDominioValoreAnagrafica(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se SAMPMATCODE_INGRED è valorizzato dopo aver effettuato uno split sul carattere
     * $ ogni valore ottenuto deve essere presente nella colonna TERMCODE della query
     * con la condizione SAMP_DATE incluso tra le due date di validità
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] -  BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            // SAMPMATCODE_INGRED
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            if(campoDaValidare == null){
                log.debug("nomeCampo[{}] è null", nomeCampo);
                return List.of(creaEsitoOk(nomeCampo));
            }
            List<String> elementiCampoDaValidare = Arrays.stream(
                    campoDaValidare.split("\\$")).collect(Collectors.toList()
            );

            String sampDate = (String) recordDtoGenerico.getCampo("sampDate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate sampDateLocalDate = LocalDate.parse(sampDate, formatter);

            // Tabella Anagrafica
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            GestoreAnagrafica gestoreAnagrafica = getGestoreAnagrafica();
            List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella, false).getRecordsAnagrafica();

            // Filtro il dominio dei valori validi
            log.trace("RegolaDominioAnagraficaSampDateValido inizio stream filter : " + System.nanoTime());

            List<RecordAnagrafica> listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .collect(Collectors.toList());

            var recordMatch = listaDominio.stream().filter(
                    e->isRecordContieneTuttiGliSplitCampoDaValidare(e, elementiCampoDaValidare).isPresent()
            ).findFirst();

            if(
                    (recordMatch.isPresent() &&
                    recordMatch.get().getValidoDa().toLocalDate().isBefore(sampDateLocalDate) &&
                    recordMatch.get().getValidoA().toLocalDate().isAfter(sampDateLocalDate))
            ){
                return List.of(creaEsitoOk(nomeCampo));
            }
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaDominioAnagraficaSampDateValido per il campo " + nomeCampo);
        }

    }

    public GestoreAnagrafica getGestoreAnagrafica() {
       return new GestoreAnagrafica();
    }

    private Optional<RecordAnagrafica> isRecordContieneTuttiGliSplitCampoDaValidare(RecordAnagrafica recordAnagrafica, List<String> elementiCampoDaValidare)
    {
        for(String elemento: elementiCampoDaValidare){
            if(!recordAnagrafica.getDato().contains(elemento)){
                return Optional.empty();
            }
        }
        return Optional.of(recordAnagrafica);
    }

}

