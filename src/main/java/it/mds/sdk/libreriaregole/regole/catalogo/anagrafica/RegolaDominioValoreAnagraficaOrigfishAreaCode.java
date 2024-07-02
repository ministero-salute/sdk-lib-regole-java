/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaOrigfishAreaCode")
public class RegolaDominioValoreAnagraficaOrigfishAreaCode extends RegolaGenerica {

    public RegolaDominioValoreAnagraficaOrigfishAreaCode(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo in input "nomeCampo"  sia obbligatorio
     * al verificarsi di determinate regole (vedi par. 29), altrimenti KO.
     * Oppure che se il campo è uguale a PNR, il controllo sia bypassato.
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            String nomeTabellaAnagraficaReport = this.getParametri().getParametriMap().get("nomeTabellaAnagraficaReport");
            String nomeTabellaParamParent = this.getParametri().getParametriMap().get("nomeTabellaParamParent");

            String progId = (String) recordDtoGenerico.getCampo("progid");
            String sampMatCodeBuilding = (String) recordDtoGenerico.getCampo("sampmatcodeBuilding");
            String paramCode = (String) recordDtoGenerico.getCampo("paramcode");


            String origFishAreaCode = (String) recordDtoGenerico.getCampo(nomeCampo);

            List<String> RF_PARAM_LIST = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());



            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();

            List<RecordAnagrafica> listaValoriAnagraficaReport = gestoreAnagrafica.richiediAnagrafica(nomeTabellaAnagraficaReport, false).getRecordsAnagrafica();
            List<RecordAnagrafica> listaValoriParamParent = gestoreAnagrafica.richiediAnagrafica(nomeTabellaParamParent, false).getRecordsAnagrafica();

            // listaDominio = list termCode
            var listaDominioAnagraficaReport = listaValoriAnagraficaReport
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            var listaDominioParamParent = listaValoriParamParent
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            String sampMatCodeBuildingTrimmed = sampMatCodeBuilding.length() < 5 ? sampMatCodeBuilding : sampMatCodeBuilding.substring(0, 5);

            // prendi l'elemento dalla lista (estratta dalla query anagraficaReport) che all'interno di termCode (sono 2 campi, quindi la parte prima di #) contiene sampMatCode (considerando solo i primi 5 char)
            Optional<String> elemContainsSampMatCodeBuilding = listaDominioAnagraficaReport.stream().filter(el -> el.split("#")[0].contains(sampMatCodeBuildingTrimmed)).findFirst();
            // prendi l'elemento dalla lista (estratta da paramParent) che all'interno di termCode (sono 2 campi, quindi la parte prima di #) contiene paramCode
            Optional<String> elemContainsParamCode = listaDominioParamParent.stream().filter(el -> el.split("#")[0].contains(paramCode)).findFirst();

            if (Objects.equals("PNR", progId)) {
                return List.of(creaEsitoOk(nomeCampo));
            }


            // se l'elemento che contiene il sampMatCodeBuilding passato, composto da (termCode#ReportHierarchyCode) è presente
            //  e contiene nella seconda parte della stringa divisa da # (parte REPORTHIERARCHYCODE )
            // il param z0001 all'inizio (nei primi 5 char) e nel resto i parametri 0012 o 0013
            // e se l'elemento che contiene il paramCode (termCode#master) nella parte master è presente e contiene uno dei parametri RF.
            // allora se origFishArea è null, torna KO.
            if (
                    elemContainsSampMatCodeBuilding.isPresent() &&
                            (elemContainsSampMatCodeBuilding.get().split("#")[1].length() >= 5) &&
                                elemContainsSampMatCodeBuilding.get().split("#")[1].substring(0, 5).contains("Z0001") &&
                                (elemContainsSampMatCodeBuilding.get().split("#")[1].substring(5).contains("0012") ||
                                    elemContainsSampMatCodeBuilding.get().split("#")[1].substring(5).contains("0013")
                            ) &&
                            elemContainsParamCode.isPresent() &&
                            RF_PARAM_LIST.contains(elemContainsParamCode.get().split("#")[1]) &&
                            origFishAreaCode == null) {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }


}
