/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDisuguaglianzaListaCampi")
public class RegolaDisuguaglianzaListaCampi extends RegolaGenerica {

    public RegolaDisuguaglianzaListaCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     **
     * Verifica che n valori del DTO in ingresso siano diversi da altrettanti n valori del DTO.
     * Esempio: suppondendo di avere i seguenti 6 campi nel DTO,
     *
     *      1. RegioneDomicilioSanitario
     *      2. ComuneDomicilioSanitario
     *      3. AslDomicilioSanitario
     *      4. RegioneResidenza
     *      5. ComuneResidenza
     *      6. AslResidenza
     *
     * la regola è valida se RegioneDomicilioSanitario+ComuneDomicilioSanitario+AslDomicilioSanitario != RegioneResidenza+ComuneResidenza+AslResidenza
     * oppure se i "campiDaConfrontare" sono null
     *
     *
     * @param nomeCampo campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        StringBuilder concatCampiDaConfrontare = new StringBuilder();
        StringBuilder concatCampiConfronto = new StringBuilder();
        List<String> chiaviDaConfrontare = Arrays.stream(this.getParametri().getParametriMap().
                get("listaCampiDaConfrontare").split("\\|")).collect(Collectors.toList());
        List<String> chiaviConfronto = Arrays.stream(this.getParametri().getParametriMap().
                get("listaCampiConfronto").split("\\|")).collect(Collectors.toList());
        for(String s : chiaviDaConfrontare){
            try {
                concatCampiDaConfrontare.append(recordDtoGenerico.getCampo(s) != null ? recordDtoGenerico.getCampo(s) : "");
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.error("Impossibile validare la regola Lista valori ammessi per il campo " +nomeCampo , e );
                throw new ValidazioneImpossibileException("Impossibile validare la regola uguaglianza lista campi per il campo " +nomeCampo );
            }
        }
        for(String s : chiaviConfronto){
            try {
                concatCampiConfronto.append(recordDtoGenerico.getCampo(s));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.error("Impossibile validare la regola Lista valori ammessi per il campo " +nomeCampo , e );
                throw new ValidazioneImpossibileException("Impossibile validare la regola uguaglianza lista campi per il campo " +nomeCampo );
            }
        }

        if(concatCampiDaConfrontare.length() == 0){
            listaEsiti.add(creaEsitoOk(nomeCampo));
        }
        else if(concatCampiConfronto.length() != 0 && concatCampiDaConfrontare.toString().equals(concatCampiConfronto.toString())){
            listaEsiti.add(creaEsitoOk(nomeCampo));
        }else{
            listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
        }

        return listaEsiti;
    }
}
