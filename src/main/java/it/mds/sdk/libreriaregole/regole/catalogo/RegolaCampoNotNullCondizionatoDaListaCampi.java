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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCampoNotNullCondizionatoDaListaCampi")
public class RegolaCampoNotNullCondizionatoDaListaCampi extends RegolaGenerica {

    public RegolaCampoNotNullCondizionatoDaListaCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che se il campo da validare  è  valorizzato allora lista di campi in input  deve essere valorizzata
     * Esempio RegioneDomicilio,comuneDomicilio,ASL Domicilio o sono tutti valorizzati o tutti null.
     * Non può essercene valorizzati alcuni e altri a null
     *
     * @param nomeCampo il nome del campo da validare
     * @param recordDtoGenerico DTO del record di un flusso
     * @return ritorna una lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            List<String> listaCampi = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaCampi").split("\\|")).collect(Collectors.toList());

            List<String> valoriCampiDto = new ArrayList<>();

        if(campoDaValidare != null && !campoDaValidare.isEmpty()) {
                //per ogni campo della lista recupero il suo valore
                for (String campoDaConcatenare : listaCampi) {
                    String valoreCampoCoonfronto = (String) recordDtoGenerico.getCampo(campoDaConcatenare);
                    if (valoreCampoCoonfronto != null && !valoreCampoCoonfronto.isEmpty()) {
                        valoriCampiDto.add(valoreCampoCoonfronto);
                    }
                }

                if (!valoriCampiDto.isEmpty() && valoriCampiDto.size() == listaCampi.size()) {
                    return List.of(creaEsitoOk(nomeCampo));
                }

                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }

        return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException  e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare RegolaCampoNullCondizionatoDaListaCampi del campo " + nomeCampo );
        }
    }
}
