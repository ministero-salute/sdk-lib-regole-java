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
class RegolaAntigeneNonValorizzatoCorrettamenteTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaAntigeneNonValorizzatoCorrettamente regola = new RegolaAntigeneNonValorizzatoCorrettamente();
        assertTrue(regola instanceof RegolaAntigeneNonValorizzatoCorrettamente);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaAntigeneNonValorizzatoCorrettamente regola = new RegolaAntigeneNonValorizzatoCorrettamente("RegolaAntigeneNonValorizzatoCorrettamente", "D38", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataSomministrazione", "dataSomministrazione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaAntigeneNonValorizzatoCorrettamente regola = new RegolaAntigeneNonValorizzatoCorrettamente("RegolaAntigeneNonValorizzatoCorrettamente", "D38", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("antigene")).thenReturn("07");
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2020-10-10");
        List<Esito> result = regola.valida("antigene", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataSomministrazione", "dataSomministrazione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaAntigeneNonValorizzatoCorrettamente regola = new RegolaAntigeneNonValorizzatoCorrettamente("RegolaAntigeneNonValorizzatoCorrettamente", "D38", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("antigene")).thenReturn("08");
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2020-10-10");
        List<Esito> result = regola.valida("antigene", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D38", e.getErroriValidazione().get(0).getCodice());
        }
    }


    @Test
    void testNullPointerException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaAntigeneNonValorizzatoCorrettamente regola = new RegolaAntigeneNonValorizzatoCorrettamente("RegolaAntigeneNonValorizzatoCorrettamente", "D38", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("antigene")).thenReturn("08");
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn(null);
        List<Esito> result = regola.valida("antigene", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D38", e.getErroriValidazione().get(0).getCodice());
        }
    }
}