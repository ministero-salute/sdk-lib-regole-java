package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.testutil.TestUtils;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RegolaCampoNotNullCondizionatoUnValoreUnCampoTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "sampPoint");
        parametri.put("parametroCondizionante", "MS.060.400");
        parametri.put("campoCondizionante2", "OSAid");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
        // TEST OK: codAzAllOrig != null & sampPoint = MS.060.400 & codAzAllOrig == OSAid
    void regolaCampoNotNullCondizionatoUnValoreUnCampo_condizionato_equals_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("codAzAllOrig")).thenReturn("A_VALUE");
        Mockito.when(dtoGenerico.getCampo("sampPoint")).thenReturn("MS.060.400");
        Mockito.when(dtoGenerico.getCampo("OSAid")).thenReturn("A_VALUE");

        var regola = new RegolaCampoNotNullCondizionatoUnValoreUnCampo("regolaCampoNotNullCondizionatoUnValoreUnCampo", "BR154", "Codice BR154", parametri);
        var listaEsiti = regola.valida("codAzAllOrig", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: codAzAllOrig != null & sampPoint != MS.060.400
    void regolaCampoNotNullCondizionatoUnValoreUnCampo_notCondizionato_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("codAzAllOrig")).thenReturn("A_VALUE");
        Mockito.when(dtoGenerico.getCampo("sampPoint")).thenReturn("NOT_MS.060.400");

        var regola = new RegolaCampoNotNullCondizionatoUnValoreUnCampo("regolaCampoNotNullCondizionatoUnValoreUnCampo", "BR154", "Codice BR154", parametri);
        var listaEsiti = regola.valida("codAzAllOrig", dtoGenerico);

        Mockito.verify(dtoGenerico, times(0)).getCampo("OSAid");
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST OK: codAzAllOrig = null
    void regolaCampoNotNullCondizionatoUnValoreUnCampo_null_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("codAzAllOrig")).thenReturn(null);

        var regola = new RegolaCampoNotNullCondizionatoUnValoreUnCampo("regolaCampoNotNullCondizionatoUnValoreUnCampo", "BR154", "Codice BR154", parametri);
        var listaEsiti = regola.valida("codAzAllOrig", dtoGenerico);

        Mockito.verify(dtoGenerico, times(0)).getCampo("sampPoint");
        Mockito.verify(dtoGenerico, times(0)).getCampo("OSAid");
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST KO: codAzAllOrig != null & sampPoint = MS.060.400 & codAzAllOrig != OSAid
    void regolaCampoNotNullCondizionatoUnValoreUnCampo_condizionato_notEquals_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("codAzAllOrig")).thenReturn("A_VALUE");
        Mockito.when(dtoGenerico.getCampo("sampPoint")).thenReturn("MS.060.400");
        Mockito.when(dtoGenerico.getCampo("OSAid")).thenReturn("OTHER_VALUE");

        var regola = new RegolaCampoNotNullCondizionatoUnValoreUnCampo("regolaCampoNotNullCondizionatoUnValoreUnCampo", "BR154", "Codice BR154", parametri);
        var listaEsiti = regola.valida("codAzAllOrig", dtoGenerico);

        TestUtils.assertValidationError("codAzAllOrig", "BR154", "Codice BR154", listaEsiti);
    }

    @Test
        // TEST KO: dto.getCampo -> InvocationTargetException => ValidazioneImpossibileException
    void regolaCampoNotNullCondizionatoUnValoreUnCampo_InvocationTargetException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("codAzAllOrig")).thenThrow(InvocationTargetException.class);

        var regola = new RegolaCampoNotNullCondizionatoUnValoreUnCampo("regolaCampoNotNullCondizionatoUnValoreUnCampo", "BR154", "Codice BR154", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codAzAllOrig", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> NoSuchMethodException => ValidazioneImpossibileException
    void regolaCampoNotNullCondizionatoUnValoreUnCampo_NoSuchMethodException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("codAzAllOrig")).thenThrow(NoSuchMethodException.class);

        var regola = new RegolaCampoNotNullCondizionatoUnValoreUnCampo("regolaCampoNotNullCondizionatoUnValoreUnCampo", "BR154", "Codice BR154", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codAzAllOrig", dtoGenerico));
    }

    @Test
        // TEST KO: dto.getCampo -> IllegalAccessException => ValidazioneImpossibileException
    void regolaCampoNotNullCondizionatoUnValoreUnCampo_IllegalAccessException_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("codAzAllOrig")).thenThrow(IllegalAccessException.class);

        var regola = new RegolaCampoNotNullCondizionatoUnValoreUnCampo("regolaCampoNotNullCondizionatoUnValoreUnCampo", "BR154", "Codice BR154", parametri);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codAzAllOrig", dtoGenerico));
    }

    @Test
    void validaCostruttore() {
        RegolaCampoNotNullCondizionatoUnValoreUnCampo regola = new RegolaCampoNotNullCondizionatoUnValoreUnCampo();
        assertTrue(regola instanceof RegolaCampoNotNullCondizionatoUnValoreUnCampo);
    }
}
