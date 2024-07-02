/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegolaDominioValoreAnagraficaCCBetaTest {


    @Mock
    RecordDtoGenerico dtoGenerico;

    @Test
    void validaCostruttore() {
        RegolaDominioValoreAnagraficaCCBeta regola = new RegolaDominioValoreAnagraficaCCBeta();
        assertTrue(regola instanceof RegolaDominioValoreAnagraficaCCBeta);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("progId", "progId");
        parametri.put("paramCode", "paramCode");
        parametri.put("anmethtype", "anmethtype");
        parametri.put("paramPNR", "paramPNR");
        parametri.put("AT06A", "AT06A");
        parametri.put("paramPesticida", "paramPesticida");
        parametri.put("paramContaminante", "paramContaminante");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        Mockito.when(dtoGenerico.getCampo("progId")).thenReturn("valoreParamPNR");
        Mockito.when(dtoGenerico.getCampo("anmethtype")).thenReturn("valoreParamAT06A");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("notPesticidaAndNotContaminante");

        RegolaDominioValoreAnagraficaCCBeta regola = new RegolaDominioValoreAnagraficaCCBeta("RegolaDominioValoreAnagraficaCCBeta", "CCBeta", "descrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("codiceAziendaSanitariaErogante");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceAziendaSanitariaErogante", dtoGenerico));
    }

    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("progId", "progId");
        parametri.put("paramCode", "paramCode");
        parametri.put("anmethtype", "anmethtype");

        parametri.put("paramPNR", "valoreParamPNR");
        parametri.put("AT06A", "valoreParamAT06A");
        parametri.put("paramPesticida", "pesticida");
        parametri.put("paramContaminante", "contaminante");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
        // TEST OK: Con condizione equals soddisfatta e ccBeta not null
    void regolaDominioValoreAnagraficaCCBetaTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("progId")).thenReturn("valoreParamPNR");
        Mockito.when(dtoGenerico.getCampo("anmethtype")).thenReturn("valoreParamAT06A");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("notPesticidaAndNotContaminante");
        Mockito.when(dtoGenerico.getCampo("ccBeta")).thenReturn("ccBetaNotNull");

        var regola = new RegolaDominioValoreAnagraficaCCBeta("regolaDominioValoreAnagraficaCCBeta", "BR082", "Codice BR082", parametri);
        var listaEsiti = regola.valida("ccBeta", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: Con condizione equals non soddisfatta poichè pesticida, non è richiesto ccBeta != null
    void regolaDominioValoreAnagraficaCCBetaTestOK2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("progId")).thenReturn("valoreParamPNR");
        Mockito.when(dtoGenerico.getCampo("anmethtype")).thenReturn("valoreParamAT06A");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("pesticida");
        Mockito.when(dtoGenerico.getCampo("ccBeta")).thenReturn("ccBetaNotNull");

        var regola = new RegolaDominioValoreAnagraficaCCBeta("regolaDominioValoreAnagraficaCCBeta", "BR082", "Codice BR082", parametri);
        var listaEsiti = regola.valida("ccBeta", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST KO: Con condizione equals soddisfatta e ccBeta null
    void regolaDominioValoreAnagraficaCCBetaTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("progId")).thenReturn("valoreParamPNR");
        Mockito.when(dtoGenerico.getCampo("anmethtype")).thenReturn("valoreParamAT06A");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("notPesticidaAndNotContaminante");
        Mockito.when(dtoGenerico.getCampo("ccBeta")).thenReturn(null);

        var regola = new RegolaDominioValoreAnagraficaCCBeta("regolaDominioValoreAnagraficaCCBeta", "BR082", "Codice BR082", parametri);
        var listaEsiti = regola.valida("ccBeta", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }
}
