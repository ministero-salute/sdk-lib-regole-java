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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCodiceAIC")
public class RegolaCodiceAIC extends RegolaGenerica {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RegolaCodiceAIC(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * La regola in questione procede allo scarto del campo in input "nomeCampo"
     * se le condizioni seguenti risultano verificate contemporaneamente:
     * 1) valore nomeCampo nullo;
     * 2) data di somministrazione > 01/07/2019;
     * 3) stato estero di somministrazione pari a null o pari a IT.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        List<Esito> listaEsiti = new ArrayList<>();

        try {
            String daValidare = (String) (recordDtoGenerico.getCampo(nomeCampo));
            String dataSomministrazione = (String) (recordDtoGenerico.getCampo("dataSomministrazione"));
            String statoEstero = (String) (recordDtoGenerico.getCampo("statoEstero"));

            String statoEsteroPar = this.getParametri().getParametriMap().get("statoEsteroPar");
            String dataParametro = this.getParametri().getParametriMap().get("dataParametro");

            //conversione da String a LocalDate del campo dataSomministrazione
            LocalDate dataSomministrazioneValidatore = LocalDate.parse(dataSomministrazione, formatter);
            //conversione dataParametro in LocalDate
            LocalDate dataParametroVal = LocalDate.parse(dataParametro, formatter);

            if ((daValidare == null) && (dataSomministrazioneValidatore.isAfter(dataParametroVal)) && (statoEstero == null || statoEstero.equals(statoEsteroPar))) {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                       // .withDescrizione("Il campo : " + nomeCampo + " non può essere validato ")

            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola RegolaCodiceAIC per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaCodiceAIC per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}
