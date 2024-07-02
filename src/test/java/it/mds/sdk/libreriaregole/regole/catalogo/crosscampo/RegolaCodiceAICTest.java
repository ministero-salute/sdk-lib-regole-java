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
public class RegolaCodiceAICTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaCodiceAIC regola = new RegolaCodiceAIC();
        assertTrue(regola instanceof RegolaCodiceAIC);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCodiceAIC regola = new RegolaCodiceAIC("RegolaCodiceAIC", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void validaAICPienoOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataParametro", "2019-07-01");
        parametri.put("statoEsteroPar", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCodiceAIC regola = new RegolaCodiceAIC("regolaCodiceAIC", "E5020", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("codAIC")).thenReturn("ABCDEFGHI");
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2018-07-01");
        Mockito.when(recordMockito.getCampo("statoEstero")).thenReturn("FR");
        List<Esito> result = regola.valida("codAIC", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaAICVuotoOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataParametro", "2019-07-01");
        parametri.put("statoEsteroPar", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCodiceAIC regola = new RegolaCodiceAIC("regolaCodiceAIC", "E5020", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("codAIC")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2018-07-01");
        Mockito.when(recordMockito.getCampo("statoEstero")).thenReturn("IT");
        List<Esito> result = regola.valida("codAIC", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaAICKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataParametro", "2019-07-01");
        parametri.put("statoEsteroPar", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCodiceAIC regola = new RegolaCodiceAIC("regolaCodiceAIC", "E5020", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("codAIC")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2020-07-01");
        Mockito.when(recordMockito.getCampo("statoEstero")).thenReturn(null);
        List<Esito> result = regola.valida("codAIC", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E5020", e.getErroriValidazione().get(0).getCodice());
            //assertEquals("E5020", e.getCodice());
        }
    }
}