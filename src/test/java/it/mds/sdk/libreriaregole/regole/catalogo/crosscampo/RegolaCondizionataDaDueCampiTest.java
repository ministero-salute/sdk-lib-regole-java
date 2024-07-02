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
class RegolaCondizionataDaDueCampiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroCampoValidare", "BIN");
        parametri.put("parametroCampoDipendente1", "AT06A");
        parametri.put("parametroCampoDipendente2", "MCG");

        parametri.put("campoDipendente1", "campoDipendente1");
        parametri.put("campoDipendente2", "campoDipendente2");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCondizionataDaDueCampi regola = new RegolaCondizionataDaDueCampi("regolaCondizionataDaDueCampi", "BR056", "Codice BR056", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("BIN");
        Mockito.when(recordMockito.getCampo("campoDipendente1")).thenReturn("AT06A");
        Mockito.when(recordMockito.getCampo("campoDipendente2")).thenReturn("MCG diverso");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroCampoValidare", "BIN");
        parametri.put("parametroCampoDipendente1", "AT06A");
        parametri.put("parametroCampoDipendente2", "MCG");

        parametri.put("campoDipendente1", "campoDipendente1");
        parametri.put("campoDipendente2", "campoDipendente2");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCondizionataDaDueCampi regola = new RegolaCondizionataDaDueCampi("regolaCondizionataDaDueCampi", "BR056", "Codice BR056", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("BIN");
        Mockito.when(recordMockito.getCampo("campoDipendente1")).thenReturn("non è AT06A");
        Mockito.when(recordMockito.getCampo("campoDipendente2")).thenReturn("MCG diverso");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR056", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCampoNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroCampoValidare", "BIN");
        parametri.put("parametroCampoDipendente1", "AT06A");
        parametri.put("parametroCampoDipendente2", "MCG");

        parametri.put("campoDipendente1", "campoDipendente1");
        parametri.put("campoDipendente2", "campoDipendente2");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCondizionataDaDueCampi regola = new RegolaCondizionataDaDueCampi("regolaCondizionataDaDueCampi", "BR056", "Codice BR056", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("campoDipendente1")).thenReturn("AT06A");
        Mockito.when(recordMockito.getCampo("campoDipendente2")).thenReturn("MCG diverso");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void costruttoreVuoto() {
        RegolaCondizionataDaDueCampi regola = new RegolaCondizionataDaDueCampi();
        assertTrue(regola instanceof RegolaCondizionataDaDueCampi);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCondizionataDaDueCampi regola = new RegolaCondizionataDaDueCampi("RegolaCondizionataDaDueCampi", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

}