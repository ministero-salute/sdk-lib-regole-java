/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.diretta;

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
class RegolaObbligatorietaPeriodoDiRiferimentoDirettaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaPeriodoDiRiferimentoDiretta regola = new RegolaObbligatorietaPeriodoDiRiferimentoDiretta();
        assertTrue(regola instanceof RegolaObbligatorietaPeriodoDiRiferimentoDiretta);
    }

    @Test
    void periodoMinore3MesiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaPeriodoDiRiferimentoDiretta regola = new RegolaObbligatorietaPeriodoDiRiferimentoDiretta("RegolaObbligatorietaPeriodoDiRiferimentoDiretta", "B02", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2999");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("09");
        List<Esito> result = regola.valida("annoDiRiferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void periodoMaggiore2MesiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaPeriodoDiRiferimentoDiretta regola = new RegolaObbligatorietaPeriodoDiRiferimentoDiretta("RegolaObbligatorietaPeriodoDiRiferimentoDiretta", "B02", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("07");
        List<Esito> result = regola.valida("annoDiRiferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B02", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testDateTimeParseException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaPeriodoDiRiferimentoDiretta regola = new RegolaObbligatorietaPeriodoDiRiferimentoDiretta("RegolaObbligatorietaPeriodoDiRiferimentoDiretta", "B02", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("13");
        List<Esito> result = regola.valida("annoDiRiferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B02", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaPeriodoDiRiferimentoDiretta regola = new RegolaObbligatorietaPeriodoDiRiferimentoDiretta("RegolaObbligatorietaPeriodoDiRiferimentoDiretta", "B02", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("annoDiRiferimento");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("annoDiRiferimento", recordMockito));
    }
}