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
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDataNonCompresaNelPeriodoRiferimento")
public class RegolaDataNonCompresaNelPeriodoRiferimento extends RegolaGenerica {

    public RegolaDataNonCompresaNelPeriodoRiferimento(String nome, String codErrore, String desErrore, Parametri parametri) {
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
            String dataDaComparareString = (String) recordDtoGenerico.getCampo(nomeCampo);
            String periodoRiferimentoInput = recordDtoGenerico.getCampiInput().getPeriodoRiferimentoInput();

            if(dataDaComparareString == null || periodoRiferimentoInput == null){
                log.debug("ATTENZIONE: Regola KO. il campo " + nomeCampo + " è null.");
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola di data antecedente del campo " + nomeCampo);
        }
    }
}

