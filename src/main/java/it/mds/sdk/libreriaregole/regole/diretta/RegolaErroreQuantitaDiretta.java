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
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaErroreQuantitaDiretta")
public class RegolaErroreQuantitaDiretta extends RegolaGenerica {

    public RegolaErroreQuantitaDiretta(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che se il valore del campo targatura del tracciato di input non è valorizzato con solo 0 (0, 00, 000, ….) allora il rapporto tra qta (dividendo) e fattore conv. (divisore) deve essere 1
     *
     * @param nomeCampo         nome campo da validare
     * @param recordDtoGenerico recordDTO
     * @return lista di Esiti
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            String valoreCampo = (String) recordDtoGenerico.getCampo(nomeCampo);
            if (valoreCampo != null) {
                Pattern pattern = Pattern.compile("\\b[0]*\\b");
                Matcher matcher = pattern.matcher(valoreCampo);
                if (matcher.matches()) {
                    return Collections.singletonList(creaEsitoOk(nomeCampo));
                }
            }
            if (valoreCampo != null && !valoreCampo.isEmpty()) {
                // essendo numeri passati in input li passo con string.valueof altrimenti ho una classCastException
                String dividendoString = String.valueOf(recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("dividendo")));
                String divisoreString = String.valueOf(recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("divisore")));

                if (dividendoString == null || dividendoString.isEmpty() || divisoreString == null || divisoreString.isEmpty()) {
                    return Collections.singletonList(creaEsitoKO(nomeCampo, "999", "Non è possibile validare la regola quantita perchè quantita o fattore conversione non sono presenti nel record"));
                }

                Double dividendo = Double.parseDouble(dividendoString);
                Double divisore = Double.parseDouble(divisoreString);

                if ((dividendo / divisore) == 1d) {
                    return Collections.singletonList(creaEsitoOk(nomeCampo));
                } else {
                    return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            }
            return Collections.singletonList(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola quantita per il campo " + nomeCampo);
        }
    }
}
