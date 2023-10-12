package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaDataCorrentePosterioreAnnoMeseRif;
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


class RegolaListaValoriAmmessiCampoFacoltativoTest {

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCampoFacoltativo regola = new RegolaListaValoriAmmessiCampoFacoltativo();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCampoFacoltativo);
    }

    private Map<String, String> parametri = new HashMap<>();

    @Test
    void testValoriAmmessiStringOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RecordDtoGenerico recordMockito = Mockito.mock(RecordDtoGenerico.class);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaValoriAmmessi", "MR|TR|RE");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCampoFacoltativo regola = new RegolaListaValoriAmmessiCampoFacoltativo("valoriAmmessiFacoltativo", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("MR");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoriAmmessiStringKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RecordDtoGenerico recordMockito = Mockito.mock(RecordDtoGenerico.class);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaValoriAmmessi", "MR|TR|RE");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCampoFacoltativo regola = new RegolaListaValoriAmmessiCampoFacoltativo("valoriAmmessiFacoltativo", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("GG");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testValoriAmmessiIntegerKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RecordDtoGenerico recordMockito = Mockito.mock(RecordDtoGenerico.class);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaValoriAmmessi", "1|2|3");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCampoFacoltativo regola = new RegolaListaValoriAmmessiCampoFacoltativo("valoriAmmessiFacoltativo", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(9);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testValoriAmmessiIntegerOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RecordDtoGenerico recordMockito = Mockito.mock(RecordDtoGenerico.class);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaValoriAmmessi", "1|2|3");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCampoFacoltativo regola = new RegolaListaValoriAmmessiCampoFacoltativo("valoriAmmessiFacoltativo", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(3);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoriAmmessiStringCampoNonPresenteOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaValoriAmmessi", "MR|TR|RE");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCampoFacoltativo regola = new RegolaListaValoriAmmessiCampoFacoltativo("valoriAmmessiFacoltativo", "E01", "descrizioneErrore", parametriTest);
        RecordDtoGenerico recordMockito = Mockito.mock(RecordDtoGenerico.class);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RecordDtoGenerico recordMockito = Mockito.mock(RecordDtoGenerico.class);
        RegolaListaValoriAmmessiCampoFacoltativo regola = new RegolaListaValoriAmmessiCampoFacoltativo("RegolaListaValoriAmmessiCampoFacoltativo", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceFarmaco");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("codiceFarmaco", recordMockito));
    }

}