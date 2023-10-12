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
class RegolaUguaglianzaCrossCampoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void diversitaValoreStringOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("campoValidatore","tipologia");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampo regola = new RegolaUguaglianzaCrossCampo("uguaglianzaCrossCampo","E01","descrizioneErrore",parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn("IT");
        Mockito.when(recordMockito.getCampo("tipologia")).thenReturn("IT");
        List<Esito> result = regola.valida("cittadinanza",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreStringKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("campoValidatore","tipologia");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampo regola = new RegolaUguaglianzaCrossCampo("uguaglianzaCrossCampo","E01","descrizioneErrore",parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn("IT");
        Mockito.when(recordMockito.getCampo("tipologia")).thenReturn("AA");
        List<Esito> result = regola.valida("cittadinanza",recordMockito);
        for(Esito e : result){
            assertFalse(e.isValoreEsito());
            assertEquals("E01" ,e.getErroriValidazione().get(0).getCodice());
        }
    }


    @Test
    void diversitaValoreInteriOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("campoValidatore","tipologia");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampo regola = new RegolaUguaglianzaCrossCampo("uguaglianzaCrossCampo","E01","descrizioneErrore",parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn(1);
        Mockito.when(recordMockito.getCampo("tipologia")).thenReturn(1);
        List<Esito> result = regola.valida("cittadinanza",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void diversitaValoreInteriKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("campoValidatore","tipologia");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampo regola = new RegolaUguaglianzaCrossCampo("uguaglianzaCrossCampo","E01","descrizioneErrore",parametriTest);

        Mockito.when(recordMockito.getCampo("cittadinanza")).thenReturn(1);
        Mockito.when(recordMockito.getCampo("tipologia")).thenReturn(3);
        List<Esito> result = regola.valida("cittadinanza",recordMockito);
        for(Esito e : result){
            assertFalse(e.isValoreEsito());
            assertEquals("E01" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        RegolaUguaglianzaCrossCampo regola = new RegolaUguaglianzaCrossCampo("TEST", "TEST", "TEST",null);

        Mockito.when(recordMockito.getCampo("codStruttura")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));
    }


}