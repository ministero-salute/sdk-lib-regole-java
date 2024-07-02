/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.lunghezza;

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
@XmlDiscriminatorValue("regolaIntervalloLunghezza")
public class RegolaIntervalloLunghezza extends RegolaGenerica {

    public RegolaIntervalloLunghezza(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che la lunghezza del campo sia compresa fra 2 caratteri alfanumerici
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            var campoDaValidare = recordDtoGenerico.getCampo(nomeCampo);
            if(campoDaValidare == null)
                return List.of(creaEsitoOk(nomeCampo));

            Integer daValidare = String.valueOf(campoDaValidare).length();
            Integer minLunghezza = Integer.parseInt(this.getParametri().getParametriMap().get("minLunghezza"));
            Integer maxLunghezza = Integer.parseInt(this.getParametri().getParametriMap().get("maxLunghezza"));

            if (daValidare >= minLunghezza && daValidare <= maxLunghezza) {
                return List.of(creaEsitoOk(nomeCampo));
            }
            log.debug("{}.valida - daValidare[{}] - minLunghezza[{}] - maxLunghezza[{}] - campoDaValidare non è compreso tra minLunghezza, maxLunghezza",
                    this.getClass().getName(), daValidare, minLunghezza, maxLunghezza);
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NumberFormatException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola lunghezza del campo " + nomeCampo);
        }
    }
}
