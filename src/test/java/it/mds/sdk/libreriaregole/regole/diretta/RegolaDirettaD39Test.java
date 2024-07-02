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
class RegolaDirettaD39Test {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDirettaD39 regola = new RegolaDirettaD39();
        assertTrue(regola instanceof RegolaDirettaD39);
    }

    @Test
    void noImplicitoNoEsplicitoTipoEsenzioneDiversoCampoObbligatorioOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDirettaD39 regola = new RegolaDirettaD39("RegolaDirettaD39", "D39", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceEsenzione")).thenReturn("cod1234");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoDiEsenzione")).thenReturn("2");
        List<Esito> result = regola.valida("codiceEsenzione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void siImplicitoNoEsplicitoTOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDirettaD39 regola = new RegolaDirettaD39("RegolaDirettaD39", "D39", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceEsenzione")).thenReturn("cod1234");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoDiEsenzione")).thenReturn("2");
        List<Esito> result = regola.valida("codiceEsenzione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void noImplicitoNoEsplicitoTipoEsenzioneDiversoCampoNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDirettaD39 regola = new RegolaDirettaD39("RegolaDirettaD39", "D39", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceEsenzione")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoDiEsenzione")).thenReturn("2");
        List<Esito> result = regola.valida("codiceEsenzione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D39", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDirettaD39 regola = new RegolaDirettaD39("RegolaDirettaD39", "D39", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceEsenzione");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceEsenzione", recordMockito));
    }
}