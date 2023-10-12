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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegolaDataPosterioreConParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataPosterioreConParametro regolaDataPosteriore = new RegolaDataPosterioreConParametro();
        assertTrue(regolaDataPosteriore instanceof RegolaDataPosterioreConParametro);
    }

    @Test
    void validaOKParametro() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataSomministrazione");
        parametri.put("campoAntigene", "campoAntigene");
        parametri.put("parametroAntigene1", "08");
        parametri.put("parametroAntigene2", "09");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosterioreConParametro regola = new RegolaDataPosterioreConParametro("dataPosterioreParametro", "E4100", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2019-01-01");
        Mockito.when(recordMockito.getCampo("campoAntigene")).thenReturn("08");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("2080-01-25");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaParametroDiversoAtteso() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataSomministrazione");
        parametri.put("campoAntigene", "campoAntigene");
        parametri.put("parametroAntigene1", "08");
        parametri.put("parametroAntigene2", "09");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosterioreConParametro regola = new RegolaDataPosterioreConParametro("dataPosterioreParametro", "E4100", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("campoAntigene")).thenReturn("07");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataSomministrazione");
        parametri.put("campoAntigene", "campoAntigene");
        parametri.put("parametroAntigene1", "08");
        parametri.put("parametroAntigene2", "09");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaDataPosterioreConParametro regola = new RegolaDataPosterioreConParametro("dataPosterioreParametro", "dataPosterioreParametro", "descrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoAntigene");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoAntigene", recordMockito));
    }
}