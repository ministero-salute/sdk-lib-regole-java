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
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagrafica")
public class RegolaDominioValoriAnagrafica extends RegolaGenerica {

    public RegolaDominioValoriAnagrafica(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo in input "nomeCampo"  sia contenuto in un dominio di valori di anagrafica
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

            List<RecordAnagrafica> listaValori =
                    getGestoreAnagrafica().richiediAnagrafica(nomeTabella, campoDaValidare, false).getRecordsAnagrafica();
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

            if (!listaDominio.contains(campoDaValidare)) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }

    }

    public GestoreAnagrafica getGestoreAnagrafica() {
        return new GestoreAnagrafica();
    }
}
