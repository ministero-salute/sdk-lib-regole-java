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

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCampoUgualeAPrimiQuattroCaratteri")
public class RegolaCampoUgualeAPrimiQuattroCaratteri  extends RegolaGenerica {

    public RegolaCampoUgualeAPrimiQuattroCaratteri(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *
     * controlla che i primi 4 char del campo da validare siano uguali ad un altro campo del DTO.
     * @param nomeCampo campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            String campoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);
            String campoCondizionante = (String)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante"));

            if(campoDaValidare.substring(0, 4).equals(campoCondizionante.substring(0,4))){
                return List.of(creaEsitoOk(nomeCampo));
            }
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException  e) {
            log.error("Impossibile validare la regola diversitaValoreCrossCampo per il campo " + nomeCampo, e );
            throw new ValidazioneImpossibileException("Impossibile validare la regola diversitaValoreCrossCampo per il campo "  + nomeCampo );
        }
    }
}
