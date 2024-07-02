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
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita")
public class RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita extends RegolaGenerica {

    public RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            String regioneSomministazione = (String) (recordDtoGenerico.getCampo(nomeCampo));
            String codiceRegioneResidenza = (String)recordDtoGenerico.getCampo("codiceRegioneResidenza");
            String codiceRegioneDomicilio = (String) recordDtoGenerico.getCampo("codiceRegioneDomicilio");
            String modalita = (String) recordDtoGenerico.getCampo("modalita");

            if("MV".equalsIgnoreCase(modalita)) {
                if((Objects.equals(regioneSomministazione, codiceRegioneResidenza) ||
                        Objects.equals(regioneSomministazione, codiceRegioneDomicilio)) ){
                    return List.of(creaEsitoOk(nomeCampo));
                }
                else return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaCodiceAIC per il campo " + nomeCampo);
        }

    }

}
