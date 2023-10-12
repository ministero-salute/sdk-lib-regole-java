package it.mds.sdk.libreriaregole.regole.catalogo.cnt;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regola3943")
public class Regola3943 extends RegolaGenerica {

    public Regola3943(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il valore del campo in input sia diverso da due parametri prestabiliti
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        try{
            /*
            "La “Diagnosi di chiusura” non è stata valorizzata ma si presentano le seguenti condizioni:
                · “data di chiusura scheda paziente” valorizzata e “Modalità conclusione” non valorizzata;
                · “data di chiusura scheda paziente” valorizzata e “Modalità conclusione” valorizzata con valore <> 1;
                · “data di chiusura scheda paziente” non valorizzata e “Modalità conclusione” valorizzata con valore <> 1;"

            */
            String daValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            if(StringUtils.isNotBlank(daValidare)){
                return List.of(creaEsitoOk(nomeCampo));
            }
            String dataChiusuraSchedaPaziente = (String) recordDtoGenerico.getCampo("dataChiusuraSchedaPaziente");
            String modalitaConclusione = (String) recordDtoGenerico.getCampo("modalitàConclusione");
            if (StringUtils.isNotBlank(dataChiusuraSchedaPaziente) && StringUtils.isBlank(modalitaConclusione)){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            if (!StringUtils.equals("1", modalitaConclusione)) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola RegolaObbligatorietaCampoDiversoDaParam per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaObbligatorietaCampoDiversoDaParam per il campo " + nomeCampo);
        }

        return List.of(creaEsitoOk(nomeCampo));
    }

}