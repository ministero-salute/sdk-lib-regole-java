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
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCampoNotNullCondizionatoUnValoreUnCampo")
public class RegolaCampoNotNullCondizionatoUnValoreUnCampo extends RegolaGenerica {

    public RegolaCampoNotNullCondizionatoUnValoreUnCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * controlla che:
     * se codAzAllOrig è valorizzato e sampPoint= MS.060.400 (distributori latte crudo) allora il campo codAzAllOrig
     * deve essere uguale a quello riportato nel campo OSAid
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            // codAzAllOrig
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

            if (campoDaValidare != null) {
                // sampPoint
                String campoCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante"));
                String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante"); //MS.060.400

                if (campoCondizionante.equals(parametroCondizionante)) {
                    // OSAid
                    String campoCondizionante2 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante2"));

                    if (!campoCondizionante2.equals(campoDaValidare)) {
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                }
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola RegolaCampoNotNullCondizionatoUnValoreUnCampo per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaCampoNotNullCondizionatoUnValoreUnCampo per il campo " + nomeCampo);
        }
    }
}
