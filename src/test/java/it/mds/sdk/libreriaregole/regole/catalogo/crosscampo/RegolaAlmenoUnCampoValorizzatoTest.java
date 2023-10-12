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
class RegolaAlmenoUnCampoValorizzatoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaAlmenoUnCampoValorizzato regola = new RegolaAlmenoUnCampoValorizzato();
        assertTrue(regola instanceof RegolaAlmenoUnCampoValorizzato);
    }

    @Test
    void campiValorizzatiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "costoServizio");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaAlmenoUnCampoValorizzato regola = new RegolaAlmenoUnCampoValorizzato("RegolaAlmenoUnCampoValorizzato", "D38", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("costoServizioRicetta")).thenReturn("19.9");
        Mockito.when(recordMockito.getCampo("costoServizio")).thenReturn("99.9");
        List<Esito> result = regola.valida("costoServizioRicetta", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campiEntrambiNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "costoServizio");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaAlmenoUnCampoValorizzato regola = new RegolaAlmenoUnCampoValorizzato("RegolaAlmenoUnCampoValorizzato", "D38", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("costoServizioRicetta")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("costoServizio")).thenReturn(null);
        List<Esito> result = regola.valida("costoServizioRicetta", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D38", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaAlmenoUnCampoValorizzato regola = new RegolaAlmenoUnCampoValorizzato("RegolaAlmenoUnCampoValorizzato", "D38", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}