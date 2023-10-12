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
class RegolaEsistenzaDisuguaglianzaCrossCampoTest {

    @Mock
    RecordDtoGenerico recordMockito;
    //se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE

    @Test
    void validaCostruttore() {
        RegolaEsistenzaDisuguaglianzaCrossCampo regola = new RegolaEsistenzaDisuguaglianzaCrossCampo();
        assertTrue(regola instanceof RegolaEsistenzaDisuguaglianzaCrossCampo);
    }

    @Test
    void diversitaValoreStringOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        //Esempio: se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "modalitaTrasmissione");
        parametri.put("listaValoriIncoerenti", "MV|RE");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaDisuguaglianzaCrossCampo regola = new RegolaEsistenzaDisuguaglianzaCrossCampo("esistenzaDisuguaglianzaCrossCampo", "E01", "desrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("modalitaTrasmissione")).thenReturn("TT");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreStringKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "modalitaTrasmissione");
        parametri.put("listaValoriIncoerenti", "MV|RE");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaDisuguaglianzaCrossCampo regola = new RegolaEsistenzaDisuguaglianzaCrossCampo("esistenzaDisuguaglianzaCrossCampo", "E01", "desrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("modalitaTrasmissione")).thenReturn("MV");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }


    @Test
    void diversitaValoreInteriOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        //Esempio: se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "modalitaTrasmissione");
        parametri.put("listaValoriIncoerenti", "1|2");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaDisuguaglianzaCrossCampo regola = new RegolaEsistenzaDisuguaglianzaCrossCampo("esistenzaDisuguaglianzaCrossCampo", "E01", "desrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("modalitaTrasmissione")).thenReturn(5);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreInteriKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "modalitaTrasmissione");
        parametri.put("listaValoriIncoerenti", "1|2");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaDisuguaglianzaCrossCampo regola = new RegolaEsistenzaDisuguaglianzaCrossCampo("esistenzaDisuguaglianzaCrossCampo", "E01", "desrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("modalitaTrasmissione")).thenReturn(1);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "modalitaTrasmissione");
        parametri.put("listaValoriIncoerenti", "1|2");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaDisuguaglianzaCrossCampo regola = new RegolaEsistenzaDisuguaglianzaCrossCampo("esistenzaDisuguaglianzaCrossCampo", "E01", "desrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataTrasferimento");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataTrasferimento", recordMockito));
    }

    @Test
    void diversitaValoreStringNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        //Esempio: se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "modalitaTrasmissione");
        parametri.put("listaValoriIncoerenti", "MV|RE");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaDisuguaglianzaCrossCampo regola = new RegolaEsistenzaDisuguaglianzaCrossCampo("esistenzaDisuguaglianzaCrossCampo", "E01", "desrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn(null);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
}