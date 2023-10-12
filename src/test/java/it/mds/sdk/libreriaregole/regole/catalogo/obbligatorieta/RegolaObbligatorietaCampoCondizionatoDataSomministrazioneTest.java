package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaObbligatorietaCampoCondizionatoDataSomministrazioneTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaCampoCondizionatoDataSomministrazione regola = new RegolaObbligatorietaCampoCondizionatoDataSomministrazione();
        assertTrue(regola instanceof RegolaObbligatorietaCampoCondizionatoDataSomministrazione);
    }

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("dataPassata", "2019-07-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCampoCondizionatoDataSomministrazione regola = new RegolaObbligatorietaCampoCondizionatoDataSomministrazione("RegolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", parametriTest);
        Mockito.when(recordMockito.getCampo("denomVaccino")).thenReturn("pieno1");
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2020-07-01");
        List<Esito> result = regola.valida("denomVaccino", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKODataSommAfterDataPassata() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("dataPassata", "2019-07-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCampoCondizionatoDataSomministrazione regola = new RegolaObbligatorietaCampoCondizionatoDataSomministrazione("RegolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", parametriTest);
        Mockito.when(recordMockito.getCampo("denomVaccino")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2020-07-01");
        List<Esito> result = regola.valida("denomVaccino", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaObbligatorietaCampoCondizionatoDataSomministrazione", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testOKDataSommNonAfterDataPassataAndCampoValidareNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("dataPassata", "2019-07-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCampoCondizionatoDataSomministrazione regola = new RegolaObbligatorietaCampoCondizionatoDataSomministrazione("RegolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", parametriTest);
        Mockito.when(recordMockito.getCampo("denomVaccino")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2019-06-30");
        List<Esito> result = regola.valida("denomVaccino", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("dataPassata", "2019-07-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCampoCondizionatoDataSomministrazione regola = new RegolaObbligatorietaCampoCondizionatoDataSomministrazione("regolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("denomVaccino");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("denomVaccino", recordMockito));
    }

    @Test
    void testIntervalloDataSomministrazioneNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<String, String>();

        parametri.put("dataPassata", "2019-07-01");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCampoCondizionatoDataSomministrazione regola = new RegolaObbligatorietaCampoCondizionatoDataSomministrazione("RegolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", "regolaObbligatorietaCampoCondizionatoDataSomministrazione", parametriTest);
        Mockito.when(recordMockito.getCampo("denomVaccino")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn(null);
        List<Esito> result = regola.valida("denomVaccino", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaObbligatorietaCampoCondizionatoDataSomministrazione", e.getErroriValidazione().get(0).getCodice());
        }
    }
}