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
@XmlDiscriminatorValue("regolaObbligatorietaCondizionataLista")
public class RegolaObbligatorietaCondizionataLista extends RegolaGenerica {

    public RegolaObbligatorietaCondizionataLista(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che il campo in input sia valorizzato e diverso da due parametri predefiniti
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String daValidare = (String) recordDtoGenerico.getCampo(nomeCampo); //daValidare è il campo in input (es. codiceStruttura)
            String nomeCampoValidatore = this.getParametri().getParametriMap().get("campoValidatore"); //nomeCampoValidatore è il cross campo per validare (es. tipologiaErogatore)
            String valoreCampoValidatore = (String) recordDtoGenerico.getCampo(nomeCampoValidatore); //valore del campo usato per la validazione
            String primoParametro = this.getParametri().getParametriMap().get("primoParametro");
            String secondoParametro = this.getParametri().getParametriMap().get("secondoParametro");

            if ((!valoreCampoValidatore.equals(primoParametro)) && (!valoreCampoValidatore.equals(secondoParametro))) {
                if (daValidare != null) {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                } else {
                    listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                            //.withDescrizione("Il valore del campo " + nomeCampo + " non può essere nullo")

                }
            } else {
                if (daValidare == null) {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                } else {
                    listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Non è possibile validare la regola per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
