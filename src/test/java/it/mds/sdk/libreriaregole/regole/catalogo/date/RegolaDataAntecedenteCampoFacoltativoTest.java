package it.mds.sdk.libreriaregole.regole.catalogo.date;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class RegolaDataAntecedenteCampoFacoltativoTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataAntecedenteCampoFacoltativo regola = new RegolaDataAntecedenteCampoFacoltativo();
        assertTrue(regola instanceof RegolaDataAntecedenteCampoFacoltativo);
    }

    @ParameterizedTest
    @CsvSource({
    	"1975-12-25,",
    	",1975-12-25",
    	"1975-12-25,1976-12-25"
    })
    void testDataMorteNullOK(String dataTrasferimento, String dataMorte) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataMorte");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteCampoFacoltativo regola = new RegolaDataAntecedenteCampoFacoltativo("regolaDataAntecedenteCampoFacoltativo", "regolaDataAntecedenteCampoFacoltativo", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn(dataTrasferimento);
        Mockito.when(recordMockito.getCampo("dataMorte")).thenReturn(dataMorte);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testDataTrasferimentoDiversoNulleIsNotBeforeData2OK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataMorte");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteCampoFacoltativo regola = new RegolaDataAntecedenteCampoFacoltativo("regolaDataAntecedenteCampoFacoltativo", "regolaDataAntecedenteCampoFacoltativo", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1975-12-25");
        Mockito.when(recordMockito.getCampo("dataMorte")).thenReturn("1975-12-25");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataAntecedenteCampoFacoltativo", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataAntecedenteCampoFacoltativo regola = new RegolaDataAntecedenteCampoFacoltativo("regolaDataAntecedenteCampoFacoltativo", "regolaDataAntecedenteCampoFacoltativo", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataTrasferimento");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataTrasferimento", recordMockito));
    }
    @Test
    void testDataTrasferimentoExceptionKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoValidatore", "dataMorte");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteCampoFacoltativo regola = new RegolaDataAntecedenteCampoFacoltativo("regolaDataAntecedenteCampoFacoltativo", "regolaDataAntecedenteCampoFacoltativo", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1975-13-25");
        Mockito.when(recordMockito.getCampo("dataMorte")).thenReturn("1975-12-25");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataAntecedenteCampoFacoltativo", e.getErroriValidazione().get(0).getCodice());
        }
    }
}