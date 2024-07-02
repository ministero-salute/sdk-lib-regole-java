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
class RegolaCoerenzaCampiUgualiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaCoerenzaCampiUguali regola = new RegolaCoerenzaCampiUguali();
        assertTrue(regola instanceof RegolaCoerenzaCampiUguali);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "BB");
        parametri.put("campoDipendente", "campoDipendente");
        parametri.put("valoreDipendente", "CC");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCoerenzaCampiUguali regola = new RegolaCoerenzaCampiUguali("RegolaCoerenzaCampiUguali", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenThrow(new IllegalAccessException());
        Mockito.when(recordMockito.getCampo("campoDipendente")).thenReturn("GG");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }


    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "BB");
        parametri.put("campoDipendente", "campoDipendente");
        parametri.put("valoreDipendente", "CC");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCoerenzaCampiUguali regola = new RegolaCoerenzaCampiUguali("RegolaCoerenzaCampiUguali", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("GG");
        Mockito.when(recordMockito.getCampo("campoDipendente")).thenReturn("GG");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "BB");
        parametri.put("campoDipendente", "campoDipendente");
        parametri.put("valoreDipendente", "CC");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCoerenzaCampiUguali regola = new RegolaCoerenzaCampiUguali("RegolaCoerenzaCampiUguali", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("BB");
        Mockito.when(recordMockito.getCampo("campoDipendente")).thenReturn("GG");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("AAA", e.getErroriValidazione().get(0).getCodice());
        }
    }
}