/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;

import org.junit.jupiter.api.Assertions;
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
public class RegolaValoreUgualeCondizionatoDateMinoriTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoDaValidare", "valoreCampoDaValidare");
        parametri.put("valoreCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("valoreCampoCondizionante2", "valoreCampoCondizionante2");
        parametri.put("valoreCampoCondizionante3", "valoreCampoCondizionante3");
        parametri.put("valoreCampoCondizionante4", "valoreCampoCondizionante4");
        parametri.put("valoreCampoCondizionante5", "valoreCampoCondizionante5");

        parametri.put("parametroCampoCondizionante", "Y");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;

    }

    @Test
    // sampId = Y e data <= della successiva
    void RegolaValoreUgualeCondizionatoDateMinoriTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("Y");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("2022-01-01");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante2")).thenReturn("2022-01-02");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante3")).thenReturn("2022-01-03");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante4")).thenReturn("2022-01-04");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante5")).thenReturn("2022-01-05");

        var regola = new RegolaValoreUgualeCondizionatoDateMinori (
                "regolaValoreUgualeCondizionatoDateMinori", "BR086", "Codice BR086", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
        Assertions.assertTrue(true);

    }
    @Test
    //test con sampId != "Y"
    void RegolaValoreUgualeCondizionatoDateMinoriTestOKDue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_Y");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("2022-01-01");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante2")).thenReturn("2022-01-02");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante3")).thenReturn("2022-01-03");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante4")).thenReturn("2022-01-04");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante5")).thenReturn("2022-01-05");

        var regola = new RegolaValoreUgualeCondizionatoDateMinori (
                "regolaValoreUgualeCondizionatoDateMinori", "BR086", "Codice BR086", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));

    }

    @Test
        //test con sampId != "Y"
    void RegolaValoreUgualeCondizionatoDateMinoriTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("Y");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("2022-01-01");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante2")).thenReturn("2022-01-02");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante3")).thenReturn("2022-01-16");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante4")).thenReturn("2022-01-04");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante5")).thenReturn("2022-01-05");

        var regola = new RegolaValoreUgualeCondizionatoDateMinori (
                "regolaValoreUgualeCondizionatoDateMinori", "BR086", "Codice BR086", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));

    }

    @Test
        //test con data facoltativa null
    void RegolaValoreUgualeCondizionatoDateMinoriTestOkTre() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("Y");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("2022-01-01");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante2")).thenReturn("2022-01-02");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante3")).thenReturn(null);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante4")).thenReturn("2022-01-04");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante5")).thenReturn("2022-01-05");

        var regola = new RegolaValoreUgualeCondizionatoDateMinori (
                "regolaValoreUgualeCondizionatoDateMinori", "BR086", "Codice BR086", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));

    }

    @Test
    void validaCostruttore() {
        RegolaValoreUgualeCondizionatoDateMinori regola = new RegolaValoreUgualeCondizionatoDateMinori();
        assertTrue(regola instanceof RegolaValoreUgualeCondizionatoDateMinori);
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaValoreUgualeCondizionatoDateMinori regola = new RegolaValoreUgualeCondizionatoDateMinori("TEST", "TEST", "TEST", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", dtoGenerico));
    }

}