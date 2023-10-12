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
public class RegolaDueCampiUgualiObbligatorietaCampoDiversoTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @Test
    void validaCostruttore() {
        RegolaDueCampiUgualiObbligatorietaCampoDiverso regola = new RegolaDueCampiUgualiObbligatorietaCampoDiverso();
        assertTrue(regola instanceof RegolaDueCampiUgualiObbligatorietaCampoDiverso);
    }

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoDaValidare", "valoreCampoDaValidare");

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("nomeCampoCondizionanteDue", "valoreCampoCondizionanteDue");

        parametri.put("parametroCampoCondizionante", "ADD");
        parametri.put("parametroCampoCondizionanteDue", "N112A");
        parametri.put("parametroCampoCondizionanteTre", "A163R");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }
    @Test
        // TEST OK: SAMPMATCODE_LEGIS diverso da A163R
    void RegolaDueCampiUgualiObbligatorietaCampoDiversoTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT-A163R");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("ADD");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("N112A");

        var regola = new RegolaDueCampiUgualiObbligatorietaCampoDiverso(
                "regolaDueCampiUgualiObbligatorietaCampoDiverso", "BR189", "Codice BR189", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST KO: SAMPMATCODE_LEGIS uguale a A163R
    void RegolaDueCampiUgualiObbligatorietaCampoDiversoTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("A163R");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("ADD");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("N112A");

        var regola = new RegolaDueCampiUgualiObbligatorietaCampoDiverso(
                "regolaDueCampiUgualiObbligatorietaCampoDiverso", "BR189", "Codice BR189", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDueCampiUgualiObbligatorietaCampoDiverso regola = new RegolaDueCampiUgualiObbligatorietaCampoDiverso("RegolaDueCampiUgualiObbligatorietaCampoDiverso", "AAA", "Codice", null);

        Mockito.when(dtoGenerico.getCampo("campoDaValidare")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", dtoGenerico));
    }
}