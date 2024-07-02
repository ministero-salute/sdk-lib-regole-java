/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
class RegolaIncoerenzaASLSomministrazioneTipoTrasmissioneTest {
    @Mock
    RecordDtoGenerico recordMockito;
    //se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaASLSomministrazioneTipoTrasmissione regola = new RegolaIncoerenzaASLSomministrazioneTipoTrasmissione();
        assertTrue(regola instanceof RegolaIncoerenzaASLSomministrazioneTipoTrasmissione);
    }

    @Test
    void regolaIncoerenzaASLSomministrazioneTipoTrasmissioneOK1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RegolaIncoerenzaASLSomministrazioneTipoTrasmissione regola = new RegolaIncoerenzaASLSomministrazioneTipoTrasmissione("regolaIncoerenzaASLSomministrazioneTipoTrasmissione", "", "desrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("999");
        Mockito.when(recordMockito.getCampo("tipoTrasmissione")).thenReturn("RT");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void regolaIncoerenzaASLSomministrazioneTipoTrasmissioneOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RegolaIncoerenzaASLSomministrazioneTipoTrasmissione regola = new RegolaIncoerenzaASLSomministrazioneTipoTrasmissione("regolaIncoerenzaASLSomministrazioneTipoTrasmissione", "", "desrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("989");
        Mockito.when(recordMockito.getCampo("tipoTrasmissione")).thenReturn("RT");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaIncoerenzaASLSomministrazioneTipoTrasmissione regola = new RegolaIncoerenzaASLSomministrazioneTipoTrasmissione("regolaIncoerenzaASLSomministrazioneTipoTrasmissione", "", "desrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}