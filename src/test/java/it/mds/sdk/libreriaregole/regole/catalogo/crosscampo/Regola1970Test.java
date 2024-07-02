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
class Regola1970Test {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        Regola1970 regola = new Regola1970();
        assertTrue(regola instanceof Regola1970);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Regola1970 regola = new Regola1970("Regola1970", "1970", "Codice 1970", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void testAslOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Regola1970 regola = new Regola1970("Regola1970", "1970", "Codice 1970", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("120");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }


    @Test
    void testAslKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Regola1970 regola = new Regola1970("Regola1970", "1970", "Codice 1970", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("120");
        Mockito.when(recordMockito.getCampo("codiceComuneResidenza")).thenReturn("123456");
        Mockito.when(recordMockito.getCampo("codiceRegioneResidenza")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("1970", e.getErroriValidazione().get(0).getCodice());
        }
    }
}