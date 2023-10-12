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
@XmlDiscriminatorValue("regolaB29Diretta")
public class RegolaB29Diretta extends RegolaGenerica {


    public RegolaB29Diretta(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Se il valore del campo codiceEsenzione del csv è valorizzato, allora il valore del campo tipoDiEsenzione di input deve essere diverso
     * da 1 9 e null. Diverso da null significa presente e diverso da blanks.
     *
     * @param nomeCampo         il nome del campo da validare
     * @param recordDtoGenerico DTO del record di un flusso
     * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        List<Esito> listaEsiti = new ArrayList<>();
        try {
            //campo su cui applicare la regola
            String tipoDiEsenzione = (String) recordDtoGenerico.getCampo(nomeCampo);

            String codiceEsenzione = (String) recordDtoGenerico.getCampo("codiceEsenzione");

            if (codiceEsenzione != null && (tipoDiEsenzione == null || "1".equals(tipoDiEsenzione) || "9".equals(tipoDiEsenzione))) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare Obbligatorietà condizionata del campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
