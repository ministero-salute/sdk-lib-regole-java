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
class RegolaMaggioranzaCrossCampoStringTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaMaggioranzaCrossCampoString regola = new RegolaMaggioranzaCrossCampoString();
        assertTrue(regola instanceof RegolaMaggioranzaCrossCampoString);
    }

    @Test
    void validaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaMaggioranzaCrossCampoString regola = new RegolaMaggioranzaCrossCampoString("RegolaVerificaEtaString", "3941", "Codice 3941", parametriTest);
        Mockito.when(recordMockito.getCampo("annoNascita")).thenReturn("1996");
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn("2022");

        List<Esito> result = regola.valida("annoNascita", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaMaggioranzaCrossCampoString regola = new RegolaMaggioranzaCrossCampoString("RegolaVerificaEtaString", "3941", "Codice 3941", parametriTest);
        Mockito.when(recordMockito.getCampo("annoNascita")).thenReturn("2023");
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn("2022");

        List<Esito> result = regola.valida("annoNascita", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaMaggioranzaCrossCampoString regola = new RegolaMaggioranzaCrossCampoString("RegolaMaggioranzaCrossCampoString", "3941", "Codice 3941", parametriTest);


        Mockito.when(recordMockito.getCampo("annoNascita")).thenThrow(new IllegalAccessException());
        //    Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn("2022");
        // Mockito.when(recordMockito.getCampo("anno")).thenReturn(1996);

        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("annoNascita", recordMockito));

    }

}