package it.mds.sdk.libreriaregole.regole.diretta;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioCampoUgualeValoreAnagraficaMeseAnno")
public class RegolaDominioCampoUgualeValoreAnagraficaMeseAnno extends RegolaGenerica {

    public RegolaDominioCampoUgualeValoreAnagraficaMeseAnno(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Se il campoCondizionante = parametro condizionante allora il nomeCampo(campo da validare) deve essere nell'anagrafica nomeTabella
     * con data inizio validita e data fine validita compresa nel periodo-Anno
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            if (campoDaValidare == null || campoDaValidare.isEmpty()) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }

            String campoCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante"));
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante");

            if (!parametroCondizionante.equals(campoCondizionante)) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
            String mese = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("mese"));
            String anno = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("anno"));
            LocalDateTime dataConfrontoAnagrafica = LocalDateTime.of(LocalDate.of(Integer.parseInt(anno), Month.of(Integer.parseInt(mese)), 1), LocalTime.of(00, 00, 00));

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
            List<RecordAnagrafica> listaValori =
                    gestoreAnagrafica.richiediAnagrafica(nomeTabella, campoDaValidare, false).getRecordsAnagrafica();

            // listaDominio = list code
            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null &&
                            (ra.getValidoDa().compareTo(dataConfrontoAnagrafica) * dataConfrontoAnagrafica.compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null)
                    )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            if (!listaDominio.contains(campoDaValidare)) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }
}
