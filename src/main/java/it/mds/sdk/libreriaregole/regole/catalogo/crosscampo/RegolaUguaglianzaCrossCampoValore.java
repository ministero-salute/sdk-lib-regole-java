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
import java.util.ArrayList;
import java.util.List;


@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaUguaglianzaCrossCampoValore")
public class RegolaUguaglianzaCrossCampoValore extends RegolaGenerica {


    public RegolaUguaglianzaCrossCampoValore(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il valore del campo in input sia uguale ad un parametro prefissato; qualora così fosse verifica che
     * il secondo campo sia anch'esso uguale al parametro in input, e se l'esito è true i due campi sono identici
     * si assume che, sia il campo in input e che l'altro campo del DTO, siano valorizzati (non null)
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            //nel caso della regola SISM, daValidare è il nostro ASL Provenienza
            String daValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
            String validatore = this.getParametri().getParametriMap().get("campoValidatore");
            //inseriamo adesso il parametro da inputare dall'esterno per verifica
            String parametroInput = this.getParametri().getParametriMap().get("parametroInput");
            //comparante rappresenta il campo codice Regione di Residenza
            String comparante = String.valueOf(recordDtoGenerico.getCampo(validatore));
            if (daValidare.equals(parametroInput)) {
                if (comparante.equals(parametroInput)) {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                } else {
                    listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                            //.withDescrizione("Il campo : " + nomeCampo + " e campo : " + validatore + " non sono uguali")
                }
                return listaEsiti;

            }
            listaEsiti.add(creaEsitoOk(nomeCampo));
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola UguaglianzaCrossCampoValore per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola UguaglianzaCrossCampoValore per il campo " + nomeCampo);
        }
        return listaEsiti;
    }


}
