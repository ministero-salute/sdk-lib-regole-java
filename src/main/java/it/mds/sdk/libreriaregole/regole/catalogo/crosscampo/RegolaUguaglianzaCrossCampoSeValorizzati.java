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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaUguaglianzaCrossCampoSeValorizzati")
public class RegolaUguaglianzaCrossCampoSeValorizzati extends RegolaGenerica {

    public RegolaUguaglianzaCrossCampoSeValorizzati(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il valore del campo in input sia uguale al valore di un altro campo del DTO se valorizzati
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String valoreCampoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

            String nomeCampoCondizionante = this.getParametri().getParametriMap().get("nomeCampoCondizionante");
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(nomeCampoCondizionante));

            if (valoreCampoDaValidare != null && valoreCampoCondizionante != null && !valoreCampoDaValidare.equals(valoreCampoCondizionante)) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola UguaglianzaCrossCampoSeValorizzati per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola UguaglianzaCrossCampoSeValorizzati per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}
