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
public class RegolaListaValoriAmmessiCampoDiversoCampoMaggioreTest {

    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoDaValidare", "valoreCampoDaValidare");
        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("nomeCampoCondizionanteDue", "valoreCampoCondizionanteDue");
        parametri.put("nomeCampoCondizionanteTre", "valoreCampoCondizionanteTre");
        parametri.put("parametroCampoCondizionante", "W005A");

        parametri.put("listaValoriAmmessi", "J003A|J031A");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCampoDiversoCampoMaggiore regola = new RegolaListaValoriAmmessiCampoDiversoCampoMaggiore();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCampoDiversoCampoMaggiore);
    }

    @Test
        // TEST OK: valoreCampoDaValidare presente in lista v.a. e parametroCampoCondizionante!= valoreCampoCondizionante e valoreCampoDaValidare > valoreCampoCondizionanteTre
    void RegolaListaValoriAmmessiCondizionataCampoDiversoSecondaTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn(2D);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("!W005A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("J003A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteTre")).thenReturn(1D);


        var regola = new RegolaListaValoriAmmessiCampoDiversoCampoMaggiore(
                "regolaListaValoriAmmessiCampoDiversoCampoMaggiore", "BR138", "Codice BR138", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: valoreCampoDaValidare presente in lista v.a. e parametroCampoCondizionante!= valoreCampoCondizionante e valoreCampoDaValidare > valoreCampoCondizionanteTre
    void RegolaListaValoriAmmessiCondizionataCampoDiversoCampoValidareNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn(null);

        var regola = new RegolaListaValoriAmmessiCampoDiversoCampoMaggiore(
                "regolaListaValoriAmmessiCampoDiversoCampoMaggiore", "BR138", "Codice BR138", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: valoreCampoDaValidare presente in lista v.a. e parametroCampoCondizionante!= valoreCampoCondizionante e valoreCampoDaValidare < valoreCampoCondizionanteTre
    void RegolaListaValoriAmmessiCondizionataCampoDiversoSecondaTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn(2D);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("!W005A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("J003A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteTre")).thenReturn(3D);


        var regola = new RegolaListaValoriAmmessiCampoDiversoCampoMaggiore(
                "regolaListaValoriAmmessiCampoDiversoCampoMaggiore", "BR138", "Codice BR138", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriAmmessiCampoDiversoCampoMaggiore regola = new RegolaListaValoriAmmessiCampoDiversoCampoMaggiore(
                "regolaListaValoriAmmessiCampoDiversoCampoMaggiore", "BR138", "Codice BR138", parametri
        );
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("valoreCampoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("valoreCampoDaValidare", dtoGenerico));
    }
}