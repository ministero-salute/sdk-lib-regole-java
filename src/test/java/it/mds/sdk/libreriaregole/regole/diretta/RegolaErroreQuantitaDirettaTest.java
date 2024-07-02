/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.diretta;

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
class RegolaErroreQuantitaDirettaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaErroreQuantitaDiretta regola = new RegolaErroreQuantitaDiretta();
        assertTrue(regola instanceof RegolaErroreQuantitaDiretta);
    }

    @Test
    void targaturaSoloZeriOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dividendo", "quantita");
        parametri.put("divisore", "fattoreDiConversione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaErroreQuantitaDiretta regola = new RegolaErroreQuantitaDiretta("RegolaErroreQuantitaDiretta", "B13", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("targatura")).thenReturn("000000000");
        List<Esito> result = regola.valida("targatura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void rapportoUgualeAUnoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dividendo", "quantita");
        parametri.put("divisore", "fattoreDiConversione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaErroreQuantitaDiretta regola = new RegolaErroreQuantitaDiretta("RegolaErroreQuantitaDiretta", "B13", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("targatura")).thenReturn("000000001");
        Mockito.when(recordMockito.getCampo("quantita")).thenReturn("10");
        Mockito.when(recordMockito.getCampo("fattoreDiConversione")).thenReturn("10");
        List<Esito> result = regola.valida("targatura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void rapportoDiversoDaUnoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dividendo", "quantita");
        parametri.put("divisore", "fattoreDiConversione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaErroreQuantitaDiretta regola = new RegolaErroreQuantitaDiretta("RegolaErroreQuantitaDiretta", "B13", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("targatura")).thenReturn("000000001");
        Mockito.when(recordMockito.getCampo("quantita")).thenReturn("10");
        Mockito.when(recordMockito.getCampo("fattoreDiConversione")).thenReturn("7");
        List<Esito> result = regola.valida("targatura", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B13", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void targaturaNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dividendo", "quantita");
        parametri.put("divisore", "fattoreDiConversione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaErroreQuantitaDiretta regola = new RegolaErroreQuantitaDiretta("RegolaErroreQuantitaDiretta", "B13", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("targatura")).thenReturn(null);
        List<Esito> result = regola.valida("targatura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void divisoreNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dividendo", "quantita");
        parametri.put("divisore", "fattoreDiConversione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaErroreQuantitaDiretta regola = new RegolaErroreQuantitaDiretta("RegolaErroreQuantitaDiretta", "B13", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("targatura")).thenReturn("000000002");
        Mockito.when(recordMockito.getCampo("quantita")).thenReturn("10");
        Mockito.when(recordMockito.getCampo("fattoreDiConversione")).thenReturn("");
        List<Esito> result = regola.valida("targatura", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("999", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaErroreQuantitaDiretta regola = new RegolaErroreQuantitaDiretta("RegolaErroreQuantitaDiretta", "B13", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("targatura");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("targatura", recordMockito));
    }
}