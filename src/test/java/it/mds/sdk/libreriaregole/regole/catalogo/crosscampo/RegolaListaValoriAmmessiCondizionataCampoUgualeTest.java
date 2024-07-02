/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaB42;
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
class RegolaListaValoriAmmessiCondizionataCampoUgualeTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void testValoriAmmessiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("parametroCampoCondizionante", "PNR");
        parametri.put("listaValoriAmmessi", "K018A"); //Lista di un solo campo
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataCampoUguale regola = new RegolaListaValoriAmmessiCondizionataCampoUguale("regolaListaValoriAmmessiCondizionataCampoUguale", "BR008", "Codice BR008", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("K018A");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("PNR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoriAmmessiK0() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("parametroCampoCondizionante", "PNR");
        parametri.put("listaValoriAmmessi", "K018A"); //Lista di un solo campo
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataCampoUguale regola = new RegolaListaValoriAmmessiCondizionataCampoUguale("regolaListaValoriAmmessiCondizionataCampoUguale", "BR008", "Codice BR008", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("valore non valido");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("PNR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR008", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testCampoCondizionanteDiversoDaParametroOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("parametroCampoCondizionante", "PNR");
        parametri.put("listaValoriAmmessi", "K018A"); //Lista di un solo campo
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataCampoUguale regola = new RegolaListaValoriAmmessiCondizionataCampoUguale("regolaListaValoriAmmessiCondizionataCampoUguale", "BR008", "Codice BR008", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("valore Random");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("RPNPRE");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCondizionataCampoUguale regola = new RegolaListaValoriAmmessiCondizionataCampoUguale();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataCampoUguale);
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriAmmessiCondizionataCampoUguale regola = new RegolaListaValoriAmmessiCondizionataCampoUguale("RegolaListaValoriAmmessiCondizionataCampoUguale", "BR008", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

}