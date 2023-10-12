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
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaValidazionePeriodoRiferimento")
public class RegolaValidazionePeriodoRiferimento extends RegolaGenerica {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RegolaValidazionePeriodoRiferimento(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * La data di apertura scheda non puo' essere successica al periodo di riferimento
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            String dataDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String annoRiferimento = (String) recordDtoGenerico.getCampo("annoRiferimento");
            String periodoRiferimento = (String) recordDtoGenerico.getCampo("periodoRiferimento");

            if (dataDaValidare == null || periodoRiferimento == null) {
                return List.of(creaEsitoOk(nomeCampo));
            }


            LocalDate dataValidatore = LocalDate.parse(dataDaValidare, formatter);

            LocalDate dataPerPeriodoRif = null;

            if ("s1".equalsIgnoreCase(periodoRiferimento)) {
                dataPerPeriodoRif = LocalDate.parse(annoRiferimento + "-06-30");
            } else if ("s2".equalsIgnoreCase(periodoRiferimento)) {
                dataPerPeriodoRif = LocalDate.parse(annoRiferimento + "-12-31");
            }

            if (dataValidatore.isAfter(dataPerPeriodoRif)) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

            } else {
                return List.of(creaEsitoOk(nomeCampo));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Non è possibile validare la regola di data antecedente del campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola di data antecedente del campo " + nomeCampo);
        }
    }
}

