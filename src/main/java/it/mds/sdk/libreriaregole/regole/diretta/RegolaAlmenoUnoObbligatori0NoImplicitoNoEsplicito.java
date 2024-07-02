/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.diretta;

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
@XmlDiscriminatorValue("regolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito")
public class RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito extends RegolaGenerica {

    public RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Se il record non rispetta l'anonimato implicito (vedi definizione codice 1 sheet BR - Definizioni) e non rispetta
     * l'anonimato esplicito (vedi definizione codice 1 sheet BR - Definizioni)
     * allora almeno uno dei due campi cod_asl_ass e stat_est deve essere valorizzato (presente e diverso da blanks)
     *
     * @param nomeCampo
     * @param recordDtoGenerico
     * @return
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        List<Esito> listaEsiti = new ArrayList<>();
        try {
            // aslAssistito
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String statoEsteroDiResidenza = (String) recordDtoGenerico.getCampo("statoEsteroDiResidenza");

            RegolaAnonimatoImplicito regolaAnonimatoImplicito = new RegolaAnonimatoImplicito("", "", "", new Parametri());
            List<Esito> esitiImplicito = regolaAnonimatoImplicito.valida(nomeCampo, recordDtoGenerico);
            boolean esitoAnonimatoImplicito = esitiImplicito.get(0).isValoreEsito();

            RegolaAnonimatoEsplicito regolaAnonimatoEsplicito = new RegolaAnonimatoEsplicito("", "", "", new Parametri());
            List<Esito> esitiEsplicito = regolaAnonimatoEsplicito.valida(nomeCampo, recordDtoGenerico);
            boolean esitoAnonimatoEsplicito = esitiEsplicito.get(0).isValoreEsito();

            log.debug("esitoAnonimatoImplicito: {}, esitoAnonimatoEsplicito: {}, campoDaValidare :{} ",
                    esitoAnonimatoImplicito, esitoAnonimatoEsplicito, campoDaValidare);

            if (!esitoAnonimatoImplicito && !esitoAnonimatoEsplicito && campoDaValidare == null && statoEsteroDiResidenza == null) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}
