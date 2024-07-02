/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RegolaDisuguaglianzaListaCampiTest {


    RegolaDisuguaglianzaListaCampi sut;

    RecordDtoGenerico recordDtoGenerico;

    @BeforeEach
    void init() {


        Parametri params = new Parametri();
        params.setParametriMap(
                Map.of("listaCampiDaConfrontare", "codiceRegioneDomicilio|codiceComuneDomicilio|codiceAslDomicilio",
                        "listaCampiConfronto", "codiceRegioneResidenza|codiceComuneResidenza|codiceAslResidenza"
                )
        );
        sut=new RegolaDisuguaglianzaListaCampi("nome", "2065", "errore", params);
        recordDtoGenerico = mock(RecordDtoGenerico.class);
    }

    @Test
    void valida() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn("160").when(recordDtoGenerico).getCampo("codiceRegioneDomicilio");
        doReturn("074017").when(recordDtoGenerico).getCampo("codiceComuneDomicilio");
        doReturn("106").when(recordDtoGenerico).getCampo("codiceAslDomicilio");
        doReturn("160").when(recordDtoGenerico).getCampo("codiceRegioneResidenza");
        doReturn("074017").when(recordDtoGenerico).getCampo("codiceComuneResidenza");
        doReturn("106").when(recordDtoGenerico).getCampo("codiceAslResidenza");

        List<Esito> esiti = sut.valida("codiceRegioneDomicilio", recordDtoGenerico);

        System.out.println(esiti);
        for(Esito e: esiti){
            assertTrue(CollectionUtils.isEmpty(e.getErroriValidazione()));
            assertTrue(StringUtils.isEmpty(e.getValoreScarto()));
            assertTrue(e.isValoreEsito());
        }
    }
}