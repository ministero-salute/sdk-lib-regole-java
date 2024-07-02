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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaBR195")
public class RegolaBR195 extends RegolaGenerica {

    public RegolaBR195(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *Se il valore del campo PROGID del tracciato di input è uguale alla stringa ADD allora:
     *
     * -	i valori dei campi SAMPMATCODE_BUILDING (ottenuto considerando solo i primi 5 caratteri) 
     * e SAMPMATCODE_LEGIS del tracciato di input devono essere presenti sullo stesso record e rispettivamente sulle colonne SAMPMATCODE_BUILDING 
     * e SAMPMATCODE_LEGIS della query Anagrafica TAB_FSA_BUILDING_LEGIS (paragrafo 1.29) 
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{} - nomeCampo[{}] recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico);

        try {
            String progId = (String) recordDtoGenerico.getCampo(nomeCampo);
            String sampmatcodeBuilding = (String) recordDtoGenerico.getCampo("sampmatcodeBuilding");
            String sampmatcodeLegis = (String) recordDtoGenerico.getCampo("sampmatcodeLegis");
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");


            List<RecordAnagrafica> listaValori = getGestoreAnagrafica().richiediAnagrafica(nomeTabella, false).getRecordsAnagrafica();

            // listaDominio = list code
            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null &&
                            (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null)
                    )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());
            
            if(!Objects.equals("ADD", progId)){
                log.debug("{}.valida - progId è uguale ad ADD - OK", this.getClass().getName());
                return List.of(creaEsitoOk(nomeCampo));
            }
            int sampMatCodeBuildingLength = sampmatcodeBuilding.length();
            String sampMatCodeBuildingTrim = sampMatCodeBuildingLength < 5 ? sampmatcodeBuilding.substring(0, sampMatCodeBuildingLength) : sampmatcodeBuilding.substring(0, 5);
            String comboCampi = sampMatCodeBuildingTrim + "#" + sampmatcodeLegis;

            if(!listaDominio.contains(comboCampi)){
                log.debug("{}.valida - la lista dominio non contiene la combo {} - listaDominio{} - KO", this.getClass().getName(), comboCampi, String.join("|", listaDominio));
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedRegistryException | RegistryNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola regolaCampoCondizionatoTreValori per il campo " + nomeCampo);
        }
    }

    public GestoreAnagrafica getGestoreAnagrafica(){
        return new GestoreAnagrafica();
    }
}
