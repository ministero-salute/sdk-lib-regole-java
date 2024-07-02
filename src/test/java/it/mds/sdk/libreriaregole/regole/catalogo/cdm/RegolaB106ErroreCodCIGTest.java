/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.cdm;

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
class RegolaB106ErroreCodCIGTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaB106ErroreCodCIG regola = new RegolaB106ErroreCodCIG();
        assertTrue(regola instanceof RegolaB106ErroreCodCIG);
    }

    @Test
    void testVerificaRegexOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaB106ErroreCodCIG regola = new RegolaB106ErroreCodCIG("regolaB106ErroreCodCIG", "B106", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceCig")).thenReturn("6210735CA5");
        List<Esito> result = regola.valida("codiceCig", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testVerificaRegexOKCampoNull() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaB106ErroreCodCIG regola = new RegolaB106ErroreCodCIG("regolaB106ErroreCodCIG", "B106", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceCig")).thenReturn(null);
        List<Esito> result = regola.valida("codiceCig", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testVerificaRegexKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaB106ErroreCodCIG regola = new RegolaB106ErroreCodCIG("regolaB106ErroreCodCIG", "B106", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceCig")).thenReturn("6210735CA500");
        List<Esito> result = regola.valida("codiceCig", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B106", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaB106ErroreCodCIG regola = new RegolaB106ErroreCodCIG("regolaB106ErroreCodCIG", "B106", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }

}