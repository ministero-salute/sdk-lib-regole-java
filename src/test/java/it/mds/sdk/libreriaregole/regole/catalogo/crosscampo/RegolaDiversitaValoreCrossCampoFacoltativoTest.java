package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaDiversitaValoreCrossCampoFacoltativoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void diversitaValoreStringOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "TR");
        parametri.put("campoDipendente", "nomeCampoConfronto");
        parametri.put("valoreDipendente", "MV");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCrossCampoFacoltativo regola = new RegolaDiversitaValoreCrossCampoFacoltativo("diversitaValoreCrossCampoFacoltativo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("VV");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaDiversitaValoreCrossCampoFacoltativo regola = new RegolaDiversitaValoreCrossCampoFacoltativo();
        assertTrue(regola instanceof RegolaDiversitaValoreCrossCampoFacoltativo);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "BB");
        parametri.put("campoDipendente", "campoDipendente");
        parametri.put("valoreDipendente", "CC");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCrossCampoFacoltativo regola = new RegolaDiversitaValoreCrossCampoFacoltativo("RegolaDiversitaValoreCrossCampoFacoltativo", "AAA", "Codice", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenThrow(new IllegalAccessException());
        Mockito.when(recordMockito.getCampo("campoDipendente")).thenReturn("GG");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void campoDaValidareNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "TR");
        parametri.put("campoDipendente", "nomeCampoConfronto");
        parametri.put("valoreDipendente", "MV");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCrossCampoFacoltativo regola = new RegolaDiversitaValoreCrossCampoFacoltativo("diversitaValoreCrossCampoFacoltativo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("VV");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreStringKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "TR");
        parametri.put("campoDipendente", "nomeCampoConfronto");
        parametri.put("valoreDipendente", "MV");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCrossCampoFacoltativo regola = new RegolaDiversitaValoreCrossCampoFacoltativo("diversitaValoreCrossCampoFacoltativo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("MV");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void diversitaValoreIntegerOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "1");
        parametri.put("campoDipendente", "nomeCampoConfronto");
        parametri.put("valoreDipendente", "2");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCrossCampoFacoltativo regola = new RegolaDiversitaValoreCrossCampoFacoltativo("diversitaValoreCrossCampoFacoltativo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(1);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(5);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreIntegerKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "1");
        parametri.put("campoDipendente", "nomeCampoConfronto");
        parametri.put("valoreDipendente", "2");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreCrossCampoFacoltativo regola = new RegolaDiversitaValoreCrossCampoFacoltativo("diversitaValoreCrossCampoFacoltativo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(1);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(2);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }
}