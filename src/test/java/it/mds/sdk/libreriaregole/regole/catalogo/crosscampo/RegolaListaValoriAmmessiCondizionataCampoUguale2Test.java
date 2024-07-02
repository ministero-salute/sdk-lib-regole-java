/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegolaListaValoriAmmessiCondizionataCampoUguale2Test {
    @Mock
    RecordDtoGenerico recordMockito;
    RegolaListaValoriAmmessiCondizionataCampoUguale2 regola;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("parametroCampoCondizionante", "PNR");
        parametri.put("listaValoriAmmessi", "K018A"); //Lista di un solo campo
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = new RegolaListaValoriAmmessiCondizionataCampoUguale2("regolaListaValoriAmmessiCondizionataCampoUguale", "BR008", "Codice BR008", parametriTest);
    }

    @Test
    void testValoriAmmessiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("K018A");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("PNR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoriAmmessiK0() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("K018A");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("!PNR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR008", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testCampoCondizionanteDiversoDaParametroOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("valore Random");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("RPNPRE");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void costruttoreVuoto() {
        RegolaListaValoriAmmessiCondizionataCampoUguale2 regola = new RegolaListaValoriAmmessiCondizionataCampoUguale2();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataCampoUguale2);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}
