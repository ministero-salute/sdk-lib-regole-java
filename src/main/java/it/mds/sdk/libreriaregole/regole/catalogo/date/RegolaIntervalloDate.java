package it.mds.sdk.libreriaregole.regole.catalogo.date;

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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaIntervalloDate")
public class RegolaIntervalloDate extends RegolaGenerica {


    public RegolaIntervalloDate(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che la data passata in ingresso sia compresa tra startDate ed endDate estremi inclusi
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            String campoDaValidareStr = (String) recordDtoGenerico.getCampo(nomeCampo);
            String startDateStr = this.getParametri().getParametriMap().get("startDate");
            String dataValidatoreStart = (String) recordDtoGenerico.getCampo(startDateStr);
            LocalDate startDate = null;
            LocalDate endDate = null;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate campoDaValidare = LocalDate.parse(campoDaValidareStr, formatter);


            if (campoDaValidare == null) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            startDate = LocalDate.parse(dataValidatoreStart, formatter);
            endDate = LocalDate.now();
            

            if (startDate == null && campoDaValidare.isBefore(endDate)) {
                return List.of(creaEsitoOk(nomeCampo));
            } else if (endDate == null && campoDaValidare.isAfter(startDate)) {
                return List.of(creaEsitoOk(nomeCampo));
            } else if (startDate != null && startDate.compareTo(campoDaValidare) * campoDaValidare.compareTo(endDate) >= 0) {
                return List.of(creaEsitoOk(nomeCampo));
            } else {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Non è possibile validare la regola di intervallo date del campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola di intervallo date del campo " + nomeCampo);
        } catch (DateTimeParseException dataE) {
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }catch (NullPointerException e)
        {
            log.error("Non è possibile validare la regola di intervallo date del campo perchè " + nomeCampo + "e' null", e);
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(),this.getDesErrore()));
        }
    }

}

	