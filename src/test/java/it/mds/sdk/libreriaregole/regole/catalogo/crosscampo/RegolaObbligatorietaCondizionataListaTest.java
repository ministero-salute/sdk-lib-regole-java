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
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RegolaObbligatorietaCondizionataListaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaCondizionataLista regola = new RegolaObbligatorietaCondizionataLista();
        assertTrue(regola instanceof RegolaObbligatorietaCondizionataLista);
    }

    @Test
    void validaCampoPienoOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "tipologiaErogatore");
        parametri.put("primoParametro", "6");
        parametri.put("secondoParametro", "99");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataLista regola = new RegolaObbligatorietaCondizionataLista("obbligatorietaCondizionataLista", "E3500", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("codiceStruttura")).thenReturn("1");
        Mockito.when(recordMockito.getCampo("tipologiaErogatore")).thenReturn("7");
        List<Esito> result = regola.valida("codiceStruttura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCampoVuotoKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "tipologiaErogatore");
        parametri.put("primoParametro", "6");
        parametri.put("secondoParametro", "99");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataLista regola = new RegolaObbligatorietaCondizionataLista("obbligatorietaCondizionataLista", "E3500", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("codiceStruttura")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipologiaErogatore")).thenReturn("7");
        List<Esito> result = regola.valida("codiceStruttura", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E3500" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCampoVuotoOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "tipologiaErogatore");
        parametri.put("primoParametro", "6");
        parametri.put("secondoParametro", "99");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataLista regola = new RegolaObbligatorietaCondizionataLista("obbligatorietaCondizionataLista", "E3500", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("codiceStruttura")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipologiaErogatore")).thenReturn("6");
        List<Esito> result = regola.valida("codiceStruttura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCampoPienoKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "tipologiaErogatore");
        parametri.put("primoParametro", "6");
        parametri.put("secondoParametro", "99");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataLista regola = new RegolaObbligatorietaCondizionataLista("obbligatorietaCondizionataLista", "E3500","descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceStruttura")).thenReturn("2");
        Mockito.when(recordMockito.getCampo("tipologiaErogatore")).thenReturn("99");
        List<Esito> result = regola.valida("codiceStruttura", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E3500" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        var regola = new RegolaObbligatorietaCondizionataLista(
                "TEST", "TEST", "TEST", null
        );
        Mockito.when(recordMockito.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));
    }
}