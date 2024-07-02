/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

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
@XmlDiscriminatorValue("regolaDiversitaValoreParametro")
public class RegolaDiversitaValoreParametro extends RegolaGenerica {

    public RegolaDiversitaValoreParametro(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il valore del campo in input sia diverso dal parametro prestabilito (es: regione trasmittente)
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            //nel caso della regola AVN, daValidare è il campo codRegione
            String daValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
            //inseriamo adesso il parametro da inputare dall'esterno per verifica (nel nostro caso riguarda la regione trasmittente)
            String parametroInput = this.getParametri().getParametriMap().get("parametroInput");

            if (!daValidare.equals(parametroInput)) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                        //.withDescrizione("Il campo : " + nomeCampo + " non coincide con il parametro : " + parametroInput)

            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola DiversitaValoreParametro per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola DiversitaValoreParametro per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
