/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaValoreMinoreDiUnCampoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void testValoreMinoreOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaValoreMinoreDiUnCampo regola = new RegolaValoreMinoreDiUnCampo("regolaValoreMinoreDiUnCampo", "BR016", "Codice BR182", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(3.01);
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn(5.1);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoreNonPresenteOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaValoreMinoreDiUnCampo regola = new RegolaValoreMinoreDiUnCampo("regolaValoreMinoreDiUnCampo", "BR016", "Codice BR182", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn(5.1);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoreMaggioreKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaValoreMinoreDiUnCampo regola = new RegolaValoreMinoreDiUnCampo("regolaValoreMinoreDiUnCampo", "BR016", "Codice BR182", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(3.01);
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn(2.1);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR016", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaValoreMinoreDiUnCampo regola = new RegolaValoreMinoreDiUnCampo();
        assertTrue(regola instanceof RegolaValoreMinoreDiUnCampo);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaValoreMinoreDiUnCampo regola = new RegolaValoreMinoreDiUnCampo("TEST", "TEST", "TEST",null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

}