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
public class RegolaCampoUgualeAiCaratteriDopoSplitTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "campoCondizionante");
        parametri.put("campoDaValidare", "campoDaValidare");
        parametri.put("splitter", "_");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
        // TEST OK: Con condizione equals soddisfatta
    void RegolaCampoUgualeAiCaratteriDopoSplitTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("campoDaValidare")).thenReturn("Foo_StringaUguale");
        Mockito.when(dtoGenerico.getCampo("campoCondizionante")).thenReturn("StringaUguale");

        var regola = new RegolaCampoUgualeAiCaratteriDopoSplit("regolaCampoUgualeAiCaratteriDopoSplit", "BR054", "Codice BR054", parametri);
        var listaEsiti = regola.valida("campoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void RegolaCampoUgualeAiCaratteriDopoSplitTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("campoDaValidare")).thenReturn("Foo_StringaUguale");
        Mockito.when(dtoGenerico.getCampo("campoCondizionante")).thenReturn("StringaNonUguale");

        var regola = new RegolaCampoUgualeAiCaratteriDopoSplit("regolaCampoUgualeAiCaratteriDopoSplit", "BR054", "Codice BR054", parametri);
        var listaEsiti = regola.valida("campoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void validaCostruttore() {
        RegolaCampoUgualeAiCaratteriDopoSplit regola = new RegolaCampoUgualeAiCaratteriDopoSplit();
        assertTrue(regola instanceof RegolaCampoUgualeAiCaratteriDopoSplit);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCampoUgualeAiCaratteriDopoSplit regola = new RegolaCampoUgualeAiCaratteriDopoSplit("RegolaCampoUgualeAiCaratteriDopoSplit", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", dtoGenerico));
    }
}
