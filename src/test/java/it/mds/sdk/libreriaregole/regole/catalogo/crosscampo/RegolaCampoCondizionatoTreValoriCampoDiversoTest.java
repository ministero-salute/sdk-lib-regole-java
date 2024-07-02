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
class RegolaCampoCondizionatoTreValoriCampoDiversoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaCampoCondizionatoTreValoriCampoDiverso regola = new RegolaCampoCondizionatoTreValoriCampoDiverso();
        assertTrue(regola instanceof RegolaCampoCondizionatoTreValoriCampoDiverso);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCampoCondizionatoTreValoriCampoDiverso regola = new RegolaCampoCondizionatoTreValoriCampoDiverso("RegolaCampoCondizionatoTreValoriCampoDiverso", "AAAA", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante1", "campoCondizionante1");
        parametri.put("campoCondizionante2", "campoCondizionante2");
        parametri.put("parametroCondizionante1", "ADD");
        parametri.put("parametroCondizionante2", "N112A");
        parametri.put("listaValoriAmmessi", "G061A|G062A");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCampoCondizionatoTreValoriCampoDiverso regola = new RegolaCampoCondizionatoTreValoriCampoDiverso("RegolaCampoCondizionatoTreValoriCampoDiverso", "AAAA", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("120");
        Mockito.when(recordMockito.getCampo("campoCondizionante1")).thenReturn("OK1");
        Mockito.when(recordMockito.getCampo("campoCondizionante2")).thenReturn("Ok2");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante1", "campoCondizionante1");
        parametri.put("campoCondizionante2", "campoCondizionante2");
        parametri.put("parametroCondizionante1", "ADD");
        parametri.put("parametroCondizionante2", "N112A");
        parametri.put("listaValoriAmmessi", "G061A|G062A");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCampoCondizionatoTreValoriCampoDiverso regola = new RegolaCampoCondizionatoTreValoriCampoDiverso("RegolaCampoCondizionatoTreValoriCampoDiverso", "AAAA", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("G061A");
        Mockito.when(recordMockito.getCampo("campoCondizionante1")).thenReturn("ADD");
        Mockito.when(recordMockito.getCampo("campoCondizionante2")).thenReturn("N112A");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("AAAA", e.getErroriValidazione().get(0).getCodice());
        }
    }

}