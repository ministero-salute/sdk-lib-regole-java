package it.mds.sdk.libreriaregole.regole.catalogo;

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
class RegolaCheckDigitTargaturaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaCheckDigitTargatura regola = new RegolaCheckDigitTargatura();
        assertTrue(regola instanceof RegolaCheckDigitTargatura);
    }


    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCheckDigitTargatura regola = new RegolaCheckDigitTargatura("RegolaCheckDigitTargatura", "B101", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("targatura");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("targatura", recordMockito));
    }


    @Test
    void campoTargaturaNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaCheckDigitTargatura regola = new RegolaCheckDigitTargatura("RegolaCheckDigitTargatura", "B101", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("targatura")).thenReturn(null);
        List<Esito> result = regola.valida("targatura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoTargaturaCheckDigit0K() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaCheckDigitTargatura regola = new RegolaCheckDigitTargatura("RegolaCheckDigitTargatura", "B101", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("targatura")).thenReturn("0000949439");
        List<Esito> result = regola.valida("targatura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoTargaturaCheckDigitK0() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaCheckDigitTargatura regola = new RegolaCheckDigitTargatura("RegolaCheckDigitTargatura", "B101", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("targatura")).thenReturn("0000949438");
        List<Esito> result = regola.valida("targatura", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B101", e.getErroriValidazione().get(0).getCodice());
        }
    }
}