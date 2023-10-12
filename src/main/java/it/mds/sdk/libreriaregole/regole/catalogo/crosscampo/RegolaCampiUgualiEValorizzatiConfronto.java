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
import java.util.List;
import java.util.Objects;


@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCampiUgualiEValorizzatiConfronto")
public class RegolaCampiUgualiEValorizzatiConfronto extends RegolaGenerica {

    public RegolaCampiUgualiEValorizzatiConfronto(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *
     * controlla che:
     * se EVALCODE= J002A e sono valorizzati entrambi i campi RESVAL e EVALLOWLIMIT, allora deve essere RESVAL <= EVALLOWLIMIT
     * se RESVAL > EVALLOWLIMIT, allora evalCode deve essere diverso da J002A
     *  @param nomeCampo campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            // resVal
            Double campoDaValidare = (Double)recordDtoGenerico.getCampo(nomeCampo);
            //evallowLimit
            Double campoCondizionante = (Double)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante"));
            //evalCode
            String campoCondizionanteDue = (String)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionanteDue"));

            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante"); //J002A

            if(Objects.equals(campoCondizionanteDue, parametroCondizionante) && campoDaValidare != null && campoCondizionante != null && (campoDaValidare > campoCondizionante)){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException  e) {
            log.error("Impossibile validare la regola diversitaValoreCrossCampo per il campo " + nomeCampo, e );
            throw new ValidazioneImpossibileException("Impossibile validare la regola diversitaValoreCrossCampo per il campo "  + nomeCampo );
        }
    }
}