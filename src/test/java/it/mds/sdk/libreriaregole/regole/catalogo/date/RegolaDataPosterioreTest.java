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
public class RegolaDataPosterioreTest {

    @Mock
    RecordDtoGenerico recordMockito;



    private Map<String, String> parametri = new HashMap<String, String>();

    @Test
    void validaCostruttore() {
        RegolaDataPosteriore regola = new RegolaDataPosteriore();
        assertTrue(regola instanceof RegolaDataPosteriore);
    }

    @Test
    void testDataPosterioreOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore","dataTrasferimentoResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosteriore regola = new RegolaDataPosteriore("dataPosteriore","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimentoResidenza")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("2022-01-25");
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @ParameterizedTest
    @CsvSource({
    	"dataPosteriore,2022-04-12,2022-01-25",
    	"dataPosteriore,25/01/1979,2022-01-25",
    	"dataAnteriore,2022-01-25,"
    	
    })
    void testDataPosterioreKO(String data, String dataTrasferimentoResidenza, String nomeCampoConfronto) 
    		throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore","dataTrasferimentoResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosteriore regola = new RegolaDataPosteriore(data,"E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataTrasferimentoResidenza")).thenReturn(dataTrasferimentoResidenza);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(nomeCampoConfronto);
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertFalse(e.isValoreEsito());
            assertEquals("E01" ,e.getErroriValidazione().get(0).getCodice());
        }
    }
    
    @Test
    void testDataPosterioreCampoConfrontoNull() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        parametri.put("nomeCampoValidatore","dataTrasferimentoResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataPosteriore regola = new RegolaDataPosteriore("dataAnteriore","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("2022-01-25");
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        parametri.put("nomeCampoValidatore","dataTrasferimentoResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaDataPosteriore regola = new RegolaDataPosteriore("regolaDataPosteriore", "regolaDataPosteriore", "descrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}
