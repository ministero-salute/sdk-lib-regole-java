/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
@XmlDiscriminatorValue("regolaUguaglianzaCrossCampoCondizionale")
public class RegolaUguaglianzaCrossCampoCondizionale extends RegolaUguaglianzaCrossCampo {

    public RegolaUguaglianzaCrossCampoCondizionale(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il valore del campo in input sia uguale al valore di un altro campo del DTO e
     * che la modalità di trasmissione sia pari ad un valore prefissato
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        try {
            String modalitaTrasmissioneParametro = this.getParametri().getParametriMap().get("modalitaTrasmissione");
            String modalitaTrasmissione = (String)recordDtoGenerico.getCampo(modalitaTrasmissioneParametro);
            String parametroModalitaTrasmissione = this.getParametri().getParametriMap().get("parametroModalitaTrasmissione");

            if (modalitaTrasmissione.equals(parametroModalitaTrasmissione)) {
                listaEsiti = super.valida(nomeCampo, recordDtoGenerico);
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola UguaglianzaCrossCampoCondizionale per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola UguaglianzaCrossCampoCondizionale per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}
