package it.mds.sdk.libreriaregole.regole.catalogo.lunghezza;

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
class RegolaLunghezzacampoFacoltativoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void testLunghezzaValidaOKCampoNonPresente() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("size", "3");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaLunghezzaCampoFacoltativo regola = new RegolaLunghezzaCampoFacoltativo("lunghezza", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testLunghezzaValidaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("size", "4");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaLunghezzaCampoFacoltativo regola = new RegolaLunghezzaCampoFacoltativo("lunghezza", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("TEST");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testLunghezzaValidaKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("size", "4");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaLunghezzaCampoFacoltativo regola = new RegolaLunghezzaCampoFacoltativo("lunghezza", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("TESTKO");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaLunghezzaCampoFacoltativo regola = new RegolaLunghezzaCampoFacoltativo();
        assertTrue(regola instanceof RegolaLunghezzaCampoFacoltativo);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaLunghezzaCampoFacoltativo regola = new RegolaLunghezzaCampoFacoltativo();
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}
