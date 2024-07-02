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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaCCAlpha")
public class RegolaDominioValoreAnagraficaCCAlpha extends RegolaGenerica {

    public RegolaDominioValoreAnagraficaCCAlpha(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }


    /**
     * Vedi par. 27 delle specifiche
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
            String paramCode = (String) recordDtoGenerico.getCampo("paramcode");
            Object campoDaValidareCCAlpha = recordDtoGenerico.getCampo(nomeCampo);

            String anmethtype = (String) recordDtoGenerico.getCampo("anmethtype");
            String paramtype = (String) recordDtoGenerico.getCampo("paramtype");

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();

            List<RecordAnagrafica> listaValoriParamVMPRIT = gestoreAnagrafica.richiediAnagrafica("ANAG_SALM_PARAM_VMPR_IT", false).getRecordsAnagrafica();
            List<RecordAnagrafica> listaValoriParamVMPRITMolecole = gestoreAnagrafica.richiediAnagrafica("ANAG_SALM_PARAM_VMPR_IT", false).getRecordsAnagrafica();
            List<RecordAnagrafica> listaValoriGruppoMolecola = gestoreAnagrafica.richiediAnagrafica("ANAG_SALM_GRUPPO_MOLECOLA", false).getRecordsAnagrafica();

            var listaDominioParamVMPRIT = getListaDominioFromListaAnagrafiche(listaValoriParamVMPRIT);
            var listaDominioParamVMPRITMolecole = getListaDominioFromListaAnagrafiche(listaValoriParamVMPRITMolecole);
            var listaDominioGruppoMolecola = getListaDominioFromListaAnagrafiche(listaValoriGruppoMolecola);

            var idMolecola = listaDominioParamVMPRITMolecole.stream().filter(
                    e -> e.split("#")[0].equals(paramCode)
            ).findFirst();
            if (!"AT08A".equals(anmethtype) && !listaDominioParamVMPRIT.contains(paramCode) && idMolecola.isPresent() && 
            		!listaDominioGruppoMolecola.contains(idMolecola.get().split("#")[1]) && !"P002A".equals(paramtype) 
            		&& campoDaValidareCCAlpha == null){
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }

    public List<String> getListaDominioFromListaAnagrafiche(List<RecordAnagrafica> recordAnagraficaList) {
        return recordAnagraficaList.stream()
                .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                        || (ra.getValidoDa() == null && ra.getValidoA() == null))
                .map(RecordAnagrafica::getDato)
                .collect(Collectors.toList());
    }
}
