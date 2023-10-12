package it.mds.sdk.libreriaregole.regole.diretta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;

@ExtendWith(MockitoExtension.class)
class RegolaDataCorrentePosterioreAnnoMeseRifTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataCorrentePosterioreAnnoMeseRif regola = new RegolaDataCorrentePosterioreAnnoMeseRif();
        assertTrue(regola instanceof RegolaDataCorrentePosterioreAnnoMeseRif);
    }

    @Test
    void testRegolaDataCorrentePosterioreAnnoMeseRifOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "annoDiRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataCorrentePosterioreAnnoMeseRif regola = new RegolaDataCorrentePosterioreAnnoMeseRif("RegolaDataCorrentePosterioreAnnoMeseRif", "B03", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        List<Esito> result = regola.valida("meseDiRiferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @ParameterizedTest
    @CsvSource({"02,,999",
    			"10,2062,B03",
    			"10,2023,B03",
    			"13,2023,B03"})
    void testRegolaDataCorrentePosterioreAnnoNullKO(String mdr, String adr, String value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "annoDiRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataCorrentePosterioreAnnoMeseRif regola = new RegolaDataCorrentePosterioreAnnoMeseRif("RegolaDataCorrentePosterioreAnnoMeseRif", "B03", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn(mdr);
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn(adr);
        List<Esito> result = regola.valida("meseDiRiferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals(value, e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testRegolaDataCorrentePosterioreAnnoMeseRifAnnoCorrMaggioreAnnoRifOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "annoDiRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDataCorrentePosterioreAnnoMeseRif regola = new RegolaDataCorrentePosterioreAnnoMeseRif("RegolaDataCorrentePosterioreAnnoMeseRif", "B03", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2021");
        List<Esito> result = regola.valida("meseDiRiferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataCorrentePosterioreAnnoMeseRif regola = new RegolaDataCorrentePosterioreAnnoMeseRif("RegolaDataCorrentePosterioreAnnoMeseRif", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("meseDiRiferimento");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("meseDiRiferimento", recordMockito));
    }

}