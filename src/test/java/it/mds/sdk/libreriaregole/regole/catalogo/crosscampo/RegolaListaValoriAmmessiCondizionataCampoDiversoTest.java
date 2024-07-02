/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class RegolaListaValoriAmmessiCondizionataCampoDiversoTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @ParameterizedTest
    @CsvSource({
    	"K009A,NRP",
    	"valore Random,PNR",
    	"K022A,PNR"
    })
    void testValoriAmmessiOK(String campoDaValidare, String valoreCampoCondizionante) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("parametroCampoCondizionante", "PNR");
        parametri.put("listaValoriAmmessi", "K005A|K009A|K018A|K019A|K022A");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataCampoDiverso regola = new RegolaListaValoriAmmessiCondizionataCampoDiverso("regolaListaValoriAmmessiCondizionataCampoDiverso", "BR007", "Codice BR007", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(campoDaValidare);
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn(valoreCampoCondizionante);
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
        parametri.put("listaValoriAmmessi", "K005A|K009A|K018A|K019A|K022A");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataCampoDiverso regola = new RegolaListaValoriAmmessiCondizionataCampoDiverso("regolaListaValoriAmmessiCondizionataCampoDiverso", "BR007", "Codice BR007", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("valore non valido");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("NRP");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR007", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCondizionataCampoDiverso regola = new RegolaListaValoriAmmessiCondizionataCampoDiverso();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataCampoDiverso);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriAmmessiCondizionataCampoDiverso regola = new RegolaListaValoriAmmessiCondizionataCampoDiverso("regolaListaValoriAmmessiCondizionataCampoDiverso", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

}