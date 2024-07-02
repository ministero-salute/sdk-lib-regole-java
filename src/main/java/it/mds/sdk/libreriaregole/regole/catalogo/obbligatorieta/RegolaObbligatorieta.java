/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@XmlDiscriminatorValue("regolaObbligatorieta")
@NoArgsConstructor
public class RegolaObbligatorieta extends RegolaGenerica {

    public RegolaObbligatorieta(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che il campo in ingresso sia presente (obbligatorio)
     *
     * @param nomeCampo         è il nome del campo da validare
     * @param recordDtoGenerico corrisponde al DTO del record di un flusso
     * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            if (recordDtoGenerico.getCampo(nomeCampo) != null && !String.valueOf(recordDtoGenerico.getCampo(nomeCampo)).isBlank()) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Non è possibile controllare l'obbligatorietà di " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile controllare l'obbligatorietà di " + nomeCampo);
        }
        return listaEsiti;
    }

}
