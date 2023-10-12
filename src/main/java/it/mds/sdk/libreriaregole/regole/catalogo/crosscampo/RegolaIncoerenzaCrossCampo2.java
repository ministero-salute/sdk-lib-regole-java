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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaIncoerenzaCrossCampo2")
public class RegolaIncoerenzaCrossCampo2 extends RegolaGenerica {

    public RegolaIncoerenzaCrossCampo2(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica la coerenza tra il valore di due campi del DTO in questo modo :
     * Se il campo in input (nomeCampo) é diverso ad un valore prefissato verifica che un altro campo dello stesso DTO (parametro campoDipendente ) abbia un valore diverso da una lista  prefissata (parametro listaValoriIncoerenti)
     * Esempio: se RESTYPE è diverso da BIN allora EVALCODE non può mai essere J040A oppure J041A
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), recordDtoGenerico);

        try {
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

            String valoreConfronto = this.getParametri().getParametriMap().get("valoreConfronto");
            String campoCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante"));

            List<String> listaValoriConfronto = Arrays.stream(this.getParametri().getParametriMap().get("listaValoriIncoerenti")
                    .split("\\|")).map(String::toLowerCase).collect(Collectors.toList());

            if (campoDaValidare != null && listaValoriConfronto.contains(campoDaValidare.toLowerCase()) && !valoreConfronto.equalsIgnoreCase(campoCondizionante)) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola IncoerenzaCrossCampo2 per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola IncoerenzaCrossCampo2 per il campo " + nomeCampo);
        }
    }
}

