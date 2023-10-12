package it.mds.sdk.libreriaregole.regole.catalogo.cdm;

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
class RegolaVerificaFormatoDataTest {

    @Mock
    RecordDtoGenerico recordMockito;
    private Map<String, String> parametri = new HashMap<String, String>();

    @Test
    void validaCostruttore() {
        RegolaVerificaFormatoData regola = new RegolaVerificaFormatoData();
        assertTrue(regola instanceof RegolaVerificaFormatoData);
    }

    @Test
    void testVerificaDataOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoCondizionanteUno", "annoStipulaContratto");
        parametri.put("nomeCampoCondizionanteDue", "giornoStipulaContratto");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaVerificaFormatoData regola = new RegolaVerificaFormatoData("regolaVerificaData", "D88", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("meseStipulaContratto")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("annoStipulaContratto")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("giornoStipulaContratto")).thenReturn("17");
        List<Esito> result = regola.valida("meseStipulaContratto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testDataMeseNullKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoCondizionanteUno", "annoStipulaContratto");
        parametri.put("nomeCampoCondizionanteDue", "giornoStipulaContratto");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaVerificaFormatoData regola = new RegolaVerificaFormatoData("regolaVerificaData", "D88", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("meseStipulaContratto")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("annoStipulaContratto")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("giornoStipulaContratto")).thenReturn("17");
        List<Esito> result = regola.valida("meseStipulaContratto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("999", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testDataNowBeforeKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoCondizionanteUno", "annoStipulaContratto");
        parametri.put("nomeCampoCondizionanteDue", "giornoStipulaContratto");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaVerificaFormatoData regola = new RegolaVerificaFormatoData("regolaVerificaData", "D88", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("meseStipulaContratto")).thenReturn("2");
        Mockito.when(recordMockito.getCampo("annoStipulaContratto")).thenReturn("2999");
        Mockito.when(recordMockito.getCampo("giornoStipulaContratto")).thenReturn("17");
        List<Esito> result = regola.valida("meseStipulaContratto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D88", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        parametri.put("nomeCampoCondizionanteUno", "annoStipulaContratto");
        parametri.put("nomeCampoCondizionanteDue", "giornoStipulaContratto");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaVerificaFormatoData regola = new RegolaVerificaFormatoData("regolaVerificaData", "D88", "descrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}