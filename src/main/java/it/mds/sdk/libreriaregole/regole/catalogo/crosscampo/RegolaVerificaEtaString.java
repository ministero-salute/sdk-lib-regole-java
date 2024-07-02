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
@XmlDiscriminatorValue("regolaVerificaEtaString")
public class RegolaVerificaEtaString extends RegolaGenerica {

    public RegolaVerificaEtaString(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * La regola consente di effettuare il calcolo dell'età mediante
     * sottrazione di due input verificando che il risultato sia compreso tra 18 e 100 anni
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

            String nomeCampoValidatore = this.getParametri().getParametriMap().get("campoDaValidare");
            //Parametrizzazione intervallo età

            String etaMin = this.getParametri().getParametriMap().get("etaMin");
            String etaMax = this.getParametri().getParametriMap().get("etaMax");
            Double etaMinNew = Double.parseDouble(etaMin);
            Double etaMaxNew = Double.parseDouble(etaMax);

            if(campoDaValidare == null || nomeCampoValidatore == null){
                log.debug("alcuni campi null -> campoDaValidare[{}], nomeCampoValidatore[{}] \n non permettono l'esecuzione della regola: [{}]",
                        campoDaValidare, nomeCampoValidatore, this.getClass().getName());
                return List.of(creaEsitoOk(nomeCampo));
            }

            Double campoDaValidareNew = Double.parseDouble(campoDaValidare);
            Double comparante = Double.parseDouble(String.valueOf(recordDtoGenerico.getCampo(nomeCampoValidatore)));
            Double eta = comparante - campoDaValidareNew;

            if ((eta >= etaMinNew) && (eta <= etaMaxNew)) {
                log.debug("OK: l'età [{}] è compresa tra il minimo [{}] e il massimo [{}]", eta, etaMinNew, etaMaxNew);
                return List.of(creaEsitoOk(nomeCampo));
            } else {
                log.debug("KO: l'età [{}] NON è compresa tra il minimo [{}] e il massimo [{}]", eta, etaMinNew, etaMaxNew);
                return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                //.withDescrizione("L'età deve essere compresa tra " + etaMin + " e " + etaMax)
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola VerificaEtaString per il campo " + nomeCampo);
        }

    }
}
