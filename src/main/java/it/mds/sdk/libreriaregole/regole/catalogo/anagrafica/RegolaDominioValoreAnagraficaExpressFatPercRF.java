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
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaExpressPercFatPercRF")
public class RegolaDominioValoreAnagraficaExpressFatPercRF extends RegolaGenerica {


    public RegolaDominioValoreAnagraficaExpressFatPercRF(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }


    /**
     * Controlla che il valore del campo in input "nomeCampo"  sia obbligatorio
     * PARAMCODE è contenuto all’interno della colonna TERM_CODE della query Anagrafica PARAM PARENT (paragrafo 1.21)
     * e in corrispondenza di tale record (della query)
     * sono presente i valori RF-00000001-PPP oppure RF-00011520-PAR nel campo MASTER della query Anagrafica PARAM PARENT
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            String paramCode = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("paramCode"));
            String sampMatCodeBuilding = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("sampMatCodeBuilding"));

            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);


            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();

            List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella, false).getRecordsAnagrafica();

            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            var termCode = listaDominio.stream().filter(e -> e.split("#")[1].equals(paramCode)).findFirst();

            List<String> RF_PARAM_LIST = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessiRF").split("\\|")).collect(Collectors.toList());

            List<String> A_PARAM_LIST = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessiA").split("\\|")).collect(Collectors.toList());

			if (termCode.isPresent() && RF_PARAM_LIST.contains(termCode.get().split("#")[0])
					&& sampMatCodeBuilding.length() > 4 && A_PARAM_LIST.contains(sampMatCodeBuilding.substring(0, 5))
					&& campoDaValidare == null) {
				return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
			}
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }
}
