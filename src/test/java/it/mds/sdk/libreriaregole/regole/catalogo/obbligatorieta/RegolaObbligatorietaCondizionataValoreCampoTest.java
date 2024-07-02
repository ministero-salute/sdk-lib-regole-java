/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

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
class RegolaObbligatorietaCondizionataValoreCampoTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaCondizionataValoreCampo regola = new RegolaObbligatorietaCondizionataValoreCampo();
        assertTrue(regola instanceof RegolaObbligatorietaCondizionataValoreCampo);
    }

    @Test
    void testObbligatorietaCondizionataValidaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "modalitaTrasmissione");
        parametri.put("parametroCampoCondizionante", "TR");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataValoreCampo regola = new RegolaObbligatorietaCondizionataValoreCampo("obbligatorietaCondizionata", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("modalitaTrasmissione")).thenReturn("TR");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testObbligatorietaStringValidaKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "modalitaTrasmissione");
        parametri.put("parametroCampoCondizionante", "TR");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaObbligatorietaCondizionataValoreCampo regola = new RegolaObbligatorietaCondizionataValoreCampo("obbligatorietaCondizionata", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("modalitaTrasmissione")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testObbligatorietaCondizionataIntegerValidaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "modalitaTrasmissione");
        parametri.put("parametroCampoCondizionante", "TR");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataValoreCampo regola = new RegolaObbligatorietaCondizionataValoreCampo("obbligatorietaCondizionata", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(123);
        Mockito.when(recordMockito.getCampo("modalitaTrasmissione")).thenReturn("TR");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "modalitaTrasmissione");
        parametri.put("parametroCampoCondizionante", "TR");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataValoreCampo regola = new RegolaObbligatorietaCondizionataValoreCampo("obbligatorietaCondizionata", "E01", "descrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        Mockito.when(recordMockito.getCampo("modalitaTrasmissione")).thenReturn("TR");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }


}