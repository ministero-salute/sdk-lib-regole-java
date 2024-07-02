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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaValoreMinoreDiUnCampo")
public class RegolaValoreMinoreDiUnCampo extends RegolaGenerica {

    public RegolaValoreMinoreDiUnCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo sia minore al valore di un altro campo del DTO se entrambi sono valorizzati
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO relativo ad un record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {

            Double valoreCampoDaValidare = (Double) recordDtoGenerico.getCampo(nomeCampo);

            String nomeCampoCondizionante = this.getParametri().getParametriMap().get("nomeCampoCondizionante");//campo del DTO
            Double valoreCampoCondizionante = (Double) recordDtoGenerico.getCampo(nomeCampoCondizionante); //valore del campo del DTO

            if (valoreCampoDaValidare != null && valoreCampoCondizionante != null && valoreCampoDaValidare > valoreCampoCondizionante) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la Regola ValoreMinoreDiUnCampo  per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la Regola ValoreMinoreDiUnCampo  per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}


