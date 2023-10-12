package it.mds.sdk.libreriaregole.regole.catalogo.date;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDataPosterioreConParametro")
public class RegolaDataPosterioreConParametro extends RegolaDataPosteriore {

    public RegolaDataPosterioreConParametro(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * In caso di antigene uguale ad un parametro prefissato, controlla che il valore del campo passato in ingresso(una data)
     * sia posteriore a un'altra data (se valorizzata) all'interno dello stesso DTO.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        try {
            String campoAntigene = this.getParametri().getParametriMap().get("campoAntigene");
            String valoreCampoAntigene = String.valueOf(recordDtoGenerico.getCampo(campoAntigene));
            String parametroAntigeneVerifica1 = this.getParametri().getParametriMap().get("parametroAntigene1");
            String parametroAntigeneVerifica2 = this.getParametri().getParametriMap().get("parametroAntigene2");

            if (valoreCampoAntigene.equals(parametroAntigeneVerifica1) || valoreCampoAntigene.equals(parametroAntigeneVerifica2)) {
                listaEsiti = super.valida(nomeCampo, recordDtoGenerico);
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola UguaglianzaCrossCampo per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola UguaglianzaCrossCampo per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
