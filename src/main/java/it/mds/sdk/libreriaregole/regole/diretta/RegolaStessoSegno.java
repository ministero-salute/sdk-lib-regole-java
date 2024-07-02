/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.diretta;

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
@XmlDiscriminatorValue("regolaStessoSegno")
public class RegolaStessoSegno extends RegolaGenerica {

    public RegolaStessoSegno(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Se il valore del campo idContatto del tracciato di input NON è costituito solo da n caratteri uguali a 0 allora i segni dei campi
     * costoServizio e costoAcquisto del tracciato di input devono essere uguali
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        String segnoCostoServizio = null;
        String segnoCostoAcquisto = null;

        List<Esito> listaEsiti = new ArrayList<>();
        try {
            //campo su cui applicare la regola
            String idContatto = (String) recordDtoGenerico.getCampo(nomeCampo);

            String costoServizio = (String) recordDtoGenerico.getCampo("costoServizio");
            String costoAcquisto = (String) recordDtoGenerico.getCampo("costoAcquisto");
            if (costoServizio == null) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
            if (costoServizio != null) {
                segnoCostoServizio = costoServizio.substring(0, 1);
            }

            if (costoAcquisto != null) {
                segnoCostoAcquisto = costoAcquisto.substring(0, 1);
            }

            boolean isDiversoTuttiZero = false;

            for (int i = 0; i < idContatto.length(); i++) {
                if (idContatto.charAt(i) != '0') {
                    isDiversoTuttiZero = true;
                    break;
                }
            }

            if (isDiversoTuttiZero) {
                if (("-".equals(segnoCostoServizio) && "-".equals(segnoCostoAcquisto)) ||
                        (!"-".equals(segnoCostoServizio) && !"-".equals(segnoCostoAcquisto))) {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                } else {
                    listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaB17Diretta del campo " + nomeCampo);
        } catch (NullPointerException e) {
            log.error("Non è possibile validare la regola di intervallo date del campo perchè " + nomeCampo + "e' null", e);
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }
        return listaEsiti;
    }
}
