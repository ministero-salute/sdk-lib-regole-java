/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

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
class RegolaVerificaCodiceFiscaleTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaVerificaCodiceFiscale regola = new RegolaVerificaCodiceFiscale();
        assertTrue(regola instanceof RegolaVerificaCodiceFiscale);
    }

    @ParameterizedTest
    @CsvSource({
    	"CNTSVT90R29C351P,1990-10-29",
    	"GRSVCN86E27A028C,1986-05-27",
    	"TRVRTI85D44A028Q,1985-04-04",
    	"GRSGPP47L20A028J,1947-07-20",
    	"RMNMRA60R62E618D,1960-10-22"
    })
    void codiceFiscaleOK(String nomeCampoConfronto, String dataNascitaAssistito) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroDataNascita", "dataNascitaAssistito");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaVerificaCodiceFiscale regolaValoreNonAmmesso = new RegolaVerificaCodiceFiscale("RegolaVerificaCodiceFiscale", "CF", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(nomeCampoConfronto);
        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn(dataNascitaAssistito);
        List<Esito> result = regolaValoreNonAmmesso.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }


    @Test
    void codiceFiscaleK0() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroDataNascita", "dataNascitaAssistito");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaVerificaCodiceFiscale regolaValoreNonAmmesso = new RegolaVerificaCodiceFiscale("RegolaVerificaCodiceFiscale", "CF", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("SVTOTE81P20P456L");
        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn("1990-10-29");
        List<Esito> result = regolaValoreNonAmmesso.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("CF", e.getErroriValidazione().get(0).getCodice());
        }
    }


    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaVerificaCodiceFiscale regola = new RegolaVerificaCodiceFiscale("RegolaVerificaCodiceFiscale", "CF", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceEsenzione");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceEsenzione", recordMockito));
    }

//    @Test
//    void codiceFiscale2OK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//        Map<String, String> parametri = new HashMap<>();
//        parametri.put("parametroDataNascita", "dataNascitaAssistito");
//        Parametri parametriTest = new Parametri();
//        parametriTest.setParametriMap(parametri);
//
//        RegolaVerificaCodiceFiscale regolaValoreNonAmmesso = new RegolaVerificaCodiceFiscale("RegolaVerificaCodiceFiscale", "CF", "descrizioneErrore", parametriTest);
//        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("GRSVCN86E27A028C");
//        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn("1986-05-27");
//        List<Esito> result = regolaValoreNonAmmesso.valida("nomeCampoConfronto", recordMockito);
//        for (Esito e : result) {
//            assertTrue(e.isValoreEsito());
//        }
//    }


//    @Test
//    void codiceFiscale3OK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//        Map<String, String> parametri = new HashMap<>();
//        parametri.put("parametroDataNascita", "dataNascitaAssistito");
//        Parametri parametriTest = new Parametri();
//        parametriTest.setParametriMap(parametri);
//
//        RegolaVerificaCodiceFiscale regolaValoreNonAmmesso = new RegolaVerificaCodiceFiscale("RegolaVerificaCodiceFiscale", "CF", "descrizioneErrore", parametriTest);
//        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("TRVRTI85D44A028Q");
//        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn("1985-04-04");
//        List<Esito> result = regolaValoreNonAmmesso.valida("nomeCampoConfronto", recordMockito);
//        for (Esito e : result) {
//            assertTrue(e.isValoreEsito());
//        }
//    }

//    @Test
//    void codiceFiscale4OK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//        Map<String, String> parametri = new HashMap<>();
//        parametri.put("parametroDataNascita", "dataNascitaAssistito");
//        Parametri parametriTest = new Parametri();
//        parametriTest.setParametriMap(parametri);
//
//        RegolaVerificaCodiceFiscale regolaValoreNonAmmesso = new RegolaVerificaCodiceFiscale("RegolaVerificaCodiceFiscale", "CF", "descrizioneErrore", parametriTest);
//        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("GRSGPP47L20A028J");
//        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn("1947-07-20");
//        List<Esito> result = regolaValoreNonAmmesso.valida("nomeCampoConfronto", recordMockito);
//        for (Esito e : result) {
//            assertTrue(e.isValoreEsito());
//        }
//    }


//    @Test
//    void codiceFiscale5OK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//        Map<String, String> parametri = new HashMap<>();
//        parametri.put("parametroDataNascita", "dataNascitaAssistito");
//        Parametri parametriTest = new Parametri();
//        parametriTest.setParametriMap(parametri);
//
//        RegolaVerificaCodiceFiscale regolaValoreNonAmmesso = new RegolaVerificaCodiceFiscale("RegolaVerificaCodiceFiscale", "CF", "descrizioneErrore", parametriTest);
//        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("RMNMRA60R62E618D");
//        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn("1960-10-22");
//        List<Esito> result = regolaValoreNonAmmesso.valida("nomeCampoConfronto", recordMockito);
//        for (Esito e : result) {
//            assertTrue(e.isValoreEsito());
//        }
//    }
}