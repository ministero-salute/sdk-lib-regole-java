/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.diretta;

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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegolaDominioCampoUgualeValoreAnagraficaMeseAnnoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDominioCampoUgualeValoreAnagraficaMeseAnno regola = new RegolaDominioCampoUgualeValoreAnagraficaMeseAnno();
        assertTrue(regola instanceof RegolaDominioCampoUgualeValoreAnagraficaMeseAnno);
    }

    @Test
    void campoDaValidareNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDominioCampoUgualeValoreAnagraficaMeseAnno regola = new RegolaDominioCampoUgualeValoreAnagraficaMeseAnno("RegolaDominioCampoUgualeValoreAnagraficaMeseAnno", "D03", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceFarmaco")).thenReturn(null);
        List<Esito> result = regola.valida("codiceFarmaco", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoCondizionanteDiversoDaParametro() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "tipoFarmaco");
        parametri.put("parametroCondizionante", "1");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDominioCampoUgualeValoreAnagraficaMeseAnno regola = new RegolaDominioCampoUgualeValoreAnagraficaMeseAnno("RegolaDominioCampoUgualeValoreAnagraficaMeseAnno", "D03", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceFarmaco")).thenReturn("cod12345");
        Mockito.when(recordMockito.getCampo("tipoFarmaco")).thenReturn("2");
        List<Esito> result = regola.valida("codiceFarmaco", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDominioCampoUgualeValoreAnagraficaMeseAnno regola = new RegolaDominioCampoUgualeValoreAnagraficaMeseAnno("RegolaDominioCampoUgualeValoreAnagraficaMeseAnno", "D03", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceFarmaco");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceFarmaco", recordMockito));
    }
}