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
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaResloq")
public class RegolaDominioValoreAnagraficaResloq extends RegolaGenerica {

    public RegolaDominioValoreAnagraficaResloq(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *
     * Controlla che il valore del campo in input "nomeCampo"  sia obbligatorio, o soddisfi delle regole, altrimenti va in KO
     * @param nomeCampo campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {

            String progId = (String)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("progId"));
            String paramCode = (String)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("paramCode"));
            String resInfoNotSummed = (String)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("resInfoNotSummed"));
            String accredproc = (String)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("accredproc"));

            Object campoDaValidareResloq = recordDtoGenerico.getCampo(nomeCampo);
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();

            List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella,false).getRecordsAnagrafica();

            // listaDominio = list termCode
            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa()!= null && ra.getValidoA()!=null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa()==null && ra.getValidoA()==null) )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            if(campoDaValidareResloq != null){
                return List.of(creaEsitoOk(nomeCampo));
            }

            if((listaDominio.contains(paramCode)) ||
                    Objects.equals(resInfoNotSummed, "y") ||
                        Objects.equals(accredproc, "V999A") ||
                            Objects.equals(progId, "MCG")
            ){
                return List.of(creaEsitoOk(nomeCampo));
            }

            return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
        }
    }
}
