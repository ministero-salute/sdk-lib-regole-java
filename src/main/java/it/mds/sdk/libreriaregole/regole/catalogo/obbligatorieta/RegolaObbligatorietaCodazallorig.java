package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

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
@XmlDiscriminatorValue("regolaObbligatorietaCodazallorig")
@NoArgsConstructor
public class RegolaObbligatorietaCodazallorig extends RegolaGenerica {

    public RegolaObbligatorietaCodazallorig(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo in input "nomeCampo" sia obbligatorio, o
     * soddisfi delle regole, altrimenti va in KO
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            var progId = recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("progId"));
            var origCountry = recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("origCountry"));
            var samppoint = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("samppoint"));
            var sampStrategy = recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("sampStrategy"));

            var pnrParam = this.getParametri().getParametriMap().get("pnrParam");
            var itParam = this.getParametri().getParametriMap().get("itParam");
            var st90aParam = this.getParametri().getParametriMap().get("st90Param");

            List<String> MS_PARAM_LIST = Arrays
                    .stream(this.getParametri().getParametriMap().get("listaValoriAmmessi").split("\\|"))
                    .collect(Collectors.toList());

            var MS_PARAM = "MS.B80.500";

            if(( (progId.equals(pnrParam) && origCountry.equals(itParam) && MS_PARAM_LIST.contains(samppoint)) || ( MS_PARAM.equals(samppoint) && !sampStrategy.equals(st90aParam) ) )
                && (recordDtoGenerico.getCampo(nomeCampo) != null &&  !"".equals(recordDtoGenerico.getCampo(nomeCampo)))){
                    return List.of(creaEsitoOk(nomeCampo));
                }


            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ValidazioneImpossibileException(
                    "Impossibile validare RegolaObbligatorietaCodazallorig del campo " + nomeCampo);
        }

    }

}
