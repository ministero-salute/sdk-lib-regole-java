package it.mds.sdk.libreriaregole.regole.catalogo;

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
@XmlDiscriminatorValue("regolaIntervalloMinMax")
public class RegolaIntervalloMinMax extends RegolaGenerica {


    public RegolaIntervalloMinMax(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo passato in input sia valorizzato e compreso tra minimo e massimo estremi compresi
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO relativo ad un record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {

            Double valoreCampoDaValidare = (Double) recordDtoGenerico.getCampo(nomeCampo);

            // parametro con cui devo confrontare il valoreCampoDaValidare
            Double parametroMin = Double.valueOf(this.getParametri().getParametriMap().get("parametroMin"));
            Double parametroMax = Double.valueOf(this.getParametri().getParametriMap().get("parametroMax"));

            if (valoreCampoDaValidare != null && (valoreCampoDaValidare < parametroMin || valoreCampoDaValidare > parametroMax)) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la Regola IntervalloMinMax  per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la Regola IntervalloMinMax  per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}

	