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
@XmlDiscriminatorValue("regolaCampoCondizionatoDueValori")
public class RegolaCampoCondizionatoDueValori extends RegolaGenerica {

    public RegolaCampoCondizionatoDueValori(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * controlla che:
     * se origReg = 0UE, allora origCountry ≠ IT;
     * se origReg ≠ 0UE, allora origCountry = IT
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{} - nomeCampo[{}] recordDtoGenerico[{}] - BEGIN",
                RegolaCampoCondizionatoDueValori.class.getName(), nomeCampo, recordDtoGenerico);

        try {
            // origCountry
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            //origReg
            String campoCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante"));

            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante"); //OUE
            String parametroDaValidare = this.getParametri().getParametriMap().get("parametroDaValidare"); //IT

            log.debug("{} - nomeCampo[{}] recordDtoGenerico[{}] - campoDaValidare[{}] campoCondizionante[{}] " +
                            "parametroCondizionante[{}] parametroDaValidare[{}]",
                    RegolaCampoCondizionatoDueValori.class.getName(), nomeCampo, recordDtoGenerico, campoDaValidare,
                    campoCondizionante, parametroCondizionante, parametroDaValidare);

            if ((Objects.equals(campoCondizionante, parametroCondizionante) &&
                    !campoDaValidare.equals(parametroDaValidare)) ||
                    (!Objects.equals(campoCondizionante, parametroCondizionante) &&
                            campoDaValidare.equals(parametroDaValidare))) {

                return List.of(creaEsitoOk(nomeCampo));
            }

            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{} - nomeCampo[{}] recordDtoGenerico[{}] - Error: {}",
                    RegolaCampoCondizionatoDueValori.class.getName(), nomeCampo, recordDtoGenerico, e.getMessage(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola regolaCampoCondizionatoDueValori per il campo " + nomeCampo);
        }
    }
}
