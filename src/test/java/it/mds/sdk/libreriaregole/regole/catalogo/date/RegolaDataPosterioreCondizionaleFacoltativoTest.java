package it.mds.sdk.libreriaregole.regole.catalogo.date;

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
class RegolaDataPosterioreCondizionaleFacoltativoTest {
    @Mock
    RecordDtoGenerico recordMockito;
    @Test
    void validaCostruttore() {
        RegolaDataPosterioreCondizionaleFacoltativo regola = new RegolaDataPosterioreCondizionaleFacoltativo();
        assertTrue(regola instanceof RegolaDataPosterioreCondizionaleFacoltativo);
    }
    @Test
    void testDataTrasferimentoOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataNascita");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosterioreCondizionaleFacoltativo regola = new RegolaDataPosterioreCondizionaleFacoltativo("regolaDataPosterioreCondizionaleFacoltativo", "regolaDataPosterioreCondizionaleFacoltativo", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1986-05-27");
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("1979-01-25");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testDataTrasferimentoKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataNascita");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosterioreCondizionaleFacoltativo regola = new RegolaDataPosterioreCondizionaleFacoltativo("regolaDataPosterioreCondizionaleFacoltativo", "regolaDataPosterioreCondizionaleFacoltativo", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("1986-05-27");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataPosterioreCondizionaleFacoltativo", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testDataTrasferimentoNullOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataNascita");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosterioreCondizionaleFacoltativo regola = new RegolaDataPosterioreCondizionaleFacoltativo("regolaDataPosterioreCondizionaleFacoltativo", "regolaDataPosterioreCondizionaleFacoltativo", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("1979-01-25");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testDataTrasferimentoException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataNascita");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosterioreCondizionaleFacoltativo regola = new RegolaDataPosterioreCondizionaleFacoltativo("regolaDataPosterioreCondizionaleFacoltativo", "regolaDataPosterioreCondizionaleFacoltativo", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1979-13-25");
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("1979-01-25");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataPosterioreCondizionaleFacoltativo", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataPosterioreCondizionaleFacoltativo regola = new RegolaDataPosterioreCondizionaleFacoltativo("regolaDataPosterioreCondizionaleFacoltativo", "regolaDataPosterioreCondizionaleFacoltativo", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataTrasferimento");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataTrasferimento", recordMockito));
    }
}