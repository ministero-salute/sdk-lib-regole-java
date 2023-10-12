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
class RegolaIncoerenzaCrossCampoTest {
    //Esempio: Se la cittadinanza é uguale a IT allora la tipologia di CI deve essere diversa da 1 o 2 o 3
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaCrossCampo regola = new RegolaIncoerenzaCrossCampo();
        assertTrue(regola instanceof RegolaIncoerenzaCrossCampo);
    }

    @Test
    void diversitaValoreStringOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "IT");
        parametri.put("nomeCampoCoerente", "tipologia");
        parametri.put("listaValoriIncoerenti", "AA|BB");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo regola = new RegolaIncoerenzaCrossCampo("diversitaEsistenzaDisuguaglianzaCrossCampo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn("IT");
        Mockito.when(recordMockito.getCampo("tipologia")).thenReturn("CC");
        List<Esito> result = regola.valida("cittadinanza", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreStringNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "IT");
        parametri.put("nomeCampoCoerente", "tipologia");
        parametri.put("listaValoriIncoerenti", "AA|BB");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo regola = new RegolaIncoerenzaCrossCampo("diversitaEsistenzaDisuguaglianzaCrossCampo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn(null);
        List<Esito> result = regola.valida("cittadinanza", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreStringKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "IT");
        parametri.put("nomeCampoCoerente", "tipologia");
        parametri.put("listaValoriIncoerenti", "AA|BB");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo regola = new RegolaIncoerenzaCrossCampo("diversitaEsistenzaDisuguaglianzaCrossCampo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn("IT");
        Mockito.when(recordMockito.getCampo("tipologia")).thenReturn("AA");
        List<Esito> result = regola.valida("cittadinanza", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void diversitaValoreInteriOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "IT");
        parametri.put("nomeCampoCoerente", "tipologia");
        parametri.put("listaValoriIncoerenti", "1|2|3");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo regola = new RegolaIncoerenzaCrossCampo("diversitaEsistenzaDisuguaglianzaCrossCampo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn("IT");
        Mockito.when(recordMockito.getCampo("tipologia")).thenReturn(4);
        List<Esito> result = regola.valida("cittadinanza", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreInteriKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "IT");
        parametri.put("nomeCampoCoerente", "tipologia");
        parametri.put("listaValoriIncoerenti", "1|2|3");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo regola = new RegolaIncoerenzaCrossCampo("diversitaEsistenzaDisuguaglianzaCrossCampo", "E01", "descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn("IT");
        Mockito.when(recordMockito.getCampo("tipologia")).thenReturn(3);
        List<Esito> result = regola.valida("cittadinanza", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreConfronto", "IT");
        parametri.put("nomeCampoCoerente", "tipologia");
        parametri.put("listaValoriIncoerenti", "1|2|3");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaIncoerenzaCrossCampo regola = new RegolaIncoerenzaCrossCampo("diversitaEsistenzaDisuguaglianzaCrossCampo", "E01", "descrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("cittadinanza");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("cittadinanza", recordMockito));
    }
}