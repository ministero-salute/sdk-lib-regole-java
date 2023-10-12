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
class Regola3campiTuttiPieniOTuttiVuotiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        Regola3campiTuttiPieniOTuttiVuoti regola = new Regola3campiTuttiPieniOTuttiVuoti();
        assertTrue(regola instanceof Regola3campiTuttiPieniOTuttiVuoti);
    }

    @Test
    void test3CampiVuotiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("campo2", "tipologiaCodIdentAssistito");
        parametri.put("campo3", "validitaCodIdentAssistito");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        Regola3campiTuttiPieniOTuttiVuoti regola = new Regola3campiTuttiPieniOTuttiVuoti("Regola3campiTuttiPieniOTuttiVuoti", "B16", "Codice B16", parametriTest);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipologiaCodIdentAssistito")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("validitaCodIdentAssistito")).thenReturn(null);
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void test3CampiPieniOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("campo2", "tipologiaCodIdentAssistito");
        parametri.put("campo3", "validitaCodIdentAssistito");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        Regola3campiTuttiPieniOTuttiVuoti regola = new Regola3campiTuttiPieniOTuttiVuoti("Regola3campiTuttiPieniOTuttiVuoti", "B16", "Codice B16", parametriTest);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("pieno1");
        Mockito.when(recordMockito.getCampo("tipologiaCodIdentAssistito")).thenReturn("pieno2");
        Mockito.when(recordMockito.getCampo("validitaCodIdentAssistito")).thenReturn("pieno3");
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void test2CampiPieni1VuotoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("campo2", "tipologiaCodIdentAssistito");
        parametri.put("campo3", "validitaCodIdentAssistito");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        Regola3campiTuttiPieniOTuttiVuoti regola = new Regola3campiTuttiPieniOTuttiVuoti("Regola3campiTuttiPieniOTuttiVuoti", "B16", "Codice B16", parametriTest);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("pieno1");
        Mockito.when(recordMockito.getCampo("tipologiaCodIdentAssistito")).thenReturn("pieno2");
        Mockito.when(recordMockito.getCampo("validitaCodIdentAssistito")).thenReturn(null);
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B16", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Regola3campiTuttiPieniOTuttiVuoti regola = new Regola3campiTuttiPieniOTuttiVuoti("Regola3campiTuttiPieniOTuttiVuoti", "CF", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("identificativoAssistito");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("identificativoAssistito", recordMockito));
    }
}