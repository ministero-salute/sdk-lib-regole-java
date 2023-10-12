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
public class RegolaDataAnterioreTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataAntecedente regola = new RegolaDataAntecedente();
        assertTrue(regola instanceof RegolaDataAntecedente);
    }
    @Test
    void testDataAnterioreOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore","dataTrasferimentoResidenza");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedente regola = new RegolaDataAntecedente("dataAnteriore","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimentoResidenza")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("1978-01-25");
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @ParameterizedTest
    @CsvSource({
    	"2022-01-25,2022-04-25",
    	"25/01/1979,2022-01-25",
    	"2022-01-25,"
    })
    void testDataAnterioreKO(String dataTrasferimentoResidenza, String nomeCampoConfronto) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore","dataTrasferimentoResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedente regola = new RegolaDataAntecedente("dataAnteriore","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimentoResidenza")).thenReturn(dataTrasferimentoResidenza);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(nomeCampoConfronto);
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertFalse(e.isValoreEsito());
            assertEquals("E01" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testDataAnterioreCampoConfrontoNull() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore","dataTrasferimentoResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedente regola = new RegolaDataAntecedente("dataAnteriore","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("2022-01-25");
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataAntecedente regola = new RegolaDataAntecedente("regolaDataAntecedente", "regolaDataAntecedente", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}
