package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.testutil.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegolaCampoCondizionatoTreValoriBR191Test {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante1", "progID");
        parametri.put("campoCondizionante2", "progLegalRef");
        parametri.put("parametroCondizionante1", "ADD");
        parametri.put("parametroCondizionante2", "N262A");
        parametri.put("listaValoriAmmessi", "G061A");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @ParameterizedTest
    @CsvSource({"G061A,ADD,N262A",
    			"G061A,NOT_ADD,N262A",
    			"G061A,ADD,NOT_N262A",
    			"G061A,NOT_ADD,NOT_N262A",
    			"NOT_G061A,NOT_ADD,NOT_N262A",
    			"NOT_G061A,NOT_ADD,N262A",
    			"NOT_G061A,ADD,NOT_N262A"
    			})
        // TEST OK: resUnit 
    void regolaCampoCondizionatoTreValori_resUnit1_TestOK(String resUnit,
    		String progID, String progLegalRef) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resUnit")).thenReturn(resUnit);
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn(progID);
        Mockito.when(dtoGenerico.getCampo("progLegalRef")).thenReturn(progLegalRef);

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR191", "Codice BR191", parametri);
        var listaEsiti = regola.valida("resUnit", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }



    @Test
        // TEST OK: resUnit != G061A & progID = ADD & progLegalRef = N262A
    void regolaCampoCondizionatoTreValori_notresUnitG061A_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resUnit")).thenReturn("NOT_G061A");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("ADD");
        Mockito.when(dtoGenerico.getCampo("progLegalRef")).thenReturn("N262A");

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR191", "Codice BR191", parametri);
        var listaEsiti = regola.valida("resUnit", dtoGenerico);

        TestUtils.assertValidationError("resUnit", "BR191", "Codice BR191", listaEsiti);
    }

    @Test
        // TEST KO: dto.getCampo -> InvocationTargetException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_InvocationTargetException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resUnit")).thenThrow(InvocationTargetException.class);

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR191", "Codice BR191", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("resUnit", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> NoSuchMethodException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_NoSuchMethodException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resUnit")).thenThrow(NoSuchMethodException.class);

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR191", "Codice BR191", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("resUnit", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> IllegalAccessException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_IllegalAccessException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resUnit")).thenThrow(IllegalAccessException.class);

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR191", "Codice BR191", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("resUnit", dtoGenerico));
    }
}