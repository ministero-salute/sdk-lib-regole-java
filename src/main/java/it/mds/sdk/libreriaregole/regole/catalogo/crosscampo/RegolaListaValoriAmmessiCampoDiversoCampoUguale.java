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
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaListaValoriAmmessiCampoDiversoCampoUguale")
public class RegolaListaValoriAmmessiCampoDiversoCampoUguale extends RegolaGenerica {
    public RegolaListaValoriAmmessiCampoDiversoCampoUguale(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se EVALCODE è J003A  o J031A  e  EVALLIMITTYPE ≠ W012A, allora  RESTYPE == VAL
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try{
            //resType
            String campoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);
            //evalCode
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante"))
            );
            //evalLimitType
            String valoreCampoCondizionanteDue = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionanteDue"))
            );
            // W012A
            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");
            // VAL
            String parametroCampoCondizionanteDue = this.getParametri().getParametriMap().get("parametroCampoCondizionanteDue");

            //Lista da 1 a N dei valori ammessi per il campo da validare
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if( listaValori.contains(valoreCampoCondizionante) &&
                    !valoreCampoCondizionanteDue.equals(parametroCampoCondizionante) &&
                        !campoDaValidare.equals(parametroCampoCondizionanteDue)
            ){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
        log.error("Non è possibile controllare l'obbligatorietà di " + nomeCampo, e);
        throw new ValidazioneImpossibileException("Non è possibile controllare l'obbligatorietà di " + nomeCampo);
    }

    }
}
