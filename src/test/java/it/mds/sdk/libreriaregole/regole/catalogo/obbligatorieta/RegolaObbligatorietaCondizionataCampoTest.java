/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
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
class RegolaObbligatorietaCondizionataCampoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void testObbligatorietaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "idContatto");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataCampo regola = new RegolaObbligatorietaCondizionataCampo("regolaObbligatorietaCondizionataCampo", "BR045", "Codice BR045", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoContatto")).thenReturn("abc");
        Mockito.when(recordMockito.getCampo("idContatto")).thenReturn("abc");
        List<Esito> result = regola.valida("tipoContatto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCondizionante", "idContatto");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataCampo regola = new RegolaObbligatorietaCondizionataCampo("regolaObbligatorietaCondizionataCampo", "BR045", "Codice BR045", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoContatto")).thenReturn("abc");
        Mockito.when(recordMockito.getCampo("idContatto")).thenReturn(null);
        List<Esito> result = regola.valida("tipoContatto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR045", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaCondizionataCampo regola = new RegolaObbligatorietaCondizionataCampo();
        assertTrue(regola instanceof RegolaObbligatorietaCondizionataCampo);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaCondizionataCampo regola = new RegolaObbligatorietaCondizionataCampo("RegolaObbligatorietaCondizionataCampo", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("tipoContatto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("tipoContatto", recordMockito));
    }

}