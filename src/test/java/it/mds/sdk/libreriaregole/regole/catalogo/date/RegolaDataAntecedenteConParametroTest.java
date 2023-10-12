package it.mds.sdk.libreriaregole.regole.catalogo.date;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RegolaDataAntecedenteConParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataAntecedenteConParametro regolaDataAntecedente = new RegolaDataAntecedenteConParametro();
        assertTrue(regolaDataAntecedente instanceof RegolaDataAntecedenteConParametro);
    }

    @Test
    void validaOKParametro() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataDecesso");
        parametri.put("parametroTipoFlusso", "RE");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteConParametro regola = new RegolaDataAntecedenteConParametro("dataAntecedenteParametro", "E2085", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataDecesso")).thenReturn("2080-01-25");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("2000-01-25");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaParametroDiversoAtteso() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataDecesso");
        parametri.put("parametroTipoFlusso", "RI");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteConParametro regola = new RegolaDataAntecedenteConParametro("dataAntecedenteParametro", "E2085","descrizioneErrore", parametriTest);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
}