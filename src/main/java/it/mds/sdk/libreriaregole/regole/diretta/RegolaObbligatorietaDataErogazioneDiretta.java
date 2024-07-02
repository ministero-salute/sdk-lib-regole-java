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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaDataErogazioneDiretta")
public class RegolaObbligatorietaDataErogazioneDiretta extends RegolaGenerica {

    public RegolaObbligatorietaDataErogazioneDiretta(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * il valore del campo (data) data_erog del tracciato di input deve essere compresa tra il periodo di riferimento
     * (campi "anno"/"mese" del tracciato di input) -
     * (operazione) 12 mesi e il periodo di riferimento (campi "anno"/"mese" del tracciato di input)
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
//            data erogazione = 22-06-2022 e anno/mese rif = 23-06-2022, anno/mese rif - 12 mesi = 23 - 06 -2021.
//            Regola ok perchè compresa nel range

            LocalDate dataErogazione = LocalDate.parse((String) recordDtoGenerico.getCampo(nomeCampo));

            String annoDiRiferimento = (String) recordDtoGenerico.getCampo("annoDiRiferimento");
            String meseDiRiferimento = (String) recordDtoGenerico.getCampo("meseDiRiferimento");
            LocalDate periodoRiferimentoDTO = LocalDate.parse(annoDiRiferimento + "-" + meseDiRiferimento + "-01");
            LocalDate periodoRiferimentoDtoMenoUnAnno = periodoRiferimentoDTO.minusYears(1);
            periodoRiferimentoDTO = periodoRiferimentoDTO.plusMonths(1);

            if (!periodoRiferimentoDtoMenoUnAnno.isAfter(dataErogazione) && periodoRiferimentoDTO.isAfter(dataErogazione)) {
                return List.of(creaEsitoOk(nomeCampo));
            }
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola per il campo " + nomeCampo);
        } catch (DateTimeParseException dataE) {
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }

    }

}
