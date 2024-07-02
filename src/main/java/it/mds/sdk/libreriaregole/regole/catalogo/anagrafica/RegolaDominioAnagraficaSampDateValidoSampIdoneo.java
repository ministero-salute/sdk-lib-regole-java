/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioAnagraficaSampDateValidoSampIdoneo")
public class RegolaDominioAnagraficaSampDateValidoSampIdoneo extends RegolaDominioAnagraficaSampDateValido {

    public RegolaDominioAnagraficaSampDateValidoSampIdoneo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla se sampIdoneo = Y, allora il valore del campo in input "nomeCampo" se valorizzato,deve essere contenuto in un dominio
     * di valori di anagrafica e SampDate deve essere incluso tra le 2 date di validità dell'anagrafica
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] -  BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            // sampidoneo
            String sampidoneo = (String) recordDtoGenerico.getCampo("sampidoneo");

            if ("Y".equals(sampidoneo)) {
                return super.valida(nomeCampo, recordDtoGenerico);
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaDominioAnagraficaSampDateValidoSampIdoneo per il campo " + nomeCampo);
        }

    }
}
