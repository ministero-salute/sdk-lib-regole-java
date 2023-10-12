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
class RegolaIncoerenzaComuneRegioneASLTest {
    @Mock
    RecordDtoGenerico recordMockito;
    //se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaComuneRegioneASL regola = new RegolaIncoerenzaComuneRegioneASL();
        assertTrue(regola instanceof RegolaIncoerenzaComuneRegioneASL);
    }

    @Test
    void regolaIncoerenzaComuneRegioneASLKO1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCoerente1", "999");
        parametri.put("valoreCoerente2", "999");
        parametri.put("valoreCoerente3", "999");
        parametri.put("campoCoerente2", "campoCoerente2");
        parametri.put("campoCoerente3", "campoCoerente3");
        parametri.put("listaValoriCoerenti", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIncoerenzaComuneRegioneASL regola = new RegolaIncoerenzaComuneRegioneASL("RegolaIncoerenzaComuneRegioneASL", "", "desrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("899");
        Mockito.when(recordMockito.getCampo("campoCoerente2")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void regolaIncoerenzaComuneRegioneASLOK1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCoerente1", "999");
        parametri.put("valoreCoerente2", "999");
        parametri.put("valoreCoerente3", "999");
        parametri.put("campoCoerente2", "campoCoerente2");
        parametri.put("campoCoerente3", "campoCoerente3");
        parametri.put("listaValoriCoerenti", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIncoerenzaComuneRegioneASL regola = new RegolaIncoerenzaComuneRegioneASL("RegolaIncoerenzaComuneRegioneASL", "", "desrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("999");
        Mockito.when(recordMockito.getCampo("campoCoerente2")).thenReturn("999");
        Mockito.when(recordMockito.getCampo("campoCoerente3")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void regolaIncoerenzaComuneRegioneASLKO2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCoerente1", "999");
        parametri.put("valoreCoerente2", "999");
        parametri.put("valoreCoerente3", "999");
        parametri.put("campoCoerente2", "campoCoerente2");
        parametri.put("campoCoerente3", "campoCoerente3");
        parametri.put("listaValoriCoerenti", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIncoerenzaComuneRegioneASL regola = new RegolaIncoerenzaComuneRegioneASL("RegolaIncoerenzaComuneRegioneASL", "", "desrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("999");
        Mockito.when(recordMockito.getCampo("campoCoerente2")).thenReturn("998");
        Mockito.when(recordMockito.getCampo("campoCoerente3")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCoerente1", "999");
        parametri.put("valoreCoerente2", "999");
        parametri.put("valoreCoerente3", "999");
        parametri.put("campoCoerente2", "campoCoerente2");
        parametri.put("campoCoerente3", "campoCoerente3");
        parametri.put("listaValoriCoerenti", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIncoerenzaComuneRegioneASL regola = new RegolaIncoerenzaComuneRegioneASL("RegolaIncoerenzaComuneRegioneASL", "", "desrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}