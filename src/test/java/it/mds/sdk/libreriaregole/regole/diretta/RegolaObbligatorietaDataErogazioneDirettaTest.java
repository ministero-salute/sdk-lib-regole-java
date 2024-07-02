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
class RegolaObbligatorietaDataErogazioneDirettaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaDataErogazioneDiretta regola = new RegolaObbligatorietaDataErogazioneDiretta();
        assertTrue(regola instanceof RegolaObbligatorietaDataErogazioneDiretta);
    }

    @Test
    void periodoCompresoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaDataErogazioneDiretta regola = new RegolaObbligatorietaDataErogazioneDiretta("RegolaObbligatorietaDataErogazioneDiretta", "B20", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2022-09-01");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("10");
        List<Esito> result = regola.valida("dataErogazione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void periodoCompresoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaDataErogazioneDiretta regola = new RegolaObbligatorietaDataErogazioneDiretta("RegolaObbligatorietaDataErogazioneDiretta", "B20", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2022-09-01");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("05");
        List<Esito> result = regola.valida("dataErogazione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B20", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testDateTimeParseException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaDataErogazioneDiretta regola = new RegolaObbligatorietaDataErogazioneDiretta("RegolaObbligatorietaDataErogazioneDiretta", "B20", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataErogazione")).thenReturn("2022-09-01");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("15");
        List<Esito> result = regola.valida("dataErogazione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B20", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaDataErogazioneDiretta regola = new RegolaObbligatorietaDataErogazioneDiretta("RegolaObbligatorietaDataErogazioneDiretta", "B20", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataErogazione");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataErogazione", recordMockito));
    }
}