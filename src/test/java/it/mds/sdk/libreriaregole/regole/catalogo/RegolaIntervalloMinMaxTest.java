/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaDirettaD39;
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
class RegolaIntervalloMinMaxTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void testValoreCompresoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("parametroMin", "0");
        parametri.put("parametroMax", "100");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIntervalloMinMax regola = new RegolaIntervalloMinMax("regolaIntervalloMinMax", "BR182", "Codice BR182", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(3.01);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoreSopraMaxKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("parametroMin", "0");
        parametri.put("parametroMax", "100");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIntervalloMinMax regola = new RegolaIntervalloMinMax("regolaIntervalloMinMax", "BR182", "Codice BR182", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(103.01);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR182", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testValoreSottoMinKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("parametroMin", "0");
        parametri.put("parametroMax", "100");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIntervalloMinMax regola = new RegolaIntervalloMinMax("regolaIntervalloMinMax", "BR182", "Codice BR182", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(-0.01);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR182", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testCampoNonValorizzatoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("parametroMin", "0");
        parametri.put("parametroMax", "100");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIntervalloMinMax regola = new RegolaIntervalloMinMax("regolaIntervalloMinMax", "BR182", "Codice BR182", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaIntervalloMinMax regola = new RegolaIntervalloMinMax();
        assertTrue(regola instanceof RegolaIntervalloMinMax);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaIntervalloMinMax regola = new RegolaIntervalloMinMax("regolaIntervalloMinMax", "BR182", "Codice BR182", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}