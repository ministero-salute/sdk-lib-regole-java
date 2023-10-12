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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioAnagraficaSampDateValido")
public class RegolaDominioAnagraficaSampDateValido extends RegolaGenerica {

    public RegolaDominioAnagraficaSampDateValido(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo in input "nomeCampo"  sia contenuto in un dominio di valori di anagrafica
     * e che SampDate sia incluso tra le 2 date di validità dell'anagrafica
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
            // campoDaValidare
            Object campoDaValidare =  recordDtoGenerico.getCampo(nomeCampo);
            // Tabella Anagrafica
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            // sampDate (campo condizionante e obbligatorio)
            String sampDate = (String) recordDtoGenerico.getCampo("sampDate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate sampDateLocalDateTime = LocalDate.parse(sampDate, formatter);



            if (campoDaValidare == null) {
                return List.of(creaEsitoOk(nomeCampo));
            } else {
                GestoreAnagrafica gestoreAnagrafica = getGestoreAnagrafica();
                List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella, String.valueOf(campoDaValidare), false).getRecordsAnagrafica();

                // Filtro il dominio dei valori validi
                log.trace("RegolaDominioAnagraficaSampDateValido inizio stream filter : " + System.nanoTime());
                List<RecordAnagrafica> listaDominio = listaValori
                        .stream()
                        .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                                || (ra.getValidoDa() == null && ra.getValidoA() == null))
                        .collect(Collectors.toList());
                log.trace("RegolaDominioAnagraficaSampDateValido fine stream filter : " + System.nanoTime());

                Optional<RecordAnagrafica> elementoDaValidare = listaDominio.stream().filter(e -> e.getDato().contains(String.valueOf(campoDaValidare))).findFirst();

                if (elementoDaValidare.isPresent() && sampDateLocalDateTime.isAfter(elementoDaValidare.get().getValidoDa().toLocalDate()) &&
                        sampDateLocalDateTime.isBefore(elementoDaValidare.get().getValidoA().toLocalDate())) {
                    return List.of(creaEsitoOk(nomeCampo));
                } else {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaDominioAnagraficaSampDateValido per il campo " + nomeCampo);
        }

    }

    public GestoreAnagrafica getGestoreAnagrafica() {
        return  new GestoreAnagrafica();
    }


}
