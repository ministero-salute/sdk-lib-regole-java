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
public class RegolaDueCampiUgualiObbligatorietaCampoDiverso extends RegolaGenerica {

    public RegolaDueCampiUgualiObbligatorietaCampoDiverso(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     se PROGID = ADD e progLegalRef = N112A
     allora SAMPMATCODE_LEGIS deve essere diverso da A163R
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            //sampMatCodeLegis F
            Object valoreCampoDaValidare = recordDtoGenerico.getCampo(nomeCampo);
            //progId
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionante"))
            ); //valore del campo1 del DTO
            //progLegalRef
            String valoreCampoCondizionanteDue = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionanteDue"))
            ); //valore del campo1 del DTO

            //ADD
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");
            //N112A
            String parametroCondizionante2 = this.getParametri().getParametriMap().get("parametroCampoCondizionanteDue");
            //A163R
            String parametroCondizionante3 = this.getParametri().getParametriMap().get("parametroCampoCondizionanteTre");

            if(valoreCampoCondizionante.equals(parametroCondizionante) &&
                valoreCampoCondizionanteDue.equals(parametroCondizionante2) &&
                    parametroCondizionante3.equals(String.valueOf(valoreCampoDaValidare))
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