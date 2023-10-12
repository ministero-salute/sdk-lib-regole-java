package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.testutil.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegolaCampoCondizionatoDueValoriTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "origReg");
        parametri.put("parametroCondizionante", "OUE");
        parametri.put("parametroDaValidare", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
        // TEST OK: origCountry != IT & origReg == OUE
    void regolaCampoCondizionatoDueValori_OUE_notIT_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("origCountry")).thenReturn("NOT_IT");
        Mockito.when(dtoGenerico.getCampo("origReg")).thenReturn("OUE");

        var regola = new RegolaCampoCondizionatoDueValori("regolaCampoCondizionatoDueValori", "BR129", "Codice BR129", parametri);
        var listaEsiti = regola.valida("origCountry", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: origCountry == IT & origReg != OUE
    void regolaCampoCondizionatoDueValori_notOUE_IT_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("origCountry")).thenReturn("IT");
        Mockito.when(dtoGenerico.getCampo("origReg")).thenReturn("NOT_OUE");

        var regola = new RegolaCampoCondizionatoDueValori("regolaCampoCondizionatoDueValori", "BR129", "Codice BR129", parametri);
        var listaEsiti = regola.valida("origCountry", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST KO: origCountry != IT & origReg != OUE
    void regolaCampoCondizionatoDueValori_notOUE_notIT_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("origCountry")).thenReturn("NOT_IT");
        Mockito.when(dtoGenerico.getCampo("origReg")).thenReturn("NOT_OUE");

        var regola = new RegolaCampoCondizionatoDueValori("regolaCampoCondizionatoDueValori", "BR129", "Codice BR129", parametri);
        var listaEsiti = regola.valida("origCountry", dtoGenerico);

        TestUtils.assertValidationError("origCountry", "BR129", "Codice BR129", listaEsiti);
    }

    @Test
        // TEST KO: origCountry == IT & origReg == OUE
    void regolaCampoCondizionatoDueValori_OUE_IT_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("origCountry")).thenReturn("IT");
        Mockito.when(dtoGenerico.getCampo("origReg")).thenReturn("OUE");

        var regola = new RegolaCampoCondizionatoDueValori("regolaCampoCondizionatoDueValori", "BR129", "Codice BR129", parametri);
        var listaEsiti = regola.valida("origCountry", dtoGenerico);

        TestUtils.assertValidationError("origCountry", "BR129", "Codice BR129", listaEsiti);
    }

    @Test
        // TEST KO: dto.getCampo -> InvocationTargetException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_InvocationTargetException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("origCountry")).thenThrow(InvocationTargetException.class);

        var regola = new RegolaCampoCondizionatoDueValori("regolaCampoCondizionatoDueValori", "BR129", "Codice BR129", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("origCountry", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> NoSuchMethodException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_NoSuchMethodException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("origCountry")).thenThrow(NoSuchMethodException.class);

        var regola = new RegolaCampoCondizionatoDueValori("regolaCampoCondizionatoDueValori", "BR129", "Codice BR129", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("origCountry", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> IllegalAccessException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_IllegalAccessException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("origCountry")).thenThrow(IllegalAccessException.class);

        var regola = new RegolaCampoCondizionatoDueValori("regolaCampoCondizionatoDueValori", "BR129", "Codice BR129", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("origCountry", dtoGenerico));
    }

    @Test
    void validaCostruttore() {
        RegolaCampoCondizionatoDueValori regola = new RegolaCampoCondizionatoDueValori();
        assertTrue(regola instanceof RegolaCampoCondizionatoDueValori);
    }

}
