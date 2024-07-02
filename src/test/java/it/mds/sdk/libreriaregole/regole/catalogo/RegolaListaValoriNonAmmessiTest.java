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
class RegolaListaValoriNonAmmessiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    private Map<String, String> parametri = new HashMap<>();

    @Test
    void costruttoreVuoto() {
        RegolaListaValoriNonAmmessi regola = new RegolaListaValoriNonAmmessi();
        assertTrue(regola instanceof RegolaListaValoriNonAmmessi);
    }

    @Test
    void testValoriNonAmmessiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaValoriNonAmmessi1", "07|99");
        parametri.put("listaValoriNonAmmessi2", "04|05|99");
        parametri.put("viaSomministrazione", "viaSomministrazione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriNonAmmessi regola = new RegolaListaValoriNonAmmessi("valoriNonAmmessi", "E4001", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("06");
        Mockito.when(recordMockito.getCampo("viaSomministrazione")).thenReturn("02");

        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoriNonAmmessi2OK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaValoriNonAmmessi1", "07|99");
        parametri.put("listaValoriNonAmmessi2", "04|05|99");
        parametri.put("viaSomministrazione", "viaSomministrazione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriNonAmmessi regola = new RegolaListaValoriNonAmmessi("valoriNonAmmessi", "E4001","descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("99");
        Mockito.when(recordMockito.getCampo("viaSomministrazione")).thenReturn("02");

        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoriNonAmmessiKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaValoriNonAmmessi1", "07|99");
        parametri.put("listaValoriNonAmmessi2", "04|05|99");
        parametri.put("viaSomministrazione", "viaSomministrazione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriNonAmmessi regola = new RegolaListaValoriNonAmmessi("valoriNonAmmessi", "E4001", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("99");
        Mockito.when(recordMockito.getCampo("viaSomministrazione")).thenReturn("05");

        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E4001" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriNonAmmessi regola = new RegolaListaValoriNonAmmessi("valoriNonAmmessi", "E4001", "descrizioneErrore",null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}