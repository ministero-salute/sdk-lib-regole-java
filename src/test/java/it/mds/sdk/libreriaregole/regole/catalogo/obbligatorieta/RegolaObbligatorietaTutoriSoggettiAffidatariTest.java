/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class RegolaObbligatorietaTutoriSoggettiAffidatariTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari();
        assertTrue(regola instanceof RegolaObbligatorietaTutoriSoggettiAffidatari);
    }

    @Test
    void testKOTipoDisponenteNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", null);
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn(null);
        List<Esito> result = regola.valida("tipoDisponente", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaObbligatorietaTutoriSoggettiAffidatari", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDonatore", "donatoreLuogoNascitaCodiceNazione|donatoreLuogoNascitaCodiceRegione|donatoreLuogoNascitaCodiceProvincia|donatoreLuogoNascitaCodiceComune|donatoreLuogoResidenzaCodiceNazione|donatoreLuogoResidenzaCodiceRegione|donatoreLuogoResidenzaCodiceProvincia|donatoreLuogoResidenzaCodiceComune|donatoreNome|donatoreCognome|donatoreCF|donatoreDataNascita|donatoreIndirizzoResidenza|donatoreCapResidenza");
        parametri.put("listaCampiDisponenti2", "disponenti2ConsensoEmail|disponenti2LuogoNascitaCodiceNazione|disponenti2LuogoNascitaCodiceRegione|disponenti2LuogoNascitaCodiceProvincia|disponenti2LuogoNascitaCodiceComune|disponenti2LuogoResidenzaCodiceNazione|disponenti2LuogoResidenzaCodiceRegione|disponenti2LuogoResidenzaCodiceProvincia|disponenti2LuogoResidenzaCodiceComune|disponenti2Nome|disponenti2Cognome|disponenti2CF|disponenti2DataNascita|disponenti2IndirizzoResidenza|disponenti2CapResidenza|disponenti2Email");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("TUTORE_SOGGETTI_AFFIDATAR");
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("GENITOR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("si");
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn(null);
        List<Esito> result = regola.valida("tipoDisponente", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaObbligatorietaTutoriSoggettiAffidatari", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testKODisponenti1MinorenneODonatorePieno() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDonatore", "donatoreLuogoNascitaCodiceNazione|donatoreLuogoNascitaCodiceRegione|donatoreLuogoNascitaCodiceProvincia|donatoreLuogoNascitaCodiceComune|donatoreLuogoResidenzaCodiceNazione|donatoreLuogoResidenzaCodiceRegione|donatoreLuogoResidenzaCodiceProvincia|donatoreLuogoResidenzaCodiceComune|donatoreNome|donatoreCognome|donatoreCF|donatoreDataNascita|donatoreIndirizzoResidenza|donatoreCapResidenza");
        parametri.put("listaCampiDisponenti2", "disponenti2ConsensoEmail|disponenti2LuogoNascitaCodiceNazione|disponenti2LuogoNascitaCodiceRegione|disponenti2LuogoNascitaCodiceProvincia|disponenti2LuogoNascitaCodiceComune|disponenti2LuogoResidenzaCodiceNazione|disponenti2LuogoResidenzaCodiceRegione|disponenti2LuogoResidenzaCodiceProvincia|disponenti2LuogoResidenzaCodiceComune|disponenti2Nome|disponenti2Cognome|disponenti2CF|disponenti2DataNascita|disponenti2IndirizzoResidenza|disponenti2CapResidenza|disponenti2Email");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("TUTORE_SOGGETTI_AFFIDATAR");
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("GENITOR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("no");
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2004-10-27");
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoNascitaCodiceNazione")).thenReturn("a");
        List<Esito> result = regola.valida("tipoDisponente", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaObbligatorietaTutoriSoggettiAffidatari", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testKOMaggiorenne() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDonatore", "donatoreLuogoNascitaCodiceNazione|donatoreLuogoNascitaCodiceRegione|donatoreLuogoNascitaCodiceProvincia|donatoreLuogoNascitaCodiceComune|donatoreLuogoResidenzaCodiceNazione|donatoreLuogoResidenzaCodiceRegione|donatoreLuogoResidenzaCodiceProvincia|donatoreLuogoResidenzaCodiceComune|donatoreNome|donatoreCognome|donatoreCF|donatoreDataNascita|donatoreIndirizzoResidenza|donatoreCapResidenza");
        parametri.put("listaCampiDisponenti2", "disponenti2ConsensoEmail|disponenti2LuogoNascitaCodiceNazione|disponenti2LuogoNascitaCodiceRegione|disponenti2LuogoNascitaCodiceProvincia|disponenti2LuogoNascitaCodiceComune|disponenti2LuogoResidenzaCodiceNazione|disponenti2LuogoResidenzaCodiceRegione|disponenti2LuogoResidenzaCodiceProvincia|disponenti2LuogoResidenzaCodiceComune|disponenti2Nome|disponenti2Cognome|disponenti2CF|disponenti2DataNascita|disponenti2IndirizzoResidenza|disponenti2CapResidenza|disponenti2Email");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("TUTORE_SOGGETTI_AFFIDATARI");
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("GENITORE");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("no");
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2004-10-27");
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn(null);
        List<Esito> result = regola.valida("tipoDisponente", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaObbligatorietaTutoriSoggettiAffidatari", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testKODisponenti2MinorenneODonatorePieno() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDonatore", "donatoreLuogoNascitaCodiceNazione|donatoreLuogoNascitaCodiceRegione|donatoreLuogoNascitaCodiceProvincia|donatoreLuogoNascitaCodiceComune|donatoreLuogoResidenzaCodiceNazione|donatoreLuogoResidenzaCodiceRegione|donatoreLuogoResidenzaCodiceProvincia|donatoreLuogoResidenzaCodiceComune|donatoreNome|donatoreCognome|donatoreCF|donatoreDataNascita|donatoreIndirizzoResidenza|donatoreCapResidenza");
        parametri.put("listaCampiDisponenti2", "disponenti2ConsensoEmail|disponenti2LuogoNascitaCodiceNazione|disponenti2LuogoNascitaCodiceRegione|disponenti2LuogoNascitaCodiceProvincia|disponenti2LuogoNascitaCodiceComune|disponenti2LuogoResidenzaCodiceNazione|disponenti2LuogoResidenzaCodiceRegione|disponenti2LuogoResidenzaCodiceProvincia|disponenti2LuogoResidenzaCodiceComune|disponenti2Nome|disponenti2Cognome|disponenti2CF|disponenti2DataNascita|disponenti2IndirizzoResidenza|disponenti2CapResidenza|disponenti2Email");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("TUTORE_SOGGETTI_AFFIDATAR");
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("GENITOR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("no");
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2004-10-27");
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoNascitaCodiceNazione")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoNascitaCodiceRegione")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoNascitaCodiceProvincia")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoNascitaCodiceComune")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoResidenzaCodiceNazione")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoResidenzaCodiceRegione")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoResidenzaCodiceProvincia")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreLuogoResidenzaCodiceComune")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreNome")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreCognome")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreCF")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreIndirizzoResidenza")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("donatoreCapResidenza")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti2ConsensoEmail")).thenReturn("SI");
        List<Esito> result = regola.valida("tipoDisponente", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaObbligatorietaTutoriSoggettiAffidatari", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @ParameterizedTest
    @CsvSource({
    	"TUTORE_SOGGETTI_AFFIDATARI,,",
    	"TUTORE_SOGGETTI_AFFIDATARI,2004-10-28,2005-10-28,",
    	"TUTORE_SOGGETTI_AFFIDATARI,2005-10-28,2005-10-28",
    	"GENITORE,2004-10-28,",  
    	"GENITORE,2005-10-28,2005-10-28",
    })
    void testKOSeTutore(String tipoDisponente, String donatoreDataNascita,String disponenti1DataNascita) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDonatore", "donatoreLuogoNascitaCodiceNazione|donatoreLuogoNascitaCodiceRegione|donatoreLuogoNascitaCodiceProvincia|donatoreLuogoNascitaCodiceComune|donatoreLuogoResidenzaCodiceNazione|donatoreLuogoResidenzaCodiceRegione|donatoreLuogoResidenzaCodiceProvincia|donatoreLuogoResidenzaCodiceComune|donatoreNome|donatoreCognome|donatoreCF|donatoreDataNascita|donatoreIndirizzoResidenza|donatoreCapResidenza");
        parametri.put("listaCampiDisponenti2", "disponenti2ConsensoEmail|disponenti2LuogoNascitaCodiceNazione|disponenti2LuogoNascitaCodiceRegione|disponenti2LuogoNascitaCodiceProvincia|disponenti2LuogoNascitaCodiceComune|disponenti2LuogoResidenzaCodiceNazione|disponenti2LuogoResidenzaCodiceRegione|disponenti2LuogoResidenzaCodiceProvincia|disponenti2LuogoResidenzaCodiceComune|disponenti2Nome|disponenti2Cognome|disponenti2CF|disponenti2DataNascita|disponenti2IndirizzoResidenza|disponenti2CapResidenza|disponenti2Email");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn(tipoDisponente);
        //Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("GENITOR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("si");
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(donatoreDataNascita);
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn(disponenti1DataNascita);
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn(null);
        List<Esito> result = regola.valida("tipoDisponente", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaObbligatorietaTutoriSoggettiAffidatari", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testKOSeTutoreEListaNonNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDonatore", "donatoreLuogoNascitaCodiceNazione|donatoreLuogoNascitaCodiceRegione|donatoreLuogoNascitaCodiceProvincia|donatoreLuogoNascitaCodiceComune|donatoreLuogoResidenzaCodiceNazione|donatoreLuogoResidenzaCodiceRegione|donatoreLuogoResidenzaCodiceProvincia|donatoreLuogoResidenzaCodiceComune|donatoreNome|donatoreCognome|donatoreCF|donatoreDataNascita|donatoreIndirizzoResidenza|donatoreCapResidenza");
        parametri.put("listaCampiDisponenti2", "disponenti2ConsensoEmail|disponenti2LuogoNascitaCodiceNazione|disponenti2LuogoNascitaCodiceRegione|disponenti2LuogoNascitaCodiceProvincia|disponenti2LuogoNascitaCodiceComune|disponenti2LuogoResidenzaCodiceNazione|disponenti2LuogoResidenzaCodiceRegione|disponenti2LuogoResidenzaCodiceProvincia|disponenti2LuogoResidenzaCodiceComune|disponenti2Nome|disponenti2Cognome|disponenti2CF|disponenti2DataNascita|disponenti2IndirizzoResidenza|disponenti2CapResidenza|disponenti2Email");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("TUTORE_SOGGETTI_AFFIDATARI");
        //Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("GENITOR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("si");
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti2ConsensoEmail")).thenReturn("SI");
        List<Esito> result = regola.valida("tipoDisponente", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaObbligatorietaTutoriSoggettiAffidatari", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testOKSeTutoreRigaDispo1DataNascitaMin18() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		LocalDate localDate = LocalDate.of(2021, 11, 25);
		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
			mockedDate.when(LocalDate::now).thenReturn(localDate);
			Map<String, String> parametri = new HashMap<>();
			parametri.put("listaCampiDonatore",
					"donatoreLuogoNascitaCodiceNazione|donatoreLuogoNascitaCodiceRegione|donatoreLuogoNascitaCodiceProvincia|donatoreLuogoNascitaCodiceComune|donatoreLuogoResidenzaCodiceNazione|donatoreLuogoResidenzaCodiceRegione|donatoreLuogoResidenzaCodiceProvincia|donatoreLuogoResidenzaCodiceComune|donatoreNome|donatoreCognome|donatoreCF|donatoreDataNascita|donatoreIndirizzoResidenza|donatoreCapResidenza");
			parametri.put("listaCampiDisponenti2",
					"disponenti2ConsensoEmail|disponenti2LuogoNascitaCodiceNazione|disponenti2LuogoNascitaCodiceRegione|disponenti2LuogoNascitaCodiceProvincia|disponenti2LuogoNascitaCodiceComune|disponenti2LuogoResidenzaCodiceNazione|disponenti2LuogoResidenzaCodiceRegione|disponenti2LuogoResidenzaCodiceProvincia|disponenti2LuogoResidenzaCodiceComune|disponenti2Nome|disponenti2Cognome|disponenti2CF|disponenti2DataNascita|disponenti2IndirizzoResidenza|disponenti2CapResidenza|disponenti2Email");
			Parametri parametriTest = new Parametri();
			parametriTest.setParametriMap(parametri);

			RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari(
					"RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari",
					"RegolaObbligatorietaTutoriSoggettiAffidatari", parametriTest);
			Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("TUTORE_SOGGETTI_AFFIDATARI");
			// Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("GENITOR");
			Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("si");
			Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn("2005-10-28");
			Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2003-10-28");
			Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn(null);
			List<Esito> result = regola.valida("tipoDisponente", recordMockito);
			for (Esito e : result) {
				assertTrue(e.isValoreEsito());
			}
		}
    }

    @Test
    void testKOSeGenitoreListaCampiDisp2NonNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	LocalDate localDate = LocalDate.of(2023, 10, 25);
		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {
			mockedDate.when(LocalDate::now).thenReturn(localDate);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDonatore", "donatoreLuogoNascitaCodiceNazione|donatoreLuogoNascitaCodiceRegione|donatoreLuogoNascitaCodiceProvincia|donatoreLuogoNascitaCodiceComune|donatoreLuogoResidenzaCodiceNazione|donatoreLuogoResidenzaCodiceRegione|donatoreLuogoResidenzaCodiceProvincia|donatoreLuogoResidenzaCodiceComune|donatoreNome|donatoreCognome|donatoreCF|donatoreDataNascita|donatoreIndirizzoResidenza|donatoreCapResidenza");
        parametri.put("listaCampiDisponenti2", "disponenti2ConsensoEmail|disponenti2LuogoNascitaCodiceNazione|disponenti2LuogoNascitaCodiceRegione|disponenti2LuogoNascitaCodiceProvincia|disponenti2LuogoNascitaCodiceComune|disponenti2LuogoResidenzaCodiceNazione|disponenti2LuogoResidenzaCodiceRegione|disponenti2LuogoResidenzaCodiceProvincia|disponenti2LuogoResidenzaCodiceComune|disponenti2Nome|disponenti2Cognome|disponenti2CF|disponenti2DataNascita|disponenti2IndirizzoResidenza|disponenti2CapResidenza|disponenti2Email");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", parametriTest);
        Mockito.when(recordMockito.getCampo("tipoDisponente")).thenReturn("GENITORE");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("si");
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn("2005-10-28");
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2003-10-28");
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn("2005-10-28");
        List<Esito> result = regola.valida("tipoDisponente", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("RegolaObbligatorietaTutoriSoggettiAffidatari", e.getErroriValidazione().get(0).getCodice());
        }
		}
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaTutoriSoggettiAffidatari regola = new RegolaObbligatorietaTutoriSoggettiAffidatari("RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", "RegolaObbligatorietaTutoriSoggettiAffidatari", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("tipoDisponente");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("tipoDisponente", recordMockito));
    }

}