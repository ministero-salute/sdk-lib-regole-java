/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaDirettaD39;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaValoreNonAmmessoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaValoreNonAmmesso regolaValoreNonAmmesso = new RegolaValoreNonAmmesso();
        assertTrue(regolaValoreNonAmmesso instanceof RegolaValoreNonAmmesso);
    }

    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroNonAmmesso", "999999");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaValoreNonAmmesso regolaValoreNonAmmesso = new RegolaValoreNonAmmesso("valoreNonAmmesso", "E6102", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("999998");
        List<Esito> result = regolaValoreNonAmmesso.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroNonAmmesso", "999999");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaValoreNonAmmesso regolaValoreNonAmmesso = new RegolaValoreNonAmmesso("valoreNonAmmesso", "E6102", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("999999");
        List<Esito> result = regolaValoreNonAmmesso.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {

            assertFalse(e.isValoreEsito());
            assertEquals("E6102" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaValoreNonAmmesso regola = new RegolaValoreNonAmmesso("valoreNonAmmesso", "E6102", "descrizioneErrore",null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceEsenzione");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceEsenzione", recordMockito));
    }
}