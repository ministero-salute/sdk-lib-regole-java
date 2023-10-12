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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCampoCondizionatoTreValori2")
public class RegolaCampoCondizionatoTreValori2 extends RegolaGenerica {

    public RegolaCampoCondizionatoTreValori2(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * controlla che:
     * se PROGID = ADD e PROGLEGALREF = N112A allora resUnit deve essere uguale a G061A oppure G062A
     * oppure
     * se PROGID = ADD e PROGLEGALREF = N262A allora resUnit deve essere uguale a G061A
     * oppure
     * se PROGID = ADD e PROGLEGALREF = N262A allora SAMPMATCODE_LEGIS deve essere uguale a A163R
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{} - nomeCampo[{}] recordDtoGenerico[{}] - BEGIN",
                RegolaCampoCondizionatoTreValori2.class.getName(), nomeCampo, recordDtoGenerico);

        try {
            // resUnit
            String campoDaValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
            //progID progLegalRef
            // i campi saranno parsati come string.valueof perchè i campi sono numeri e il cast va in errore.
            String campoCondizionante1 = String.valueOf(recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante1")));
            String campoCondizionante2 = String.valueOf(recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante2")));

            String parametroCondizionante1 = this.getParametri().getParametriMap().get("parametroCondizionante1"); //ADD
            String parametroCondizionante2 = this.getParametri().getParametriMap().get("parametroCondizionante2"); //N112A

            List<String> listaValoriAmmessi =
                    Arrays.stream(this.getParametri()
                                    .getParametriMap()
                                    .get("listaValoriAmmessi")
                                    .split("\\|"))
                            .collect(Collectors.toList()); //G061A, G062A

            log.debug("{} - nomeCampo[{}] recordDtoGenerico[{}] - campoDaValidare[{}] campoCondizionante1[{}] campoCondizionante2[{}] " +
                            "parametroCondizionante1[{}] parametroCondizionante2[{}] listaValoriAmmessi[{}]",
                    RegolaCampoCondizionatoTreValori2.class.getName(), nomeCampo, recordDtoGenerico, campoDaValidare,
                    campoCondizionante1, campoCondizionante2, parametroCondizionante1, parametroCondizionante2,
                    String.join("|", listaValoriAmmessi));

            if (listaValoriAmmessi.contains(campoDaValidare) &&
                    parametroCondizionante1.equals(campoCondizionante1)
                    && !parametroCondizionante2.equals(campoCondizionante2)
            ) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{} - nomeCampo[{}] recordDtoGenerico[{}] - Error: {}",
                    RegolaCampoCondizionatoTreValori2.class.getName(), nomeCampo, recordDtoGenerico, e.getMessage(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola regolaCampoCondizionatoTreValori per il campo " + nomeCampo);
        }
    }
}
