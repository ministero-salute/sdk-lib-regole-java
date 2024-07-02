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
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaConcatenatiNazione")
public class RegolaDominioValoriAnagraficaConcatenatiNazione extends RegolaDominioValoriAnagraficaConcatenati {

    public RegolaDominioValoriAnagraficaConcatenatiNazione(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo da validare sia conforme alla tabella ANAG_DPM_REG_PROV_COM se la Nazione di riferimento è uguale a IT
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        try {
            String codiceNazione = this.getParametri().getParametriMap().get("codiceNazione");
            String valoreCodiceNazione = String.valueOf(recordDtoGenerico.getCampo(codiceNazione));
            String parametroCodiceNazione = this.getParametri().getParametriMap().get("parametroCodiceNazione");

            if (valoreCodiceNazione.equals(parametroCodiceNazione)) {
                listaEsiti = super.valida(nomeCampo, recordDtoGenerico);
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola DominioValoriAnagraficaConcatenatiNazione per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola DominioValoriAnagraficaConcatenatiNazione per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}
