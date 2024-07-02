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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaListaValoriAmmessiCondizionataDueCampiUguali")
public class RegolaListaValoriAmmessiCondizionataDueCampiUguali extends RegolaGenerica {

    public RegolaListaValoriAmmessiCondizionataDueCampiUguali(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che la stringa o l'intero in ingresso, sia contenuto in un dominio predefinito (parametro listaValoriAmmessi)
     * se due campi del DTO sono uguali
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String valoreCampoDaValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));

            String nomeCampoCondizionante1 = this.getParametri().getParametriMap().get("nomeCampoCondizionante1");//campo1 del DTO
            String valoreCampoCondizionante1 = String.valueOf(recordDtoGenerico.getCampo(nomeCampoCondizionante1)); //valore del campo1 del DTO

            String nomeCampoCondizionante2 = this.getParametri().getParametriMap().get("nomeCampoCondizionante2");//campo2 del DTO
            String valoreCampoCondizionante2 = String.valueOf(recordDtoGenerico.getCampo(nomeCampoCondizionante2)); //valore2 del campo del DTO


            //Lista da 1 a N dei valori ammessi per il campo da validare
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if (valoreCampoCondizionante1.equals(valoreCampoCondizionante2) && !listaValori.contains(valoreCampoDaValidare)) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata due campi uguali " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata due campi uguali " + nomeCampo);
        }
        return listaEsiti;

    }
}