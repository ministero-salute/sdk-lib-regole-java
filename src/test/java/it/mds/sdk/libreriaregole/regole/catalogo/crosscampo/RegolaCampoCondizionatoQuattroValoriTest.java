/* SPDX-License-Identifier: BSD-3-Clause */

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
class RegolaCampoCondizionatoQuattroValoriTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante1", "progID");
        parametri.put("campoCondizionante2", "paramCode");
        parametri.put("campoCondizionante3", "evalCode");
        parametri.put("parametroCondizionante1", "MCG");
        parametri.put("parametroCondizionante2", "RF-00011484-PAR");
        parametri.put("parametroCondizionante3", "J041A");
        parametri.put("parametroDaValidare", "POS");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
        // TEST OK: resQualValue = POS & progID = MCG & paramCode = RF-00011484-PAR & evalCode = J041A
    void regolaCampoCondizionatoQuattroValori_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenReturn("POS");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("MCG");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("RF-00011484-PAR");
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("J041A");

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: resQualValue = POS & progID != MCG & paramCode = RF-00011484-PAR & evalCode = J041A
    void regolaCampoCondizionatoQuattroValori_notParametro1_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenReturn("POS");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("NOT_MCG");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("RF-00011484-PAR");
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("J041A");

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: resQualValue = POS & progID = MCG & paramCode != RF-00011484-PAR & evalCode = J041A
    void regolaCampoCondizionatoQuattroValori_notParametro2_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenReturn("POS");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("MCG");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("NOT_RF-00011484-PAR");
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("J041A");

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: resQualValue = POS & progID != MCG & paramCode != RF-00011484-PAR & evalCode = J041A
    void regolaCampoCondizionatoQuattroValori_notParametro1e2_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenReturn("POS");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("NOT_MCG");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("NOT_RF-00011484-PAR");
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("J041A");

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: resQualValue != POS & progID != MCG & paramCode != RF-00011484-PAR & evalCode = J041A
    void regolaCampoCondizionatoQuattroValori_notParametro1e2eDaValidare_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenReturn("NOT_POS");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("NOT_MCG");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("NOT_RF-00011484-PAR");
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("J041A");

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: resQualValue = POS & progID = MCG & paramCode = RF-00011484-PAR & evalCode != J041A
    void regolaCampoCondizionatoQuattroValori_notParametro3_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenReturn("POS");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("MCG");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("RF-00011484-PAR");
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("NOT_J041A");

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: resQualValue != POS & progID = MCG & paramCode = RF-00011484-PAR & evalCode != J041A
    void regolaCampoCondizionatoQuattroValori_notParametro3eDaValidare_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenReturn("NOT_POS");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn("MCG");
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn("RF-00011484-PAR");
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("NOT_J041A");

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @ParameterizedTest
    @CsvSource({
    	"MCG,RF-00011484-PAR",
    	"NOT_MCG,RF-00011484-PAR",
    	"MCG,NOT_RF-00011484-PAR"
    })
        // TEST OK: resQualValue != POS & progID = MCG & paramCode = RF-00011484-PAR & evalCode = J041A
    void regolaCampoCondizionatoQuattroValori_notResQualValuePOS_TestKO(String progID, String paramCode) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenReturn("NOT_POS");
        Mockito.when(dtoGenerico.getCampo("progID")).thenReturn(progID);
        Mockito.when(dtoGenerico.getCampo("paramCode")).thenReturn(paramCode);
        Mockito.when(dtoGenerico.getCampo("evalCode")).thenReturn("J041A");

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", dtoGenerico);

        TestUtils.assertValidationError("resQualValue", "BR135", "Codice BR135", listaEsiti);
    }

    @Test
        // TEST KO: dto.getCampo -> InvocationTargetException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_InvocationTargetException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenThrow(InvocationTargetException.class);

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("resQualValue", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> NoSuchMethodException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_NoSuchMethodException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenThrow(NoSuchMethodException.class);

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("resQualValue", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> IllegalAccessException => ValidazioneImpossibileException
    void regolaCampoCondizionatoDueValori_IllegalAccessException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("resQualValue")).thenThrow(IllegalAccessException.class);

        var regola = new RegolaCampoCondizionatoQuattroValori("regolaCampoCondizionatoQuattroValori", "BR135", "Codice BR135", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("resQualValue", dtoGenerico));
    }

    @Test
    void validaCostruttore() {
        RegolaCampoCondizionatoQuattroValori regola = new RegolaCampoCondizionatoQuattroValori();
        assertTrue(regola instanceof RegolaCampoCondizionatoQuattroValori);
    }
}
