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
class RegolaDiversitaValoreCampoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDiversitaValoreCampo regola = new RegolaDiversitaValoreCampo();
        assertTrue(regola instanceof RegolaDiversitaValoreCampo);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDiversitaValoreCampo regola = new RegolaDiversitaValoreCampo("RegolaDiversitaValoreCampo", "AAA", "Codice", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void campoDaValidareValorizzatoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroValidatore", "PAR");
        parametri.put("nomeCampoCoerente", "nomeCampoCoerente");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCampo regola = new RegolaDiversitaValoreCampo("RegolaDiversitaValoreCampo", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("GG");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoDaValidareValorizzatoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroValidatore", "PAR");
        parametri.put("nomeCampoCoerente", "nomeCampoCoerente");
        parametri.put("listaValoriIncoerenti", "A1|A2");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCampo regola = new RegolaDiversitaValoreCampo("RegolaDiversitaValoreCampo", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("PAR");
        Mockito.when(recordMockito.getCampo("nomeCampoCoerente")).thenReturn("A1");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("AAA", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void campoDaValidareValorizzato2OK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroValidatore", "PAR");
        parametri.put("nomeCampoCoerente", "nomeCampoCoerente");
        parametri.put("listaValoriIncoerenti", "A1|A2");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCampo regola = new RegolaDiversitaValoreCampo("RegolaDiversitaValoreCampo", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("PAR");
        Mockito.when(recordMockito.getCampo("nomeCampoCoerente")).thenReturn("A3");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
}