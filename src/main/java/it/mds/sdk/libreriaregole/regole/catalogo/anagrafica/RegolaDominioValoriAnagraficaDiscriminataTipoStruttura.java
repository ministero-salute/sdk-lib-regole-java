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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaDiscriminataTipoStruttura")
public class RegolaDominioValoriAnagraficaDiscriminataTipoStruttura extends RegolaGenerica {

    public RegolaDominioValoriAnagraficaDiscriminataTipoStruttura(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Validazione del nomeCampo sulla base di un parametro condizionante
     * Se il parametro condizionante è 1 vanno effettuate delle logiche
     * altrimenti non bisogna effettuare logiche e tornare ok.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
            String tipoStruttura = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("parametroCondizionante"));

            if(!Objects.equals(tipoStruttura, "01") || Objects.isNull(campoDaValidare) || campoDaValidare.isBlank() || Objects.equals("0000", campoDaValidare)){
                log.debug("tipoStruttura {} - campoDaValidare {}", tipoStruttura, campoDaValidare);
                return List.of(creaEsitoOk(nomeCampo));
            }

            String first2CharTipoUnOp = campoDaValidare.substring(0, 2);

            List<RecordAnagrafica> listaValori =
                    getGestoreAnagrafica().richiediAnagrafica(nomeTabella, first2CharTipoUnOp, false).getRecordsAnagrafica();
            //recupero il dominio dei valori validi

            log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
            List<String> listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null &&
                            (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0) ||
                            (ra.getValidoDa() == null && ra.getValidoA() == null)
                    )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());
            log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

            // se non viene trovato in lista o gli ultimi 2 char non sono un numero, allora KO.
            if (listaDominio.isEmpty() || !isLastTwoCharA2NumberDigit(campoDaValidare)) {
                log.debug("tipoUnitaOperativa non trovato nell'anagrafica di riferimento o ultimi 2 caratteri non numerici.");
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }

    }

    private boolean isLastTwoCharA2NumberDigit(String campoDaValidare) {
        int campoDaValidareLength = campoDaValidare.length();
        int lastTwoDigit = Integer.parseInt(campoDaValidare.substring(campoDaValidareLength - 2));
        log.debug("tipoUnitaOperativa last 2 char {} ", lastTwoDigit);
        return lastTwoDigit >=0 && lastTwoDigit <=99;
    }

    public GestoreAnagrafica getGestoreAnagrafica() {
        return new GestoreAnagrafica();
    }
}
