/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.cdm;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaB102ErroreQuantita")
public class RegolaB102ErroreQuantita extends RegolaGenerica {

    public RegolaB102ErroreQuantita(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Se il campo anno >=2016 e se CND è presente nella tabella Allegato, mediante concatenazione di num_rep#tipo_dispositivo,
     * allora il campo qta_agg deve essere maggiore o uguale a qta_contr
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
            //quantitaAggiudicata
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
            String anno = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("annoStipulaContratto"));
            String quantitaContrattualizzata = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("quantitaContrattualizzata"));
            String separatore = this.getParametri().getParametriMap().get("separatore");
            Integer annoConfronto = Integer.valueOf(this.getParametri().getParametriMap().get("annoConfronto"));

            List<String> listaCampiDaConcatenare = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaCampiDaConcatenare").split("\\|")).collect(Collectors.toList());

            String campiDtoConcatenati = "";

            //per ogni campo recupero il suo valore e lo concateno con il separatore
            StringJoiner joiner = new StringJoiner(separatore);
            for (String campoDaConcatenare : listaCampiDaConcatenare) {
                joiner.add((String) recordDtoGenerico.getCampo(campoDaConcatenare));
                campiDtoConcatenati = joiner.toString();
            }

            if (campoDaValidare == null) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            Integer annoDaConfrontare = 0;
            if (anno != null) {
                annoDaConfrontare = Integer.valueOf(anno);
            }

            Double qtaAggiunta = 0.0;
            Double qtaContrattualizzata = 0.0;

            if (campoDaValidare != null && quantitaContrattualizzata != null) {
                qtaAggiunta = Double.parseDouble(campoDaValidare);
                qtaContrattualizzata = Double.parseDouble(quantitaContrattualizzata);
            }

            if (annoDaConfrontare < annoConfronto) {
                return List.of(creaEsitoOk(nomeCampo));
            } else {
                List<RecordAnagrafica> listaValori = getGestoreAnagrafica().richiediAnagrafica(nomeTabella, campiDtoConcatenati, false).getRecordsAnagrafica();
                //recupero il dominio dei valori validi
                List<String> listaDominio;

                listaDominio = listaValori
                        .stream()
                        .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                                || (ra.getValidoDa() == null && ra.getValidoA() == null))
                        .map(RecordAnagrafica::getDato)
                        .collect(Collectors.toList());

                if (listaDominio.contains(campiDtoConcatenati)) {
                    if (qtaAggiunta >= qtaContrattualizzata) {
                        return Collections.singletonList(creaEsitoOk(nomeCampo));
                    } else {
                        return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                } else {
                    return Collections.singletonList(creaEsitoOk(nomeCampo));

                }
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola per il campo " + nomeCampo);
        }
    }

    public GestoreAnagrafica getGestoreAnagrafica() {
        return new GestoreAnagrafica();
    }
}
