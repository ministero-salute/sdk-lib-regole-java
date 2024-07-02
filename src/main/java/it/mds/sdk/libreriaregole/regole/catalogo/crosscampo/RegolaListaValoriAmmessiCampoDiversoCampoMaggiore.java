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
@XmlDiscriminatorValue("regolaListaValoriAmmessiCampoDiversoCampoMaggiore")
public class RegolaListaValoriAmmessiCampoDiversoCampoMaggiore extends RegolaGenerica {

    public RegolaListaValoriAmmessiCampoDiversoCampoMaggiore(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se EVALCODE è J003A  o J031A  e  EVALLIMITTYPE ≠ W005A, allora  RESVAL > EVALLOWLIMIT
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            //resVal
            Double valoreCampoDaValidare = (Double)recordDtoGenerico.getCampo(nomeCampo);

            if(Objects.isNull(valoreCampoDaValidare)){
                log.trace("VALIDAZIONE OK: {}.valida() - il campo {} is null", this.getClass().getName(), nomeCampo);
                return List.of(creaEsitoOk(nomeCampo));
            }
            // evalLimitType
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionante"))
            );
            //evalCode
            String valoreCampoCondizionanteDue = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionanteDue"))
            );
            //evallowLimit
            Double valoreCampoCondizionanteTre = (Double) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionanteTre")
            );
            // w005a
            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");

            //Lista da 1 a N dei valori ammessi per il campo da validare
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if(listaValori.contains(valoreCampoCondizionanteDue) &&
                    !Objects.equals(valoreCampoCondizionante, parametroCampoCondizionante) &&
                    valoreCampoDaValidare <= valoreCampoCondizionanteTre){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo);
        }


    }
}