/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.date;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaDataCorrentePosterioreAnnoMeseRif;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaDataNascitaAnonimato;
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
class RegolaDataAntecedenteOUgualeTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataAntecedenteOUguale regola = new RegolaDataAntecedenteOUguale();
        assertTrue(regola instanceof RegolaDataAntecedenteOUguale);
    }

    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataErogazione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteOUguale regola = new RegolaDataAntecedenteOUguale("RegolaDataAntecedenteOUguale", "B23", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataPrescrizione")).thenReturn("2080-01-25");
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2000-01-25");
        List<Esito> result = regola.valida("dataPrescrizione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B23", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataErogazione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteOUguale regola = new RegolaDataAntecedenteOUguale("RegolaDataAntecedenteOUguale", "B23", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataPrescrizione")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2000-01-25");
        List<Esito> result = regola.valida("dataPrescrizione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void validaOKDataDaComparareisNotAfter() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataErogazione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteOUguale regola = new RegolaDataAntecedenteOUguale("RegolaDataAntecedenteOUguale", "B23", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataPrescrizione")).thenReturn("2000-01-25");
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2080-01-25");
        List<Esito> result = regola.valida("dataPrescrizione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testRegolaDataExceptionKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<String, String>();
        parametri.put("nomeCampoValidatore", "dataErogazione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataAntecedenteOUguale regola = new RegolaDataAntecedenteOUguale("RegolaDataAntecedenteOUguale", "B23", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("dataPrescrizione")).thenReturn("2000-13-25");
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2080-01-25");
        List<Esito> result = regola.valida("dataPrescrizione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B23", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataAntecedenteOUguale regola = new RegolaDataAntecedenteOUguale("RegolaDataAntecedenteOUguale", "B23", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataPrescrizione");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("dataPrescrizione", recordMockito));
    }
}