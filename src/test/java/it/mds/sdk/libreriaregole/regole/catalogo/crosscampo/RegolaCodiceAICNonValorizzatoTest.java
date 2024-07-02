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
class RegolaCodiceAICNonValorizzatoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaCodiceAICNonValorizzato regola = new RegolaCodiceAICNonValorizzato();
        assertTrue(regola instanceof RegolaCodiceAICNonValorizzato);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCodiceAICNonValorizzato regola = new RegolaCodiceAICNonValorizzato("RegolaCodiceAICNonValorizzato", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void codiceAICPienoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataSomministrazione", "dataSomministrazione");
        parametri.put("statoEsteroSomministrazione", "statoEsteroSomministrazione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCodiceAICNonValorizzato regola = new RegolaCodiceAICNonValorizzato("RegolaCodiceAICNonValorizzato", "1990", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("campoPieno");
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2019-05-05");
        Mockito.when(recordMockito.getCampo("statoEsteroSomministrazione")).thenReturn("FR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataSomministrazione", "dataSomministrazione");
        parametri.put("statoEsteroSomministrazione", "statoEsteroSomministrazione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCodiceAICNonValorizzato regola = new RegolaCodiceAICNonValorizzato("RegolaCodiceAICNonValorizzato", "1990", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2019-05-05");
        Mockito.when(recordMockito.getCampo("statoEsteroSomministrazione")).thenReturn("FR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataSomministrazione", "dataSomministrazione");
        parametri.put("statoEsteroSomministrazione", "statoEsteroSomministrazione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCodiceAICNonValorizzato regola = new RegolaCodiceAICNonValorizzato("RegolaCodiceAICNonValorizzato", "1990", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2019-09-05");
        Mockito.when(recordMockito.getCampo("statoEsteroSomministrazione")).thenReturn("IT");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("1990", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testNullPointerKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataSomministrazione", "dataSomministrazione");
        parametri.put("statoEsteroSomministrazione", "statoEsteroSomministrazione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCodiceAICNonValorizzato regola = new RegolaCodiceAICNonValorizzato("RegolaCodiceAICNonValorizzato", "1990", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("statoEsteroSomministrazione")).thenReturn("IT");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("1990", e.getErroriValidazione().get(0).getCodice());
        }
    }
}