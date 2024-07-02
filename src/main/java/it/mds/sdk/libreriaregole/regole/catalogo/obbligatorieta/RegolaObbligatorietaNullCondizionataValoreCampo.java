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
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaNullCondizionataValoreCampo")
public class RegolaObbligatorietaNullCondizionataValoreCampo extends RegolaGenerica {

    public RegolaObbligatorietaNullCondizionataValoreCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il campo in input non sia presente o uguale a blanks(obbligatorio) se un altro campo del DTO assume un certo valore.
     *
     *  Esempio : nel caso di SALM, RESQUALVALUE non valorizzato se ANMETHTYPE uguale a AT08A
     *
     * @param nomeCampo il nome del campo da validare
     * @param recordDtoGenerico DTO del record di un flusso
     * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        String nomeCampoCondizionante;
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {

            nomeCampoCondizionante = this.getParametri().getParametriMap().get("nomeCampoCondizionante");//nel nostro esempio ANMETHTYPE
            Object campoDaValidare = recordDtoGenerico.getCampo(nomeCampo);
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(nomeCampoCondizionante));//nel nostro esempio il valore che ha effettivamente ANMETHTYPE all'interno del record DTO
            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");

            if(campoDaValidare!=null && parametroCampoCondizionante.equals(valoreCampoCondizionante)) {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
            }else{
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException |IllegalAccessException  e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaObbligatorietaNullCondizionataValoreCampo del campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
