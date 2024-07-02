/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
public class RegolaCampoUgualeListaValoriAmmessiCampoObbligatorioTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("valoreCampoCondizionanteDue", "valoreCampoCondizionanteDue");
        parametri.put("parametroCampoCondizionante", "PNR");

        parametri.put("listaValoriAmmessi", "MS.040.710|MS.040.720");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    // valoreCampoDaValidare not null
    @Test
    void RegolaCampoUgualeListaValoriAmmessiCampoObbligatorioTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_NULL");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("PNR");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("MS.040.710");

        var regola = new RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio(
                "RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio", "BR186", "Codice BR186", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    // listaValoriAmmessi non contiene valoreCampoCondizionanteDue
    @Test
    void RegolaCampoUgualeListaValoriAmmessiCampoObbligatorioTestOKDue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_NULL");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("PNR");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("NOT_MS.040.710");

        var regola = new RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio(
                "RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio", "BR186", "Codice BR186", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    // valoreCampoDaValidare null
    @Test
    void RegolaCampoUgualeListaValoriAmmessiCampoObbligatorioTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn(null);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("PNR");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("MS.040.710");

        var regola = new RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio(
                "RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio", "BR186", "Codice BR186", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void validaCostruttore() {
        RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio regola = new RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio();
        assertTrue(regola instanceof RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio regola = new RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio("RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", dtoGenerico));
    }
}
