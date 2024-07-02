/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaCondizionataDa1aNCampi")
public class RegolaObbligatorietaCondizionataNCampi extends RegolaGenerica {

    static final String LISTA_CAMPI_PARAM = "listaCampi";
    static final String SEPARATOR = "\\|";

    public RegolaObbligatorietaCondizionataNCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che il campo in input sia valorizzato e diverso da due parametri predefiniti
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String daValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String[] nomiCampi = Optional.ofNullable(getParametri()).map(Parametri::getParametriMap).map(m -> m.get(LISTA_CAMPI_PARAM))
                    .map(campi -> campi.split(SEPARATOR)).orElse(null);

            boolean obbligatorio = false;
            if (nomiCampi != null) {
                for (int i = 0; !obbligatorio && i < nomiCampi.length; i++) {
                    obbligatorio = StringUtils.isNotBlank((String) recordDtoGenerico.getCampo(nomiCampi[i]));
                }
            }

            if (obbligatorio && StringUtils.isBlank(daValidare)) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            log.error("Non è possibile validare la regola per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
