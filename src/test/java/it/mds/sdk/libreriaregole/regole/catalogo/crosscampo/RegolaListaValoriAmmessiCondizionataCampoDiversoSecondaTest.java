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
public class RegolaListaValoriAmmessiCondizionataCampoDiversoSecondaTest {

    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoDaValidare", "valoreCampoDaValidare");
        parametri.put("valoreCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("parametroCampoCondizionante", "IT");

        parametri.put("listaValoriAmmessi", "MS.F00.010|MS.F00.020");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
        // TEST OK: valoreCampoDaValidare presente in lista v.a. e origCountry != IT
    void RegolaListaValoriAmmessiCondizionataCampoDiversoSecondaTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("MS.F00.010");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("FR");


        var regola = new RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda(
                "regolaListaValoriAmmessiCondizionataCampoDiversoSeconda", "BR127", "Codice BR127", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST KO: valoreCampoDaValidare presente in lista v.a. e origCountry == IT
    void RegolaListaValoriAmmessiCondizionataCampoDiversoSecondaTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("MS.F00.010");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("IT");


        var regola = new RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda(
                "regolaListaValoriAmmessiCondizionataCampoDiversoSeconda", "BR127", "Codice BR127", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
        // TEST OK: valoreCampoDaValidare non presente in lista
    void RegolaListaValoriAmmessiCondizionataCampoDiversoSecondaTestOkDue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_IN_LIST");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("IT");


        var regola = new RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda(
                "regolaListaValoriAmmessiCondizionataCampoDiversoSeconda", "BR127", "Codice BR127", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda regola = new RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda("regolaListaValoriAmmessiCondizionataCampoDiversoSeconda", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("valoreCampoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("valoreCampoDaValidare", dtoGenerico));
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda regola = new RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda);
    }
}
