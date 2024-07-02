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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCoerenzaRegioneCampoInput")
public class RegolaCoerenzaRegioneCampoInput extends RegolaGenerica {

    public RegolaCoerenzaRegioneCampoInput(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Il campo Codice Regione non coincide con la regione che sta trasmettendo il file.
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
            String codiceRegione = (String) (recordDtoGenerico.getCampo(nomeCampo));
            String codiceRegioneInput = recordDtoGenerico.getCampiInput().getCodiceRegioneInput();

            if(codiceRegione == null ){
                log.debug("[{}].valida - codiceRegione[{}] is null", this.getClass().getName(), codiceRegione);
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            if(codiceRegioneInput == null){
                log.debug("[{}].valida - codiceRegioneInput[{}] is null", this.getClass().getName(), codiceRegioneInput);
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            if(!Objects.equals(codiceRegione.toLowerCase(), codiceRegioneInput.toLowerCase())){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]", this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaCodiceAIC per il campo " + nomeCampo);
        }

    }

}
