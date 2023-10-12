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
@XmlDiscriminatorValue("regolaDueCampiUgualiObbligatorietaCampoDiverso")
public class RegolaTreCampiUgualiObbligatorietaCampoDiverso extends RegolaGenerica {

    public RegolaTreCampiUgualiObbligatorietaCampoDiverso(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     se PROGID = ADD e se SAMPMATCODELEGIS = A0C3L e se PARAMCODE = RF-00000046-ADD  allora
     EVALLIMITTYPE non può essere uguale a W001A
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            //evalLimitType F
            Object valoreCampoDaValidare = recordDtoGenerico.getCampo(nomeCampo);
            //progId
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionante"))
            );
            //sampMatCodeLegis F
            Object valoreCampoCondizionanteDue = recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionanteDue"));
            //paramCode F
            Object valoreCampoCondizionanteTre = recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionanteTre"));

            //ADD
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");
            //A03CL
            String parametroCondizionante2 = this.getParametri().getParametriMap().get("parametroCampoCondizionanteDue");
            //RF-00000046-ADD
            String parametroCondizionante3 = this.getParametri().getParametriMap().get("parametroCampoCondizionanteTre");
            //W001A
            String parametroCondizionante4 = this.getParametri().getParametriMap().get("parametroCampoCondizionanteQuattro");

            if(parametroCondizionante.equals(valoreCampoCondizionante) &&
                    parametroCondizionante2.equals(String.valueOf(valoreCampoCondizionanteDue)) &&
                        parametroCondizionante3.equals(String.valueOf(valoreCampoCondizionanteTre)) &&
                            parametroCondizionante4.equals(String.valueOf(valoreCampoDaValidare))
            ){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata due campi uguali " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata due campi uguali " + nomeCampo);
        }

    }
}