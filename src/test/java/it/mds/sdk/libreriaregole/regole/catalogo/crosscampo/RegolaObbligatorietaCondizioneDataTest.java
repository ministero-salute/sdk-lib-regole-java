package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegolaObbligatorietaCondizioneDataTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaCondizioneData regolaObbligatorietaCondizioneData = new RegolaObbligatorietaCondizioneData();
        assertTrue(regolaObbligatorietaCondizioneData instanceof RegolaObbligatorietaCondizioneData);
    }

    @Test
    void testObbligatorietaCondizionataDataOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoCondizionante","dataSomministrazione");
        parametri.put("parametroCampoCondizionante","2019-01-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizioneData regola = new RegolaObbligatorietaCondizioneData("obbligatorietaCondizioneData","E4025","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("2020-01-10");
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testObbligatorietaCondizionataValoreNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoCondizionante","dataSomministrazione");
        parametri.put("parametroCampoCondizionante","2019-01-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizioneData regola = new RegolaObbligatorietaCondizioneData("obbligatorietaCondizioneData","E4025","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertFalse(e.isValoreEsito());
            assertEquals("E4025" ,e.getErroriValidazione().get(0).getCodice());

        }
    }

    @Test
    void testObbligatorietaCondizionataDataMinoreParametro() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoCondizionante","dataSomministrazione");
        parametri.put("parametroCampoCondizionante","2019-01-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizioneData regola = new RegolaObbligatorietaCondizioneData("obbligatorietaCondizioneData","E4025","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("2018-01-10");
        List<Esito> result = regola.valida("nomeCampoConfronto",recordMockito);
        for(Esito e : result){
            assertFalse(e.isValoreEsito());
            assertEquals("E4025" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testObbligatorietaCondizionataDataException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoCondizionante","dataSomministrazione");
        parametri.put("parametroCampoCondizionante","2019-01-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizioneData regola = new RegolaObbligatorietaCondizioneData("obbligatorietaCondizioneData","E4025","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("25/01/1979");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}