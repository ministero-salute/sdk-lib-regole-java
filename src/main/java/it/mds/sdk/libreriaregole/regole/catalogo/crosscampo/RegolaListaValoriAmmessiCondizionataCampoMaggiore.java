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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaListaValoriAmmessiCondizionataCampoMaggiore")
public class RegolaListaValoriAmmessiCondizionataCampoMaggiore extends RegolaGenerica {

    public RegolaListaValoriAmmessiCondizionataCampoMaggiore(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se EVALCODE è uguale a J003A o J038A allora  RESVAL > EVALHIGHLIMIT, se EVALHIGHLIMIT è valorizzato.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] -  BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            // resVal
            Double valoreCampoDaValidare = (Double) recordDtoGenerico.getCampo(nomeCampo);

            //evalHighLimit
            Double valoreCampoCondizionante = (Double) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante")
            ); //valore del campo del DTO

            // parametro con cui devo confrontare il valoreCampoCondizionante
            //evalCode
            String parametroCampoCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("parametroCampoCondizionante"));

            //Lista da 1 a N dei valori ammessi per il campo da validare ( per la br127 sono i valori MS)
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if ( valoreCampoCondizionante != null &&
                    valoreCampoDaValidare != null &&
                    listaValori.contains(parametroCampoCondizionante) &&
                    (valoreCampoDaValidare <= valoreCampoCondizionante)
            ) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo);
        }

    }
}