/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.diretta;

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
class RegolaDataNascitaAnonimatoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataNascitaAnonimato regola = new RegolaDataNascitaAnonimato();
        assertTrue(regola instanceof RegolaDataNascitaAnonimato);
    }

    @ParameterizedTest
    @CsvSource({
    	"1986-05-27",
    	",",
    	"9999-12-31"
    })
    void testRegolaDataCorrentePosterioreAnnoMeseRifOK(String dataNascitaAssistito) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "dataErogazione");
        parametri.put("valoreMinimo", "0");
        parametri.put("valoreMassimo", "150");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataNascitaAnonimato regola = new RegolaDataNascitaAnonimato("RegolaDataNascitaAnonimato", "D89", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn(dataNascitaAssistito);
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2022-09-30");
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("id00");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("dataNascitaAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testRegolaDataCorrentePosterioreAnnoMeseRifdataNascitaUgualeadAnonimatoeAnonimatoNonValidoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "dataErogazione");
        parametri.put("valoreMinimo", "0");
        parametri.put("valoreMassimo", "150");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataNascitaAnonimato regola = new RegolaDataNascitaAnonimato("RegolaDataNascitaAnonimato", "D89", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn("9999-12-31");
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2022-09-30");
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("id00");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("dataNascitaAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D89", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testRegolaDataCorrentePosterioreAnnoMeseRifDiffAnnoValoreMassimoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "dataErogazione");
        parametri.put("valoreMinimo", "0");
        parametri.put("valoreMassimo", "35");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataNascitaAnonimato regola = new RegolaDataNascitaAnonimato("RegolaDataNascitaAnonimato", "D89", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn("1986-05-27");
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2022-09-30");
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("id00");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("dataNascitaAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D89", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testRegolaDataAnonimatoExceptionKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "dataErogazione");
        parametri.put("valoreMinimo", "0");
        parametri.put("valoreMassimo", "35");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataNascitaAnonimato regola = new RegolaDataNascitaAnonimato("RegolaDataNascitaAnonimato", "D89", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("dataNascitaAssistito")).thenReturn("1986-13-27");
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2022-09-30");
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("id00");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("dataNascitaAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("999", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataNascitaAnonimato regola = new RegolaDataNascitaAnonimato("RegolaDataCorrentePosterioreAnnoMeseRif", "D89", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataNascitaAssistito");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("dataNascitaAssistito", recordMockito));
    }
}