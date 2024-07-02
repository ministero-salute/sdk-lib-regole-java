/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.date;

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
public class RegolaDataAntecedenteCondizionaleTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataAntecedenteCondizionale regola = new RegolaDataAntecedenteCondizionale();
        assertTrue(regola instanceof RegolaDataAntecedenteCondizionale);
    }

    @Test
    void testDataAnterioreKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataDiNascita");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteCondizionale regola = new RegolaDataAntecedenteCondizionale("dataAntecedenteCondizionale", "E2010", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataDiNascita")).thenReturn("1986-05-27");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("2018-02-25");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E2010" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testDataAnterioreOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataDiNascita");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteCondizionale regola = new RegolaDataAntecedenteCondizionale("dataAntecedenteCondizionale", "E2010","descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataDiNascita")).thenReturn("2018-02-25");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("1986-05-27");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testDataAnterioreNull() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataDiNascita");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaDataAntecedenteCondizionale regola = new RegolaDataAntecedenteCondizionale("dataAntecedenteCondizionale", "E2010","descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataDiNascita")).thenReturn("2022-01-25");
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E2010" ,e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataAntecedenteCondizionale regola = new RegolaDataAntecedenteCondizionale("regolaDataAntecedenteCondizionale", "regolaDataAntecedenteCondizionale", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }

    @Test
    void testDataTrasferimentoExceptionKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataDiNascita");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaDataAntecedenteCondizionale regola = new RegolaDataAntecedenteCondizionale("dataAntecedenteCondizionale", "regolaDataAntecedenteCondizionale","descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataDiNascita")).thenReturn("2022-13-25");
        List<Esito> result = regola.valida("dataDiNascita", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataAntecedenteCondizionale", e.getErroriValidazione().get(0).getCodice());
        }
    }
}