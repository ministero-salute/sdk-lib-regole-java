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
class RegolaB17DirettaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaB17Diretta regola = new RegolaB17Diretta();
        assertTrue(regola instanceof RegolaB17Diretta);
    }

    @Test
    void testRegolaB17DirettaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaB17Diretta regola = new RegolaB17Diretta("RegolaB17Diretta", "B17", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+Dms+s2mz2P3x90Y=");
        Mockito.when(recordMockito.getCampo("tipologiaCodIdentAssistito")).thenReturn(98);
        Mockito.when(recordMockito.getCampo("validitaCodIdentAssistito")).thenReturn(0);
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testRegolaB17DirettaKOTipologiaCodIdentAss() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaB17Diretta regola = new RegolaB17Diretta("RegolaB17Diretta", "B17", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+Dms+s2mz2P3x90Y=");
        Mockito.when(recordMockito.getCampo("tipologiaCodIdentAssistito")).thenReturn(97);
        Mockito.when(recordMockito.getCampo("validitaCodIdentAssistito")).thenReturn(0);
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B17", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testRegolaB17DirettaOKIdentificativoAss() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaB17Diretta regola = new RegolaB17Diretta("RegolaB17Diretta", "B17", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+Dms+s2mz2P3x=");
        Mockito.when(recordMockito.getCampo("tipologiaCodIdentAssistito")).thenReturn(97);
        Mockito.when(recordMockito.getCampo("validitaCodIdentAssistito")).thenReturn(0);
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaB17Diretta regola = new RegolaB17Diretta("RegolaB17Diretta", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("identificativoAssistito");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("identificativoAssistito", recordMockito));
    }
}