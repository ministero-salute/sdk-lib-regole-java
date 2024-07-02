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
class Regola4040Test {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        Regola4040 regola = new Regola4040();
        assertTrue(regola instanceof Regola4040);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Regola4040 regola = new Regola4040("Regola4040", "4040", "Codice 4040", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void testAslOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Regola4040 regola = new Regola4040("Regola4040", "4040", "Codice 4040", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("120");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("123456");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("120");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testAslKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Regola4040 regola = new Regola4040("Regola4040", "4040", "Codice 4040", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("120");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("120");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("4040", e.getErroriValidazione().get(0).getCodice());
        }
    }

}