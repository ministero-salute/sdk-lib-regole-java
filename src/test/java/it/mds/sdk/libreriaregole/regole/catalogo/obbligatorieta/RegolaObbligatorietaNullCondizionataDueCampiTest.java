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
class RegolaObbligatorietaNullCondizionataDueCampiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaNullCondizionataDueCampi regola = new RegolaObbligatorietaNullCondizionataDueCampi();
        assertTrue(regola instanceof RegolaObbligatorietaNullCondizionataDueCampi);
    }

    @Test
    void testValidaCampoBlankOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante1", "progid");
        parametri.put("nomeCampoCondizionante2", "origcountry");

        parametri.put("parametroCampoCondizionante1", "PNR");
        parametri.put("parametroCampoCondizionante2", "IT");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaNullCondizionataDueCampi regola = new RegolaObbligatorietaNullCondizionataDueCampi("regolaObbligatorietaNullCondizionataDueCampi", "BR163", "codice BR163", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(""); //EVALINFO_CONCLUSION
        Mockito.when(recordMockito.getCampo("progid")).thenReturn("PNR");
        Mockito.when(recordMockito.getCampo("origcountry")).thenReturn("FR");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValidaKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante1", "progid");
        parametri.put("nomeCampoCondizionante2", "origcountry");

        parametri.put("parametroCampoCondizionante1", "PNR");
        parametri.put("parametroCampoCondizionante2", "IT");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaNullCondizionataDueCampi regola = new RegolaObbligatorietaNullCondizionataDueCampi("regolaObbligatorietaNullCondizionataDueCampi", "BR163", "codice BR163", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("123"); //EVALINFO_CONCLUSION
        Mockito.when(recordMockito.getCampo("progid")).thenReturn("PNR");
        Mockito.when(recordMockito.getCampo("origcountry")).thenReturn("FR");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR163", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testValidaParDiversoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante1", "progid");
        parametri.put("nomeCampoCondizionante2", "origcountry");

        parametri.put("parametroCampoCondizionante1", "PNR");
        parametri.put("parametroCampoCondizionante2", "IT");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaNullCondizionataDueCampi regola = new RegolaObbligatorietaNullCondizionataDueCampi("regolaObbligatorietaNullCondizionataDueCampi", "BR163", "codice BR163", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("pieno"); //EVALINFO_CONCLUSION
        Mockito.when(recordMockito.getCampo("progid")).thenReturn("RNP");
        Mockito.when(recordMockito.getCampo("origcountry")).thenReturn("FR");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante1", "progid");
        parametri.put("nomeCampoCondizionante2", "origcountry");

        parametri.put("parametroCampoCondizionante1", "PNR");
        parametri.put("parametroCampoCondizionante2", "IT");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaNullCondizionataDueCampi regola = new RegolaObbligatorietaNullCondizionataDueCampi("regolaObbligatorietaNullCondizionataDueCampi", "BR163", "codice BR163", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}