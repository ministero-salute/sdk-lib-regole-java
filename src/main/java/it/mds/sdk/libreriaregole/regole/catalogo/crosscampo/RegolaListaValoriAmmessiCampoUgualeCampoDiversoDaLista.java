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
@XmlDiscriminatorValue("regolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista")
public class RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista extends RegolaGenerica {


    public RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * "se PROGID diverso da MCG oppure MON e
     * se ANMETHTYPE = AT06A
     * allora EVALCODE deve essere diverso da J003A oppure da J041A"
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
            // anmethtype
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante"))
            );
            //evalCode
            String valoreCampoCondizionanteDue = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionanteDue"))
            );

            // AT06A
            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");

            //Lista da 1 a N dei valori ammessi per il campo da validare
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriNonAmmessi").split("\\|")).collect(Collectors.toList());

            List<String> listaValoriDue = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriNonAmmessiDue").split("\\|")).collect(Collectors.toList());

            if(!listaValori.contains(valoreCampoDaValidare) && parametroCampoCondizionante.equals(valoreCampoCondizionante)
                    && listaValoriDue.contains(valoreCampoCondizionanteDue) ){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo);
        }
    }
}
