/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCampoUgualeListaValoriAmmessiCampoObbligatorio")
public class RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio extends RegolaGenerica {


    public RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * "se PROGID = PNR e se  SAMPPOINT = MS.040.710 oppure MS.040.720
     * allora il campo SAMPMATCODE_PRODUCTION_METHOD è obbligatorio"
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            // sampMatCodeProductionMethod F
            String valoreCampoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);
            // progId
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante"))
            );
            // samppoint
            String valoreCampoCondizionanteDue = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionanteDue"))
            );

            // PNR
//            "se PROGID = MOC oppure MCG e se SAMPIDONEO=Y
//            allora ANPORTSEQ è obbligatorio (presente e diverso da blanks)"

            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");

            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if(Objects.equals(parametroCampoCondizionante, valoreCampoCondizionante) &&
                    listaValori.contains(valoreCampoCondizionanteDue) &&
                    (Objects.isNull(valoreCampoDaValidare) || valoreCampoDaValidare.isBlank())
            ){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo);
        }
    }
}

