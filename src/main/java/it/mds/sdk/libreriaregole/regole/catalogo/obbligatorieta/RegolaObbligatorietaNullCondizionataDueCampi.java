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
@XmlDiscriminatorValue("regolaObbligatorietaNullCondizionataDueCampi")
public class RegolaObbligatorietaNullCondizionataDueCampi extends RegolaGenerica {

    public RegolaObbligatorietaNullCondizionataDueCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il campo in input non sia presente o uguale a blanks se altri due campi del DTO assumono
     * determinati valori.
     * <p>
     * Esempio : nel caso di SALM, il campo EVALINFO_CONCLUSION non deve essere presente o uguale a blanks
     * se PROGID = PNR e se ORIGCOUNTRY =! IT
     *
     * @param nomeCampo         il nome del campo da validare
     * @param recordDtoGenerico DTO del record di un flusso
     * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

            String nomeCampoCondizionante1 = this.getParametri().getParametriMap().get("nomeCampoCondizionante1"); //nel nostro esempio il campo PROGID
            String valoreCampoCondizionante1 = (String) recordDtoGenerico.getCampo(nomeCampoCondizionante1); //valore del campo PROGID
            String nomeCampoCondizionante2 = this.getParametri().getParametriMap().get("nomeCampoCondizionante2"); //nel nostro esempio il campo ORIGCOUNTRY
            String valoreCampoCondizionante2 = (String) recordDtoGenerico.getCampo(nomeCampoCondizionante2); //valore del campo ORIGCOUNTRY

            String parametroCampoCondizionante1 = this.getParametri().getParametriMap().get("parametroCampoCondizionante1");// il valore con cui devo confrontare PROGID
            String parametroCampoCondizionante2 = this.getParametri().getParametriMap().get("parametroCampoCondizionante2");// il valore con cui devo confrontare ORIGCOUNTRY

            if (valoreCampoCondizionante1.equals(parametroCampoCondizionante1) && !valoreCampoCondizionante2.equals(parametroCampoCondizionante2)
                    && campoDaValidare != null && !campoDaValidare.isBlank()) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola ObbligatorietaNullCondizionataDueCampi del campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola ObbligatorietaNullCondizionataDueCampi del campo " + nomeCampo);
        }
        return listaEsiti;
    }

}