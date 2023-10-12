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
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaPeriodoDiRiferimentoDiretta")
public class RegolaObbligatorietaPeriodoDiRiferimentoDiretta extends RegolaGenerica {

    public RegolaObbligatorietaPeriodoDiRiferimentoDiretta(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            String annoDiRiferimento = (String) recordDtoGenerico.getCampo(nomeCampo);
            String meseDiRiferimento = (String) recordDtoGenerico.getCampo("meseDiRiferimento");

            LocalDate periodoRiferimentoDTO = getInitialDateFromMonthYear(annoDiRiferimento, meseDiRiferimento);

            if (Period.between(periodoRiferimentoDTO, LocalDate.now()).getMonths() > 2) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la pergola per il campo " + nomeCampo);
        } catch (DateTimeParseException dataE) {
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }
    }

    private LocalDate getInitialDateFromMonthYear(String anno, String mese) {
        return LocalDate.parse(anno + "-" + mese + "-" + "01");
    }

}
