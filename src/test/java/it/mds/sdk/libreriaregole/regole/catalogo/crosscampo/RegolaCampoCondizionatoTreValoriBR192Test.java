package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.testutil.TestUtils;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
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
class RegolaCampoCondizionatoTreValoriBR192Test {
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
    @CsvSource({
    	"G061A,ADD,N262A",
    	"G061A,NOT_ADD,N262A",
    	"G061A,ADD,NOT_N262A",
    	"G061A,NOT_ADD,NOT_N262A",
    	"NOT_G061A,NOT_ADD,NOT_N262A",
    	"NOT_G061A,NOT_ADD,N262A",
    	"NOT_G061A,ADD,NOT_N262A"
    })
    void regolaCampoCondizionatoTreValori_sampMatCode_legis1_TestOK(String sampMatCodeLegis, 
    		String progID, String progLegalRef) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("sampMatCode_legis")).thenReturn(sampMatCodeLegis);
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn(progID);
        Mockito.when(dtoGenerico.getCampo("progLegalRef")).thenReturn(progLegalRef);

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR192", "Codice BR192", parametri);
        var listaEsiti = regola.valida("sampMatCode_legis", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: sampMatCode_legis != G061A & progID = ADD & progLegalRef = N262A
    void regolaCampoCondizionatoTreValori_notsampMatCode_legisG061A_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("sampMatCode_legis")).thenReturn("NOT_G061A");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("ADD");
        Mockito.when(dtoGenerico.getCampo("progLegalRef")).thenReturn("N262A");

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR192", "Codice BR192", parametri);
        var listaEsiti = regola.valida("sampMatCode_legis", dtoGenerico);

        TestUtils.assertValidationError("sampMatCode_legis", "BR192", "Codice BR192", listaEsiti);
    }

    @Test
        // TEST KO: dto.getCampo -> InvocationTargetException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_InvocationTargetException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("sampMatCode_legis")).thenThrow(InvocationTargetException.class);

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR192", "Codice BR192", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("sampMatCode_legis", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> NoSuchMethodException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_NoSuchMethodException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("sampMatCode_legis")).thenThrow(NoSuchMethodException.class);

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR192", "Codice BR192", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("sampMatCode_legis", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> IllegalAccessException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_IllegalAccessException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("sampMatCode_legis")).thenThrow(IllegalAccessException.class);

        var regola = new RegolaCampoCondizionatoTreValori("regolaCampoCondizionatoTreValori", "BR192", "Codice BR192", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("sampMatCode_legis", dtoGenerico));
    }
}
