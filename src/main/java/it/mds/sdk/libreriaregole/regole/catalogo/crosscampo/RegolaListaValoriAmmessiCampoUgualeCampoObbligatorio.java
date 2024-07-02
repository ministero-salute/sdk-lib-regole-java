/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaListaValoriAmmessiCampoUgualeCampoObbligatorio")
public class RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio extends RegolaGenerica {


    public RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se PROGID è diverso da VIG001CP e da MON e EVALINFO_SAMPANASSES=J038A allora EVALINFO_SAMPTKASSES deve essere J038A
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            //progId
            String valoreCampoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);
            // evalInfoSampAnasses
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante"))
            );
            //evalInfoSamptKasses
            String valoreCampoCondizionanteDue = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionanteDue"))
            );

            // J038A
            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");

            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriNonAmmessi").split("\\|")).collect(Collectors.toList());

             if(valoreCampoCondizionanteDue != null &&
                     !listaValori.contains(valoreCampoDaValidare) &&
                     parametroCampoCondizionante.equals(valoreCampoCondizionante) &&
                     !parametroCampoCondizionante.equals(valoreCampoCondizionanteDue)
             ){
                 return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
             }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo);
        }
    }
}
