/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.lunghezza;

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
public class RegolaIntervalloLunghezzaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaIntervalloLunghezza regolaIntervalloLunghezza = new RegolaIntervalloLunghezza();
        assertTrue(regolaIntervalloLunghezza instanceof RegolaIntervalloLunghezza);
    }

    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("minLunghezza", "6");
        parametri.put("maxLunghezza", "8");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIntervalloLunghezza regola = new RegolaIntervalloLunghezza("intervalloLunghezza", "E3190", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("SETTECR");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOMinoreSoglia() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("minLunghezza", "6");
        parametri.put("maxLunghezza", "8");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIntervalloLunghezza regola = new RegolaIntervalloLunghezza("intervalloLunghezza", "E3190", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("tre");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E3190", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaKOMaggioreSoglia() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("minLunghezza", "6");
        parametri.put("maxLunghezza", "8");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIntervalloLunghezza regola = new RegolaIntervalloLunghezza("intervalloLunghezza", "E3190", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("diecicarat");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E3190", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testLunghezzaException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("minLunghezza", "testSbagliato");
        parametri.put("maxLunghezza", "8");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIntervalloLunghezza regola = new RegolaIntervalloLunghezza("intervalloLunghezza", "E3190", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("settecr");

        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }

    @Test
    void validaOKSeNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("minLunghezza", "6");
        parametri.put("maxLunghezza", "8");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIntervalloLunghezza regola = new RegolaIntervalloLunghezza("intervalloLunghezza", "E3190", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
}