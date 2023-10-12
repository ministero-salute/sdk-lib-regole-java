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
class RegolaCampoFacoltativoCondizionataListaValoriAmmessiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaCampoFacoltativoCondizionataListaValoriAmmessi regola = new RegolaCampoFacoltativoCondizionataListaValoriAmmessi();
        assertTrue(regola instanceof RegolaCampoFacoltativoCondizionataListaValoriAmmessi);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCampoFacoltativoCondizionataListaValoriAmmessi regola = new RegolaCampoFacoltativoCondizionataListaValoriAmmessi("RegolaCampoFacoltativoCondizionataListaValoriAmmessi", "AAA", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "nomeCampoCondizionante");
        parametri.put("listaValoriAmmessi", "6|99");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCampoFacoltativoCondizionataListaValoriAmmessi regola = new RegolaCampoFacoltativoCondizionataListaValoriAmmessi("RegolaCampoFacoltativoCondizionataListaValoriAmmessi", "AAA", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("120");
        Mockito.when(recordMockito.getCampo("nomeCampoCondizionante")).thenReturn("99");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "nomeCampoCondizionante");
        parametri.put("listaValoriAmmessi", "6|99");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaCampoFacoltativoCondizionataListaValoriAmmessi regola = new RegolaCampoFacoltativoCondizionataListaValoriAmmessi("RegolaCampoFacoltativoCondizionataListaValoriAmmessi", "AAA", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("nomeCampoCondizionante")).thenReturn("10");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("AAA", e.getErroriValidazione().get(0).getCodice());
        }
    }
}