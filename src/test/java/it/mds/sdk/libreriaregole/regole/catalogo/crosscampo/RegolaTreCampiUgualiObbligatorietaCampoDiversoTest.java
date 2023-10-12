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
public class RegolaTreCampiUgualiObbligatorietaCampoDiversoTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("nomeCampoCondizionanteDue", "valoreCampoCondizionanteDue");
        parametri.put("nomeCampoCondizionanteTre", "valoreCampoCondizionanteTre");

        parametri.put("parametroCampoCondizionante", "ADD");
        parametri.put("parametroCampoCondizionanteDue", "A03CL");
        parametri.put("parametroCampoCondizionanteTre", "RF-00000046-ADD");
        parametri.put("parametroCampoCondizionanteQuattro", "W001A");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
        // TEST OK: EVALLIMITTYPE != W001A
    void RegolaDueCampiUgualiObbligatorietaCampoDiversoTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT-W001A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("ADD");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("A03CL");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteTre")).thenReturn("RF-00000046-ADD");

        var regola = new RegolaTreCampiUgualiObbligatorietaCampoDiverso(
                "regolaTreCampiUgualiObbligatorietaCampoDiverso", "BR201", "Codice BR201", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST KO: EVALLIMITTYPE == W001A
    void RegolaDueCampiUgualiObbligatorietaCampoDiversoTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("W001A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("ADD");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("A03CL");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteTre")).thenReturn("RF-00000046-ADD");

        var regola = new RegolaTreCampiUgualiObbligatorietaCampoDiverso(
                "regolaTreCampiUgualiObbligatorietaCampoDiverso", "BR201", "Codice BR201", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void costruttoreVuoto() {
        RegolaTreCampiUgualiObbligatorietaCampoDiverso regola = new RegolaTreCampiUgualiObbligatorietaCampoDiverso();
        assertTrue(regola instanceof RegolaTreCampiUgualiObbligatorietaCampoDiverso);
    }
    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        var regola = new RegolaTreCampiUgualiObbligatorietaCampoDiverso(
                "TEST", "TEST", "TEST", parametri
        );
        Mockito.when(dtoGenerico.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", dtoGenerico));
    }

}