/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegolaObbligatorietaCampoDiversoDaParamTest {

    @Mock
    RecordDtoGenerico recordMockito;


    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaCampoDiversoDaParam regola = new RegolaObbligatorietaCampoDiversoDaParam();
        assertTrue(regola instanceof RegolaObbligatorietaCampoDiversoDaParam);
    }

    @Test
    void valida() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("primoParametroInputDaConfrontare", "J040A");
        parametri.put("secondoParametroInputDaConfrontare", "J029A");
        parametri.put("campoValidatore","EVALCODE");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCampoDiversoDaParam regola = new RegolaObbligatorietaCampoDiversoDaParam("regolaObbligatorietaCampoDiversoDaParam", "BR174","descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("EVALLIMITTYPE")).thenReturn("J030A");
        Mockito.when(recordMockito.getCampo("EVALCODE")).thenReturn("J029B");
        List<Esito> result = regola.valida("EVALLIMITTYPE", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaCampoDiversoDaParam regola = new RegolaObbligatorietaCampoDiversoDaParam("regolaObbligatorietaCampoDiversoDaParam", "BR174","descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}