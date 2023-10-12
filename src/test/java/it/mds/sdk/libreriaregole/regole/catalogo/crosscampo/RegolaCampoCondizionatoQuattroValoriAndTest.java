package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
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
class RegolaCampoCondizionatoQuattroValoriAndTest {

    @Mock
    RecordDtoGenerico recordDtoGenerico;
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
    void regolaCampoCondizionatoQuattroValori_TestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(recordDtoGenerico.getCampo("resQualValue")).thenReturn("POS");
        Mockito.when(recordDtoGenerico.getCampo("progID")).thenReturn("MCG");
        Mockito.when(recordDtoGenerico.getCampo("paramCode")).thenReturn("RF-00011484-PAR");
        Mockito.when(recordDtoGenerico.getCampo("evalCode")).thenReturn("J041A");

        var regola = new RegolaCampoCondizionatoQuattroValoriAnd("RegolaCampoCondizionatoQuattroValoriAnd", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", recordDtoGenerico);

        for (Esito e : listaEsiti) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR135", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaCampoCondizionatoQuattroValoriAnd regola = new RegolaCampoCondizionatoQuattroValoriAnd();
        assertTrue(regola instanceof RegolaCampoCondizionatoQuattroValoriAnd);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCampoCondizionatoQuattroValoriAnd regola = new RegolaCampoCondizionatoQuattroValoriAnd("RegolaCampoCondizionatoQuattroValoriAnd", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordDtoGenerico).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordDtoGenerico));
    }

    @Test
        // TEST OK: resQualValue = POS & progID = MCG & paramCode = RF-00011484-PAR & evalCode = J041A
    void regolaCampoCondizionatoQuattroValori_TestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(recordDtoGenerico.getCampo("resQualValue")).thenReturn("PES");
        Mockito.when(recordDtoGenerico.getCampo("progID")).thenReturn("MCG");
        Mockito.when(recordDtoGenerico.getCampo("paramCode")).thenReturn("RF-00011484-PAR");
        Mockito.when(recordDtoGenerico.getCampo("evalCode")).thenReturn("J041A");

        var regola = new RegolaCampoCondizionatoQuattroValoriAnd("RegolaCampoCondizionatoQuattroValoriAnd", "BR135", "Codice BR135", parametri);
        var listaEsiti = regola.valida("resQualValue", recordDtoGenerico);

        for (Esito e : listaEsiti) {
            assertTrue(e.isValoreEsito());
        }
    }
}