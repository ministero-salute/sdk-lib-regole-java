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
@XmlDiscriminatorValue("regolaIncoerenzaStatoEsteroConComuneASL")
public class RegolaIncoerenzaStatoEsteroConComuneASL extends RegolaGenerica {

    public RegolaIncoerenzaStatoEsteroConComuneASL(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * // se statoEstero è it
     * // comune o asl non possono ESSERE rispettivamente 999999 e 999
     * // se stato estero non è it
     * // comune o asl non possono NON ESSERE rispettivamente 999999 e 999
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
            Object campoDaValidare = recordDtoGenerico.getCampo(nomeCampo);
            Object codiceComuneResidenza = recordDtoGenerico.getCampo("codiceComuneResidenza");
            Object codiceAslResidenza = recordDtoGenerico.getCampo("codiceAslResidenza");

            log.debug("PARAMETRI: stato[{}] - comune[{}] - asl[{}]", campoDaValidare, codiceComuneResidenza, codiceAslResidenza);

            // se stato residenza != IT e comune != 999999 o asl != 999 vai in ko
            if (campoDaValidare != null && !"IT".equals(String.valueOf(campoDaValidare))
                    && ((codiceComuneResidenza != null && !"999999".equals(String.valueOf(codiceComuneResidenza))) ||
                    (codiceAslResidenza != null && !"999".equals(String.valueOf(codiceAslResidenza))))) {
                log.debug("Lo stato Estero di Residenza è IT ma è incoerente con uno dei campi Comune o ASL.");
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            log.info("Stato, Comune e ASL sono coerenti. Validazione OK.");
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola IncoerenzaCrossCampo per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola IncoerenzaCrossCampo per il campo " + nomeCampo);
        }
    }

}
