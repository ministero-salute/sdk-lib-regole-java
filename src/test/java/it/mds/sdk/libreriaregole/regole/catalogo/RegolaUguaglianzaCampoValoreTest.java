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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaUguaglianzaCampoValoreTest {
    @Mock
    RecordDtoGenerico recordMockito;


    @Test
    void validaCostruttore() {
        RegolaUguaglianzaCampoValoreString regola = new RegolaUguaglianzaCampoValoreString();
        assertTrue(regola instanceof RegolaUguaglianzaCampoValoreString);
    }

    @Test
    void validaCostruttore2() {
        RegolaUguaglianzaCampoValoreInteger regola = new RegolaUguaglianzaCampoValoreInteger();
        assertTrue(regola instanceof RegolaUguaglianzaCampoValoreInteger);
    }

    @Test
    void testUguaglianzaInteriValidaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "3");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCampoValoreInteger regola =
                new RegolaUguaglianzaCampoValoreInteger("uguaglianza", "101", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(3);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testUguaglianzaInteriValidaKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "4");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaUguaglianzaCampoValoreInteger regola =
                new RegolaUguaglianzaCampoValoreInteger("uguaglianza", "101", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(3);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("101", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testUguaglianzaStringheValidaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "STRINGA_OK");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaUguaglianzaCampoValoreString regola =
                new RegolaUguaglianzaCampoValoreString("uguaglianza", "101", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("stringa")).thenReturn("STRINGA_OK");
        List<Esito> result = regola.valida("stringa", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testUguaglianzaStringheValidaKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "STRINGA_OK");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaUguaglianzaCampoValoreString regola =
                new RegolaUguaglianzaCampoValoreString("uguaglianza", "200", "descrizioneErrore", parametriTest);

        RecordDtoGenerico recordMockito = Mockito.mock(RecordDtoGenerico.class);
        Mockito.when(recordMockito.getCampo("stringa")).thenReturn("STRINGA_DIVERSA_DA_OK");
        List<Esito> result = regola.valida("stringa", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("200", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaUguaglianzaCampoValoreInteger regola = new RegolaUguaglianzaCampoValoreInteger("uguaglianza", "101", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }


    @Test
    void testExceptionString() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaUguaglianzaCampoValoreString regola = new RegolaUguaglianzaCampoValoreString("uguaglianza", "200", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}