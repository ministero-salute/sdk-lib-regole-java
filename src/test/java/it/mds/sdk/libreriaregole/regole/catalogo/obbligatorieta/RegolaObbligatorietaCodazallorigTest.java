/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

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
public class RegolaObbligatorietaCodazallorigTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("pnrParam", "progIdString");
        parametri.put("itParam", "origCountryString");
        parametri.put("st90Param", "sampStrategyString1");
        parametri.put("origCountry", "origCountry");
        parametri.put("samppoint", "samppoint");
        parametri.put("sampStrategy", "sampStrategy");
        parametri.put("progId", "progId");
        parametri.put("listaValoriAmmessi", "MS.B10.100|MS.B20.100|MS.B30.100|MS.BA0.100|MS.BA0.300|MS.B90.40|MS.060.400|MS.B80.600|MS.040.500");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaCodazallorig regola = new RegolaObbligatorietaCodazallorig();
        assertTrue(regola instanceof RegolaObbligatorietaCodazallorig);
    }

    @Test
    void regolaObbligatorietaCodazallorigTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaObbligatorietaCodazallorig regola = new RegolaObbligatorietaCodazallorig("regolaObbligatorietaCodazallorig", "BR153", "Codice BR153", parametri);

        Mockito.when(dtoGenerico.getCampo("progId")).thenReturn("progIdString");
        Mockito.when(dtoGenerico.getCampo("origCountry")).thenReturn("origCountryString");
        Mockito.when(dtoGenerico.getCampo("samppoint")).thenReturn("MS.B10.100");
        Mockito.when(dtoGenerico.getCampo("sampStrategy")).thenReturn("sampStrategyString");
        Mockito.when(dtoGenerico.getCampo("codazallorig")).thenReturn("codazallorigString");

        var listaEsiti = regola.valida("codazallorig", dtoGenerico);
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void regolaObbligatorietaCodazallorigTestKOProgId() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaObbligatorietaCodazallorig regola = new RegolaObbligatorietaCodazallorig("regolaObbligatorietaCodazallorig", "BR153", "Codice BR153", parametri);

        Mockito.when(dtoGenerico.getCampo("progId")).thenReturn("progIdStringNOT");
        Mockito.when(dtoGenerico.getCampo("origCountry")).thenReturn("origCountryString");
        Mockito.when(dtoGenerico.getCampo("samppoint")).thenReturn("MS.B10.100");
        Mockito.when(dtoGenerico.getCampo("sampStrategy")).thenReturn("sampStrategyString");

        var listaEsiti = regola.valida("codazallorig", dtoGenerico);
        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void regolaObbligatorietaCodazallorigTestKOCodazallorig() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaObbligatorietaCodazallorig regola = new RegolaObbligatorietaCodazallorig("regolaObbligatorietaCodazallorig", "BR153", "Codice BR153", parametri);

        Mockito.when(dtoGenerico.getCampo("progId")).thenReturn("progIdString");
        Mockito.when(dtoGenerico.getCampo("origCountry")).thenReturn("origCountryString");
        Mockito.when(dtoGenerico.getCampo("samppoint")).thenReturn("MS.B10.100");
        Mockito.when(dtoGenerico.getCampo("sampStrategy")).thenReturn("sampStrategyString");
        Mockito.when(dtoGenerico.getCampo("codazallorig")).thenReturn("");

        var listaEsiti = regola.valida("codazallorig", dtoGenerico);
        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaCodazallorig regola = new RegolaObbligatorietaCodazallorig("regolaObbligatorietaCodazallorig", "BR153", "Codice BR153", parametri);

        Mockito.when(dtoGenerico.getCampo("progId")).thenReturn("progIdString");
        Mockito.when(dtoGenerico.getCampo("origCountry")).thenReturn("origCountryString");
        Mockito.when(dtoGenerico.getCampo("samppoint")).thenReturn("MS.B10.100");
        Mockito.when(dtoGenerico.getCampo("sampStrategy")).thenReturn("sampStrategyString");
        Mockito.when(dtoGenerico.getCampo("codazallorig")).thenReturn("codazallorigString");

        var listaEsiti = regola.valida("codazallorig", dtoGenerico);
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));

        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("codazallorig");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codazallorig", dtoGenerico));
    }

}
