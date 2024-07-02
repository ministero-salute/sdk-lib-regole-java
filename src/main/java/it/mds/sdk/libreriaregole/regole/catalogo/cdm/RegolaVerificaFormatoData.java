/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.cdm;

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
import java.util.Collections;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaVerificaFormatoData")
public class RegolaVerificaFormatoData extends RegolaGenerica {

    public RegolaVerificaFormatoData(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che la data di esecuzione , cioe la data corrente di validazione, sia posteriore all'ultimo giorno del mese/anno di riferimento presenti nel DTO
     *
     * @param nomeCampo
     * @param recordDtoGenerico
     * @return
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            String meseRiferimento = (String) recordDtoGenerico.getCampo(nomeCampo); // valore di mese riferimento => meseStipulaContratto
            String nomeCampoCondizionanteUno = this.getParametri().getParametriMap().get("nomeCampoCondizionanteUno"); //anno riferimento => annoStipulaContratto
            String annoString = (String) recordDtoGenerico.getCampo(nomeCampoCondizionanteUno); //valore anno
            String nomeCampoCondizionanteDue = this.getParametri().getParametriMap().get("nomeCampoCondizionanteDue"); //giorno => giornoStipulaContratto
            String giornoString = (String) recordDtoGenerico.getCampo(nomeCampoCondizionanteDue); //valore giorno
            String regexMatch = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";

            if (annoString == null || meseRiferimento == null || giornoString == null) {
                return Collections.singletonList(creaEsitoKO(nomeCampo, "999", "Non è possibile validare la regola verifica formato data perchè anno,mese o giorno non presenti nel record"));
            }

            String dataConcatenata = annoString+"-"+meseRiferimento+"-"+giornoString;
            if(dataConcatenata.matches(regexMatch))
            {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }
            else {
                return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Non è possibile validare laregolaDataCorrentePosterioreAnnoMeseRif del campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola di data posteriore del campo " + nomeCampo);
        }
    }
}
