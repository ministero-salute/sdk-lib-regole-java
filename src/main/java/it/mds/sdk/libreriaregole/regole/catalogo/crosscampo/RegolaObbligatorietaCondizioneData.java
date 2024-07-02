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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaCondizioneData")
public class RegolaObbligatorietaCondizioneData extends RegolaGenerica {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RegolaObbligatorietaCondizioneData(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il campo in input sia valorizzato se il campo data del DTO è successivo ad un certo valore
     *
     * @param nomeCampo         il nome del campo da validare
     * @param recordDtoGenerico DTO del record di un flusso
     * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        String nomeCampoCondizionante = null;
        try {

            nomeCampoCondizionante = this.getParametri().getParametriMap().get("nomeCampoCondizionante");//nel nostro esempio dataSomministrazione
            String valoreCampoCondizionante = (String) recordDtoGenerico.getCampo(nomeCampo);
            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");// il valore con cui devo confrontare dataSomministrazione

            if ((valoreCampoCondizionante != null) && (parametroCampoCondizionante != null)) {
                LocalDate dataCampoCondizionante = LocalDate.parse(valoreCampoCondizionante, formatter);
                LocalDate dataParametroCampo = LocalDate.parse(parametroCampoCondizionante, formatter);
                if ((dataCampoCondizionante.isAfter(dataParametroCampo)) && recordDtoGenerico.getCampo(nomeCampo) != null) {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                } else {
                    listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));

                }
            } else {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                       // .withDescrizione("Il campo  " + nomeCampo + "è nullo ")

            }
        } catch (InvocationTargetException | NoSuchMethodException |
                IllegalAccessException | DateTimeParseException e) {
            log.error("Impossibile validare Obbligatorietà condizionata del campo " + nomeCampo + " al campo " + nomeCampoCondizionante, e);
            throw new ValidazioneImpossibileException("Impossibile validare Obbligatorietà condizionata del campo " + nomeCampo + " al campo " + nomeCampoCondizionante);
        }
        return listaEsiti;
    }
}
