package it.mds.sdk.libreriaregole.regole.catalogo.date;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaBR2080Test {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaBR2080 regola = new RegolaBR2080();
        assertTrue(regola instanceof RegolaBR2080);
    }

    @Test
    void testDataTrasmissioneODataNascitaNullOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaBR2080 regola = new RegolaBR2080("regolaBR2080", "regolaBR2080", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataTrasmissione")).thenReturn("1978-01-25");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("RE");
        List<Esito> result = regola.valida("dataNascita", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testDataTrasmissioneDiversoNullDataAfterKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaBR2080 regola = new RegolaBR2080("regolaBR2080", "regolaBR2080", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("dataTrasmissione")).thenReturn("1978-01-25");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("RE");
        List<Esito> result = regola.valida("dataNascita", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaBR2080", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testModalitaDiversaREOk() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaBR2080 regola = new RegolaBR2080("regolaBR2080", "regolaBR2080", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("1979-01-25");
        Mockito.when(recordMockito.getCampo("dataTrasmissione")).thenReturn("1978-01-25");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("RT");
        List<Esito> result = regola.valida("dataNascita", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaBR2080 regola = new RegolaBR2080("regolaBR2080", "regolaBR2080", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataNascita");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataNascita", recordMockito));
    }

    @Test
    void testDataExceptionKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaBR2080 regola = new RegolaBR2080("regolaBR2080", "regolaBR2080", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("1979-13-25");
        Mockito.when(recordMockito.getCampo("dataTrasmissione")).thenReturn("1978-01-25");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("RE");
        List<Esito> result = regola.valida("dataNascita", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaBR2080", e.getErroriValidazione().get(0).getCodice());
        }
    }
}