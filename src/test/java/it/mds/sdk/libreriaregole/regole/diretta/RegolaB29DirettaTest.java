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
class RegolaB29DirettaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaB29Diretta regola = new RegolaB29Diretta();
        assertTrue(regola instanceof RegolaB29Diretta);
    }

    @Test
    void testRegolaB29DirettaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaB29Diretta regola = new RegolaB29Diretta("RegolaB29Diretta", "B29", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("tipoDiEsenzione")).thenReturn("2");
        Mockito.when(recordMockito.getCampo("codiceEsenzione")).thenReturn("prova");
        List<Esito> result = regola.valida("tipoDiEsenzione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testRegolaB29DirettaCodEsenzioneNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaB29Diretta regola = new RegolaB29Diretta("RegolaB29Diretta", "B29", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("tipoDiEsenzione")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("codiceEsenzione")).thenReturn("prova");
        List<Esito> result = regola.valida("tipoDiEsenzione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B29", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaB29Diretta regola = new RegolaB29Diretta("RegolaB29Diretta", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("tipoDiEsenzione");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("tipoDiEsenzione", recordMockito));
    }
}