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
@XmlDiscriminatorValue("regolaObbligatorietaCondizionataCampo")
public class RegolaObbligatorietaCondizionataCampo extends RegolaGenerica {

    public RegolaObbligatorietaCondizionataCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il campo in input sia presente(obbligatorio) se un altro campo del DTO è valorizzato.
     *
     * @param nomeCampo         il nome del campo da validare
     * @param recordDtoGenerico DTO del record di un flusso
     * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            Object campoDaValidare = recordDtoGenerico.getCampo(nomeCampo);

            String nomeCampoCondizionante = this.getParametri().getParametriMap().get("nomeCampoCondizionante");
            Object valoreCampoCondizionante = recordDtoGenerico.getCampo(nomeCampoCondizionante);


            if(campoDaValidare != null && !String.valueOf(campoDaValidare).isBlank() && valoreCampoCondizionante == null){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
           return  List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare Obbligatorietà condizionata Campo del campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare Obbligatorietà condizionata del campo " + nomeCampo);
        }

    }


}
