package it.mds.sdk.libreriaregole.regole.catalogo.date;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaBR2095Test {
    @Mock
    RecordDtoGenerico recordMockito;

    @Mock
    CampiInputBean campiInputBean;

    @Test
    void validaCostruttore() {
        RegolaBR2095 regola = new RegolaBR2095();
        assertTrue(regola instanceof RegolaBR2095);
    }

    @ParameterizedTest
    @CsvSource({
    	"Q1",
    	"Q2",
    	"Q3",
    	"Q4"
    })
    void testDataDiversoNullMonthDiversoPrimoTrimestreKO(String PeriodoRiferimentoInput) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaBR2095 regola = new RegolaBR2095("RegolaBR2095", "RegolaBR2095", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataDecesso")).thenReturn("1975-12-25");
        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn(PeriodoRiferimentoInput);
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("dataDecesso", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaBR2095", e.getErroriValidazione().get(0).getCodice());
        }
    }
//    @Test
//    void testDataDiversoNullMonthDiversoSecondoTrimestreKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//
//        RegolaBR2095 regola = new RegolaBR2095("RegolaBR2095", "RegolaBR2095", "descrizioneErrore", null);
//        Mockito.when(recordMockito.getCampo("dataDecesso")).thenReturn("1975-12-25");
//        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn("Q2");
//        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
//        List<Esito> result = regola.valida("dataDecesso", recordMockito);
//        for (Esito e : result) {
//            assertFalse(e.isValoreEsito());
//            assertEquals("RegolaBR2095", e.getErroriValidazione().get(0).getCodice());
//        }
//    }
//    @Test
//    void testDataDiversoNullMonthDiversoTerzoTrimestreKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//
//        RegolaBR2095 regola = new RegolaBR2095("RegolaBR2095", "RegolaBR2095", "descrizioneErrore", null);
//        Mockito.when(recordMockito.getCampo("dataDecesso")).thenReturn("1975-12-25");
//        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn("Q3");
//        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
//        List<Esito> result = regola.valida("dataDecesso", recordMockito);
//        for (Esito e : result) {
//            assertFalse(e.isValoreEsito());
//            assertEquals("RegolaBR2095", e.getErroriValidazione().get(0).getCodice());
//        }
//    }
//    @Test
//    void testDataDiversoNullMonthDiversoQuartoTrimestreKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//
//        RegolaBR2095 regola = new RegolaBR2095("RegolaBR2095", "RegolaBR2095", "descrizioneErrore", null);
//        Mockito.when(recordMockito.getCampo("dataDecesso")).thenReturn("1975-12-25");
//        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn("Q4");
//        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
//        List<Esito> result = regola.valida("dataDecesso", recordMockito);
//        for (Esito e : result) {
//            assertFalse(e.isValoreEsito());
//            assertEquals("RegolaBR2095", e.getErroriValidazione().get(0).getCodice());
//        }
//    }
    @Test
    void testDataNullOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaBR2095 regola = new RegolaBR2095("RegolaBR2095", "RegolaBR2095", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataDecesso")).thenReturn(null);
        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn("Q1");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("dataDecesso", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaBR2095 regola = new RegolaBR2095("regolaBR2095", "regolaBR2095", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataDecesso");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataDecesso", recordMockito));
    }

    @Test
    void testDataExceptionKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaBR2095 regola = new RegolaBR2095("regolaBR2095", "regolaBR2095", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataDecesso")).thenReturn("1975-13-25");
        List<Esito> result = regola.valida("dataDecesso", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaBR2095", e.getErroriValidazione().get(0).getCodice());
        }
    }
}