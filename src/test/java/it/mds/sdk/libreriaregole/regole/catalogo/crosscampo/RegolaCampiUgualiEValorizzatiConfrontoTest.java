package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegolaCampiUgualiEValorizzatiConfrontoTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;


    @Test
    void validaCostruttore() {
        RegolaCampiUgualiEValorizzatiConfronto regola = new RegolaCampiUgualiEValorizzatiConfronto();
        assertTrue(regola instanceof RegolaCampiUgualiEValorizzatiConfronto);
    }


    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCampiUgualiEValorizzatiConfronto regola = new RegolaCampiUgualiEValorizzatiConfronto("RegolaCampiUgualiEValorizzatiConfronto", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", dtoGenerico));
    }

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "evallowLimit");
        parametri.put("campoCondizionanteDue", "evalCode");
        parametri.put("parametroCondizionante", "J002A");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @ParameterizedTest
    @CsvSource({
    	"1D,2D,J002A",
    	"2D,1D,!J002A",
    	"2D,3D,!J002A"
    })
        // TEST OK: evalCode == J002A & resVal <= evallowLimit
    void RegolaCampiUgualiEValorizzatiConfrontoTestOK(Double resVal, Double evallowLimit, String evalCode) 
    		throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resVal")).thenReturn(resVal);
        Mockito.when(dtoGenerico.getCampo("evallowLimit")).thenReturn(evallowLimit);
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn(evalCode);

        var regola = new RegolaCampiUgualiEValorizzatiConfronto("regolaCampoUgualeAiCaratteriDopoSplit", "BR040/BR041", "Codice BR040/BR041", parametri);
        var listaEsiti = regola.valida("resVal", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: evalCode == J002A & resVal > evallowLimit
    void RegolaCampiUgualiEValorizzatiConfrontoTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resVal")).thenReturn(2D);
        Mockito.when(dtoGenerico.getCampo("evallowLimit")).thenReturn(1D);
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("J002A");

        var regola = new RegolaCampiUgualiEValorizzatiConfronto("regolaCampoUgualeAiCaratteriDopoSplit", "BR040/BR041", "Codice BR040/BR041", parametri);
        var listaEsiti = regola.valida("resVal", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

//    @Test
//        // TEST OK: evalCode != J002A & resVal > evallowLimit
//    void RegolaCampiUgualiEValorizzatiConfrontoTestOKDue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//
//        Mockito.when(dtoGenerico.getCampo("resVal")).thenReturn(2D);
//        Mockito.when(dtoGenerico.getCampo("evallowLimit")).thenReturn(1D);
//        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("!J002A");
//
//        var regola = new RegolaCampiUgualiEValorizzatiConfronto("regolaCampoUgualeAiCaratteriDopoSplit", "BR040/BR041", "Codice BR040/BR041", parametri);
//        var listaEsiti = regola.valida("resVal", dtoGenerico);
//
//        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
//    }

//    @Test
//        // TEST OK: evalCode != J002A & resVal < evallowLimit
//    void RegolaCampiUgualiEValorizzatiConfrontoTestOKTre() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//
//        Mockito.when(dtoGenerico.getCampo("resVal")).thenReturn(2D);
//        Mockito.when(dtoGenerico.getCampo("evallowLimit")).thenReturn(3D);
//        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("!J002A");
//
//        var regola = new RegolaCampiUgualiEValorizzatiConfronto("regolaCampoUgualeAiCaratteriDopoSplit", "BR040/BR041", "Codice BR040/BR041", parametri);
//        var listaEsiti = regola.valida("resVal", dtoGenerico);
//
//        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
//    }
}
