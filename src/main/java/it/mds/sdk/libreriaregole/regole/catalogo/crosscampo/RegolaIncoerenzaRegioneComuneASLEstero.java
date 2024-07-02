/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaIncoerenzaRegioneComuneASLEstero")
public class RegolaIncoerenzaRegioneComuneASLEstero extends RegolaGenerica {

    public RegolaIncoerenzaRegioneComuneASLEstero(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Il codice è valorizzato e diverso da 999 e nel dominio e il codice  Comune di residenza è non nullo e valorizzato con 999999 e/o
     * oppure sono non nulli e valorizzati con valori che non afferiscono alla regione di residenza.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            int annoRiferimento = Integer.parseInt(recordDtoGenerico.getCampiInput().getAnnoRiferimentoInput());
            Object codiceRegioneResidenza = recordDtoGenerico.getCampo(nomeCampo);
            Object codiceComuneResidenza = recordDtoGenerico.getCampo("codiceComuneResidenza");

            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
            String obbligatorio = this.getParametri().getParametriMap().get("obbligatorio");

            // Se il campo non è obbligatorio e il codiceComuneResidenza è nullo, l'esito sarà OK
            if ("N".equalsIgnoreCase(obbligatorio) && codiceComuneResidenza == null) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

            if ("999".equals(String.valueOf(codiceRegioneResidenza)) &&
                    "999999".equals(String.valueOf(codiceComuneResidenza))) {
                return List.of(creaEsitoOk(nomeCampo));
            } else {
                String campiConcatenati = codiceComuneResidenza + "#" + codiceRegioneResidenza;
                GestoreAnagrafica gestoreAnagrafica = getGestoreAnagrafica();
                List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella, campiConcatenati, false).getRecordsAnagrafica();

                log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
                List<String> listaDominio = listaValori
                        .stream()
                        .filter(ra -> (ra.getValidoDa() != null && ra.getValidoDa().getYear() <= annoRiferimento &&
                                        ra.getValidoA() != null && ra.getValidoA().getYear() >= annoRiferimento
                                )
                        )
                        .map(RecordAnagrafica::getDato)
                        .collect(Collectors.toList());
                log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

                if (listaDominio.contains(campiConcatenati)) {
                    return List.of(creaEsitoOk(nomeCampo));
                } else {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            }
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
