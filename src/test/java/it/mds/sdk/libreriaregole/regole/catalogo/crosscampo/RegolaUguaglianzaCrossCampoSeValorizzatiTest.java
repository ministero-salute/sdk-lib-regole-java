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
class RegolaUguaglianzaCrossCampoSeValorizzatiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void uguaglianzaStringOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampoSeValorizzati regola = new RegolaUguaglianzaCrossCampoSeValorizzati("regolaUguaglianzaCrossCampoSeValorizzati", "BR050", "Codice BR050", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("PROGID");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("PROGID");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void uguaglianzaStringKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampoSeValorizzati regola = new RegolaUguaglianzaCrossCampoSeValorizzati("regolaUguaglianzaCrossCampoSeValorizzati", "BR050", "Codice BR050", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("valore Random");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("PROGID");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR050", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void campoVuotoOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampoSeValorizzati regola = new RegolaUguaglianzaCrossCampoSeValorizzati("regolaUguaglianzaCrossCampoSeValorizzati", "BR050", "Codice BR050", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("PROGID");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaUguaglianzaCrossCampoSeValorizzati regola = new RegolaUguaglianzaCrossCampoSeValorizzati();
        assertTrue(regola instanceof RegolaUguaglianzaCrossCampoSeValorizzati);
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaUguaglianzaCrossCampoSeValorizzati regola = new RegolaUguaglianzaCrossCampoSeValorizzati("TEST", "TEST", "TEST",null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}