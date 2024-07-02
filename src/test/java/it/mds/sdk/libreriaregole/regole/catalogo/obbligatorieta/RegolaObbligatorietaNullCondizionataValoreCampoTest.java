/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.catalogo.crosscampo.RegolaSoloUnCampoValorizzato;
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
class RegolaObbligatorietaNullCondizionataValoreCampoTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaNullCondizionataValoreCampo regola = new RegolaObbligatorietaNullCondizionataValoreCampo();
        assertTrue(regola instanceof RegolaObbligatorietaNullCondizionataValoreCampo);
    }
    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "tipoPrescrittore");
        parametri.put("parametroCampoCondizionante", "Z");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaNullCondizionataValoreCampo regola = new RegolaObbligatorietaNullCondizionataValoreCampo("RegolaObbligatorietaNullCondizionataValoreCampo", "D17", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codicePrescrittore")).thenReturn("0001");
        Mockito.when(recordMockito.getCampo("tipoPrescrittore")).thenReturn("Z");
        List<Esito> result = regola.valida("codicePrescrittore", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D17", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "tipoPrescrittore");
        parametri.put("parametroCampoCondizionante", "Z");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaNullCondizionataValoreCampo regola = new RegolaObbligatorietaNullCondizionataValoreCampo("RegolaObbligatorietaNullCondizionataValoreCampo", "D17", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codicePrescrittore")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipoPrescrittore")).thenReturn("Z");
        List<Esito> result = regola.valida("codicePrescrittore", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "tipoPrescrittore");
        parametri.put("parametroCampoCondizionante", "Z");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaObbligatorietaNullCondizionataValoreCampo regola = new RegolaObbligatorietaNullCondizionataValoreCampo("RegolaObbligatorietaNullCondizionataValoreCampo", "", "descrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codicePrescrittore");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codicePrescrittore", recordMockito));
    }
}