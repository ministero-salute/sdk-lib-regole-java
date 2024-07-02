/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaCondizionataDa1aNCampiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaCondizionataDa1aNCampi regola = new RegolaCondizionataDa1aNCampi();
        assertTrue(regola instanceof RegolaCondizionataDa1aNCampi);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCondizionataDa1aNCampi regola = new RegolaCondizionataDa1aNCampi("RegolaCondizionataDa1aNCampi", "AAA", "Codice", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void campoDaValidareValorizzatoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampi", "campo1|campo2");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCondizionataDa1aNCampi regola = new RegolaCondizionataDa1aNCampi("RegolaCondizionataDa1aNCampi", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("GG");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampi", "campo1|campo2");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCondizionataDa1aNCampi regola = new RegolaCondizionataDa1aNCampi("RegolaCondizionataDa1aNCampi", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("campo1")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("campo2")).thenReturn(null);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testK0() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampi", "campo1|campo2");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCondizionataDa1aNCampi regola = new RegolaCondizionataDa1aNCampi("RegolaCondizionataDa1aNCampi", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("campo1")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("campo2")).thenReturn("1");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("AAA", e.getErroriValidazione().get(0).getCodice());
        }
    }
}