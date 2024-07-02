/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

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
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaCampoDiversoDaParam")
public class RegolaObbligatorietaCampoDiversoDaParam extends RegolaGenerica {

    public RegolaObbligatorietaCampoDiversoDaParam(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il valore del campo in input sia diverso da due parametri prestabiliti
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            //daValidare è il campo EVALLIMITTYPE
            String daValidare = (String)recordDtoGenerico.getCampo(nomeCampo);
            //campo EVALCODE
            String campoValidatore = this.getParametri().getParametriMap().get("campoValidatore");
            String valoreCampoValidatore = String.valueOf(recordDtoGenerico.getCampo(campoValidatore));
            //Primo parametro da inputare
            String primoParametroInputDaConfrontare = this.getParametri().getParametriMap().get("primoParametroInputDaConfrontare");
            //Secondo parametro da inputare
            String secondoParametroInputDaConfrontare = this.getParametri().getParametriMap().get("secondoParametroInputDaConfrontare");

            if (!valoreCampoValidatore.equals(primoParametroInputDaConfrontare) && !valoreCampoValidatore.equals(secondoParametroInputDaConfrontare) && (daValidare == null || daValidare.isBlank())) {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola RegolaObbligatorietaCampoDiversoDaParam per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaObbligatorietaCampoDiversoDaParam per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
