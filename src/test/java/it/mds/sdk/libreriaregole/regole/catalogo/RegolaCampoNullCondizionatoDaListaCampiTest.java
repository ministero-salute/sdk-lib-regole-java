/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

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
class RegolaCampoNullCondizionatoDaListaCampiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaCampoNullCondizionatoDaListaCampi regola = new RegolaCampoNullCondizionatoDaListaCampi();
        assertTrue(regola instanceof RegolaCampoNullCondizionatoDaListaCampi);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCampoNullCondizionatoDaListaCampi regola = new RegolaCampoNullCondizionatoDaListaCampi("RegolaCampoNullCondizionatoDaListaCampi", "B42", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceAziendaSanitariaErogante");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceAziendaSanitariaErogante", recordMockito));
    }

    @Test
    void campoDaValidareNotNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampi", "meseDiRiferimento|annoRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaCampoNullCondizionatoDaListaCampi regola = new RegolaCampoNullCondizionatoDaListaCampi("RegolaCampoNullCondizionatoDaListaCampi", "B42", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("pieno");
        List<Esito> result = regola.valida("codiceAziendaSanitariaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampi", "meseDiRiferimento|annoRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaCampoNullCondizionatoDaListaCampi regola = new RegolaCampoNullCondizionatoDaListaCampi("RegolaCampoNullCondizionatoDaListaCampi", "B42", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("10");
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn("1990");
        List<Esito> result = regola.valida("codiceAziendaSanitariaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B42", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void test0K() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampi", "meseDiRiferimento|annoRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaCampoNullCondizionatoDaListaCampi regola = new RegolaCampoNullCondizionatoDaListaCampi("RegolaCampoNullCondizionatoDaListaCampi", "B42", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn(null);
        List<Esito> result = regola.valida("codiceAziendaSanitariaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
}