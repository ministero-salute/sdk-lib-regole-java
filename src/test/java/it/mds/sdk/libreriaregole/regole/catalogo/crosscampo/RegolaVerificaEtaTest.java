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
public class RegolaVerificaEtaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaVerificaEta regolaVerificaEta = new RegolaVerificaEta();
        assertTrue(regolaVerificaEta instanceof RegolaVerificaEta);
    }

    @Test
    void validaEtaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "annoRiferimento");
        parametri.put("etaMin", "18");
        parametri.put("etaMax", "100");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaVerificaEta regola = new RegolaVerificaEta("verificaEta", "E3941", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("annoNascita")).thenReturn(1986);
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn(2022);
        List<Esito> result = regola.valida("annoNascita", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }

    }

    @Test
    void validaEtaKOMinoreLimite() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "annoRiferimento");
        parametri.put("etaMin", "18");
        parametri.put("etaMax", "100");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaVerificaEta regola = new RegolaVerificaEta("verificaEta", "E3941", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("annoNascita")).thenReturn(2021);
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn(2022);
        List<Esito> result = regola.valida("annoNascita", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E3941", e.getErroriValidazione().get(0).getCodice());
        }

    }

    @Test
    void validaEtaKOMaggioreLimite() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "annoRiferimento");
        parametri.put("etaMin", "18");
        parametri.put("etaMax", "100");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaVerificaEta regola = new RegolaVerificaEta("verificaEta", "E3941", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("annoNascita")).thenReturn(1986);
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn(2087);
        List<Esito> result = regola.valida("annoNascita", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E3941", e.getErroriValidazione().get(0).getCodice());
        }

    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaVerificaEta regola = new RegolaVerificaEta("RegolaVerificaEta", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}
