package it.mds.sdk.libreriaregole.regole.catalogo.cdm;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaB106ErroreCodCIG")
public class RegolaB106ErroreCodCIG extends RegolaGenerica {

    public RegolaB106ErroreCodCIG(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che il CIG rispetti le notazioni relative a CIG e SmartCIG
     *
     * @param nomeCampo
     * @param recordDtoGenerico
     * @return
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo); //
            String regexMatchCIG = "([0-9]{7}[0-9A-F]{3})";
            String regexSmartCIG = "([V-Z]{1}[0-9A-F]{9})";

            if (campoDaValidare == null) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }

            if (campoDaValidare.matches(regexMatchCIG) || campoDaValidare.matches(regexSmartCIG)) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            } else {
                return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Non è possibile validare laregolaDataCorrentePosterioreAnnoMeseRif del campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola di data posteriore del campo " + nomeCampo);
        }
    }
}
