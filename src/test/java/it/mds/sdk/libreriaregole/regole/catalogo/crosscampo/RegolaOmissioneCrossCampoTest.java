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
class RegolaOmissioneCrossCampoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void omissioneStringOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("valoreDipendente","TV");
        parametri.put("campoDipendente" , "modalitaDiTrasmissione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaOmissioneCrossCampo regola = new RegolaOmissioneCrossCampo("omissione","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataInvio")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("modalitaDiTrasmissione")).thenReturn("TV");
        List<Esito> result = regola.valida("dataInvio",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void omissioneStringKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("valoreDipendente","TV");
        parametri.put("campoDipendente" , "modalitaDiTrasmissione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaOmissioneCrossCampo regola = new RegolaOmissioneCrossCampo("omissione","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataInvio")).thenReturn("2022-12-12");
        Mockito.when(recordMockito.getCampo("modalitaDiTrasmissione")).thenReturn("TV");
        List<Esito> result = regola.valida("dataInvio",recordMockito);
        for(Esito e : result){
            assertFalse(e.isValoreEsito()) ;
            assertEquals("E01" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void campoDipendenteDiversoDaValoreDipendenteRecordNonValorizzato() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("valoreDipendente","TV");
        parametri.put("campoDipendente" , "modalitaDiTrasmissione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaOmissioneCrossCampo regola = new RegolaOmissioneCrossCampo("omissione","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("modalitaDiTrasmissione")).thenReturn("MR");
        List<Esito> result = regola.valida("dataInvio",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoDipendenteDiversoDaValoreDipendenteRecordValorizzato() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("valoreDipendente","TV");
        parametri.put("campoDipendente" , "modalitaDiTrasmissione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaOmissioneCrossCampo regola = new RegolaOmissioneCrossCampo("omissione","E01","descrizioneErrore",parametriTest);
        //Mockito.when(recordMockito.getCampo("dataInvio")).thenReturn("2022-02-12");
        Mockito.when(recordMockito.getCampo("modalitaDiTrasmissione")).thenReturn("MR");
        List<Esito> result = regola.valida("dataInvio",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void omissioneIntegerOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("valoreDipendente","4");
        parametri.put("campoDipendente" , "modalitaDiTrasmissione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaOmissioneCrossCampo regola = new RegolaOmissioneCrossCampo("omissione","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataInvio")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("modalitaDiTrasmissione")).thenReturn(4);
        List<Esito> result = regola.valida("dataInvio",recordMockito);
        for(Esito e : result){
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void omissioneIntegerKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("valoreDipendente","4");
        parametri.put("campoDipendente" , "modalitaDiTrasmissione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaOmissioneCrossCampo regola = new RegolaOmissioneCrossCampo("omissione","E01","descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataInvio")).thenReturn(5);
        Mockito.when(recordMockito.getCampo("modalitaDiTrasmissione")).thenReturn(4);
        List<Esito> result = regola.valida("dataInvio",recordMockito);
        for(Esito e : result){
            assertFalse(e.isValoreEsito()) ;
            assertEquals("E01" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void costruttoreVuoto() {
        RegolaOmissioneCrossCampo regola = new RegolaOmissioneCrossCampo();
        assertTrue(regola instanceof RegolaOmissioneCrossCampo);
    }
    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("valoreDipendente","4");
        parametri.put("campoDipendente" , "modalitaDiTrasmissione");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        var regola = new RegolaOmissioneCrossCampo(
                "TEST", "TEST", "TEST", parametriTest
        );
        Mockito.when(recordMockito.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));
    }

}