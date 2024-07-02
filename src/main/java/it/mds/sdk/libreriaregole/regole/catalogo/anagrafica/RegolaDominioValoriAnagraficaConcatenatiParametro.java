/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaConcatenatiParametro")
public class RegolaDominioValoriAnagraficaConcatenatiParametro extends RegolaDominioValoriAnagraficaConcatenati {

    public RegolaDominioValoriAnagraficaConcatenatiParametro(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo da validare sia conforme alla tabella passata  se la il parametro  è uguale ad un valoreParametro
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        try {
            String parametroInput = this.getParametri().getParametriMap().get("parametro");
            String valoreParametroInput = String.valueOf(recordDtoGenerico.getCampo(parametroInput));
            String valoreParametro = this.getParametri().getParametriMap().get("valoreParametro");

            if (valoreParametroInput.equals(valoreParametro)) {
                listaEsiti = super.valida(nomeCampo, recordDtoGenerico);
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola DominioValoriAnagraficaConcatenatiParametro per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola DominioValoriAnagraficaConcatenatiParametro per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}
