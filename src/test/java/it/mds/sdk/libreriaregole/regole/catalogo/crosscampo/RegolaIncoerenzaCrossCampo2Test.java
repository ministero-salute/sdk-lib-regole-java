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
class RegolaIncoerenzaCrossCampo2Test {

    @Mock
    RecordDtoGenerico recordMockito;

    //Esempio: se RESTYPE è diverso da BIN allora EVALCODE non può essere J040A o J041A

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaCrossCampo2 regola = new RegolaIncoerenzaCrossCampo2();
        assertTrue(regola instanceof RegolaIncoerenzaCrossCampo2);
    }

    @Test
    void diversitaValoreStringOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "BIN");
        parametri.put("campoCondizionante", "campoCondizionante");
        parametri.put("listaValoriIncoerenti", "J040A|J041A");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo2 regola = new RegolaIncoerenzaCrossCampo2("regolaIncoerenzaCrossCampo2", "BR088", "Codice BR088", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("valore corretto");
        Mockito.when(recordMockito.getCampo("campoCondizionante")).thenReturn("BIN2");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "BIN");
        parametri.put("campoCondizionante", "campoCondizionante");
        parametri.put("listaValoriIncoerenti", "J040A|J041A");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo2 regola = new RegolaIncoerenzaCrossCampo2("regolaIncoerenzaCrossCampo2", "BR088", "Codice BR088", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("campoCondizionante")).thenReturn("BIN2");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoIncoerenteKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "BIN");
        parametri.put("campoCondizionante", "campoCondizionante");
        parametri.put("listaValoriIncoerenti", "J040A|J041A");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo2 regola = new RegolaIncoerenzaCrossCampo2("regolaIncoerenzaCrossCampo2", "BR088", "Codice BR088", parametriTest);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("J041A");
        Mockito.when(recordMockito.getCampo("campoCondizionante")).thenReturn("BIN2");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR088", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "BIN");
        parametri.put("campoCondizionante", "campoCondizionante");
        parametri.put("listaValoriIncoerenti", "J040A|J041A");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo2 regola = new RegolaIncoerenzaCrossCampo2("regolaIncoerenzaCrossCampo2", "BR088", "Codice BR088", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}