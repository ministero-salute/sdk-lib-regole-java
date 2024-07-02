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
class RegolaObbligatorietaNullCondizionataValoreDiversoCampoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaNullCondizionataValoreDiversoCampo regola = new RegolaObbligatorietaNullCondizionataValoreDiversoCampo();
        assertTrue(regola instanceof RegolaObbligatorietaNullCondizionataValoreDiversoCampo);
    }

    @Test
    void testKOValoreCampoCondizionanteDiversoDaParametro() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "tipoErogatore");
        parametri.put("parametroCampoCondizionante", "02");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaNullCondizionataValoreDiversoCampo regola = new RegolaObbligatorietaNullCondizionataValoreDiversoCampo("RegolaObbligatorietaNullCondizionataValoreDiversoCampo", "B36", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("03");
        List<Esito> result = regola.valida("tipoStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B36", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "tipoErogatore");
        parametri.put("parametroCampoCondizionante", "02");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaNullCondizionataValoreDiversoCampo regola = new RegolaObbligatorietaNullCondizionataValoreDiversoCampo("RegolaObbligatorietaNullCondizionataValoreDiversoCampo", "B36", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        List<Esito> result = regola.valida("tipoStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaNullCondizionataValoreDiversoCampo regola = new RegolaObbligatorietaNullCondizionataValoreDiversoCampo("RegolaObbligatorietaNullCondizionataValoreDiversoCampo", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("tipoStrutturaErogante");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("tipoStrutturaErogante", recordMockito));
    }
}