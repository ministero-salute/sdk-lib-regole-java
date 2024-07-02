/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.catalogo.RegolaListaValoriAmmessi;
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
class RegolaSoloUnCampoValorizzatoTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaSoloUnCampoValorizzato regola = new RegolaSoloUnCampoValorizzato();
        assertTrue(regola instanceof RegolaSoloUnCampoValorizzato);
    }
    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "aslAssistito");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaSoloUnCampoValorizzato regola = new RegolaSoloUnCampoValorizzato("RegolaSoloUnCampoValorizzato", "B38", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("statoEsteroDiResidenza")).thenReturn("0001");
        Mockito.when(recordMockito.getCampo("aslAssistito")).thenReturn("1");
        List<Esito> result = regola.valida("statoEsteroDiResidenza", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B38", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "aslAssistito");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaSoloUnCampoValorizzato regola = new RegolaSoloUnCampoValorizzato("RegolaSoloUnCampoValorizzato", "B38", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("statoEsteroDiResidenza")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("aslAssistito")).thenReturn("1");
        List<Esito> result = regola.valida("statoEsteroDiResidenza", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaSoloUnCampoValorizzato regola = new RegolaSoloUnCampoValorizzato("RegolaSoloUnCampoValorizzato", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("statoEsteroDiResidenza");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("statoEsteroDiResidenza", recordMockito));
    }
}