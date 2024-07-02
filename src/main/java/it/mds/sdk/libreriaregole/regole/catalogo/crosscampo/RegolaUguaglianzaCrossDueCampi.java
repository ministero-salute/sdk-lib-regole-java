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
@XmlDiscriminatorValue("regolaUguaglianzaCrossDueCampi")
public class RegolaUguaglianzaCrossDueCampi extends RegolaGenerica {

    public RegolaUguaglianzaCrossDueCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il valore del campo in input sia uguale al valore di almeno uno di altri due campi del DTO.
     * si assume che, sia il campo in input e che almeno uno dei due campi del DTO, siano valorizzati (non null)
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        try {
            //daValidare rappresenta nel caso di AVN il codiceRegione somministrazione
            String daValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
            //comparante1 e comparante2 sono rispettivamente i 2 campi del DTO con cui confrontare il campo daValidare
            String primoCampoValidatore = this.getParametri().getParametriMap().get("primoCampoValidatore");
            String comparante1 = String.valueOf(recordDtoGenerico.getCampo(primoCampoValidatore));
            String secondoCampoValidatore = this.getParametri().getParametriMap().get("secondoCampoValidatore");
            String comparante2 = String.valueOf(recordDtoGenerico.getCampo(secondoCampoValidatore));

            if (daValidare.equals(comparante1) || daValidare.equals(comparante2)) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                        //.withDescrizione("Il campo : " + nomeCampo + " è diverso sia da  : " + comparante1 + " che da " + comparante2)

            }


        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola UguaglianzaCrossCampo per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola UguaglianzaCrossCampo per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
