/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RegolaObbligatorietaCampoNullListaValoriCampoUgualeTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoDaValidare", "valoreCampoDaValidare");
        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("nomeCampoCondizionante2", "valoreCampoCondizionanteDue");
        parametri.put("nomeCampoCondizionante3", "valoreCampoCondizionanteTre");
        parametri.put("parametroCampoCondizionante", "PSD");
        parametri.put("parametroCampoCondizionanteDue", "Y");

        parametri.put("listaValoriAmmessi", "P004A|P005A");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
        // TEST OK: RESINFO_NOTSUMMED = Y
    void RegolaObbligatorietaCampoNullListaValoriCampoUgualeTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("Y");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("PSD");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn(null);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteTre")).thenReturn("P004A");

        var regola = new RegolaObbligatorietaCampoNullListaValoriCampoUguale(
                "regolaObbligatorietaCampoNullListaValoriCampoUguale", "BR134", "Codice BR134", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: RESINFO_NOTSUMMED != Y
    void RegolaObbligatorietaCampoNullListaValoriCampoUgualeTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_Y");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("PSD");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn(null);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteTre")).thenReturn("P004A");

        var regola = new RegolaObbligatorietaCampoNullListaValoriCampoUguale(
                "regolaObbligatorietaCampoNullListaValoriCampoUguale", "BR134", "Codice BR134", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
        // TEST OK: resLoq not null && RESINFO_NOTSUMMED != Y
    void RegolaObbligatorietaCampoNullListaValoriCampoUgualeTestOKDue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_Y");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("PSD");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("NOT_NULL");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteTre")).thenReturn("P004A");

        var regola = new RegolaObbligatorietaCampoNullListaValoriCampoUguale(
                "regolaObbligatorietaCampoNullListaValoriCampoUguale", "BR134", "Codice BR134", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaCampoNullListaValoriCampoUguale regola = new RegolaObbligatorietaCampoNullListaValoriCampoUguale();
        assertTrue(regola instanceof RegolaObbligatorietaCampoNullListaValoriCampoUguale);
    }
    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        var regola = new RegolaObbligatorietaCampoNullListaValoriCampoUguale(
                "TEST", "TEST", "TEST", parametri
        );
        Mockito.when(dtoGenerico.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", dtoGenerico));
    }
}