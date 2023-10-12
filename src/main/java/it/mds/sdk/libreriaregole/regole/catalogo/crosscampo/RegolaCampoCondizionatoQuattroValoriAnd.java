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
@XmlDiscriminatorValue("regolaCampoCondizionatoQuattroValoriAnd")
public class RegolaCampoCondizionatoQuattroValoriAnd extends RegolaGenerica {

    public RegolaCampoCondizionatoQuattroValoriAnd(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * controlla che:
     * se PROGID = MCG oppure PARAMCODE = RF-00011484-PAR,
     * e se EVALCODE = J041A
     * allora deve essere RESQUALVALUE = POS
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{} - nomeCampo[{}] recordDtoGenerico[{}] - BEGIN",
                RegolaCampoCondizionatoQuattroValoriAnd.class.getName(), nomeCampo, recordDtoGenerico);

        try {
            // resQualValue
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            //progID paramCode evalCode
            String campoCondizionante1 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante1"));
            String campoCondizionante2 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante2"));
            String campoCondizionante3 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante3"));

            String parametroCondizionante1 = this.getParametri().getParametriMap().get("parametroCondizionante1"); //MCG
            String parametroCondizionante2 = this.getParametri().getParametriMap().get("parametroCondizionante2"); //RF-00011484-PAR
            String parametroCondizionante3 = this.getParametri().getParametriMap().get("parametroCondizionante3"); //J041A
            String parametroDaValidare = this.getParametri().getParametriMap().get("parametroDaValidare"); //POS

            log.debug("{} - nomeCampo[{}] recordDtoGenerico[{}] - campoDaValidare[{}] " +
                            "campoCondizionante1[{}] campoCondizionante2[{}] campoCondizionante3[{}] parametroCondizionante1[{}] " +
                            "parametroCondizionante2[{}] parametroCondizionante3[{}] parametroDaValidare[{}]",
                    RegolaCampoCondizionatoQuattroValoriAnd.class.getName(), nomeCampo, recordDtoGenerico, campoDaValidare,
                    campoCondizionante1, campoCondizionante2, campoCondizionante3,
                    parametroCondizionante1, parametroCondizionante2, parametroCondizionante3, parametroDaValidare);

            if (parametroCondizionante1.equals(campoCondizionante1) &&
                    parametroCondizionante2.equals(campoCondizionante2)
                    && parametroCondizionante3.equals(campoCondizionante3)
                    && Objects.equals(parametroDaValidare, campoDaValidare)) {

                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{} - nomeCampo[{}] recordDtoGenerico[{}] - Error: {}",
                    RegolaCampoCondizionatoQuattroValoriAnd.class.getName(), nomeCampo, recordDtoGenerico, e.getMessage(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola regolaCampoCondizionatoQuattroValori per il campo " + nomeCampo);
        }
    }
}
