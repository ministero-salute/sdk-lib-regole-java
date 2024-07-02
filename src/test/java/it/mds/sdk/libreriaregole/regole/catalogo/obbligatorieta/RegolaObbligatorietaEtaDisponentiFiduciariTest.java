/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class RegolaObbligatorietaEtaDisponentiFiduciariTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari();
        assertTrue(regola instanceof RegolaObbligatorietaEtaDisponentiFiduciari);
    }

    @Test
    void testOKDataNascitaDonatoreMinorenne() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari("regolaObbligatorietaEtaDisponentiFiduciari", "5000", "5000", null);
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("fiduciari1DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("fiduciari2DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("NO");
        List<Esito> result = regola.valida("donatoreDataNascita", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKODataNascitaDonatoreMinorenne() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari("regolaObbligatorietaEtaDisponentiFiduciari", "5000", "5000", null);
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("fiduciari1DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("fiduciari2DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("SI");
        List<Esito> result = regola.valida("donatoreDataNascita", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("5000", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testOKMinorenne() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari("regolaObbligatorietaEtaDisponentiFiduciari", "5000", "5000", null);
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn("2004-10-27");
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("fiduciari1DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("fiduciari2DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("NO");
        List<Esito> result = regola.valida("donatoreDataNascita", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKOMinorenneDisponenti1Minorenne() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		LocalDate localDate = LocalDate.of(2020, 10, 25);
		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
			mockedDate.when(LocalDate::now).thenReturn(localDate);
			RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari(
					"regolaObbligatorietaEtaDisponentiFiduciari", "5000", "5000", null);
			Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn("2005-10-27");
			Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2005-10-27");
			Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn("TR");
			Mockito.when(recordMockito.getCampo("fiduciari1DataNascita")).thenReturn("TR");
			Mockito.when(recordMockito.getCampo("fiduciari2DataNascita")).thenReturn("TR");
			Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("NO");
			List<Esito> result = regola.valida("donatoreDataNascita", recordMockito);
			for (Esito e : result) {
				assertFalse(e.isValoreEsito());
				assertEquals("5000", e.getErroriValidazione().get(0).getCodice());
			}
		}
	}

	@Test
	void testKOMinorenneDisponenti2Minorenne()
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		LocalDate localDate = LocalDate.of(2020, 10, 25);
		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
			mockedDate.when(LocalDate::now).thenReturn(localDate);
			RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari(
					"regolaObbligatorietaEtaDisponentiFiduciari", "5000", "5000", null);
			Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn("2005-10-27");
			Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2004-10-27");
			Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn("2004-10-27");
			Mockito.when(recordMockito.getCampo("fiduciari1DataNascita")).thenReturn("TR");
			Mockito.when(recordMockito.getCampo("fiduciari2DataNascita")).thenReturn("TR");
			Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("NO");
			List<Esito> result = regola.valida("donatoreDataNascita", recordMockito);
			for (Esito e : result) {
				assertFalse(e.isValoreEsito());
				assertEquals("5000", e.getErroriValidazione().get(0).getCodice());
			}
		}
	}

    @Test
    void testKOMinorenneFiduciari1() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		LocalDate localDate = LocalDate.of(2020, 10, 25);
		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
			mockedDate.when(LocalDate::now).thenReturn(localDate);
			RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari(
					"regolaObbligatorietaEtaDisponentiFiduciari", "5000", "5000", null);
			Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn("2005-10-27");
			Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2004-10-27");
			Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn("2004-10-27");
			Mockito.when(recordMockito.getCampo("fiduciari1DataNascita")).thenReturn("2004-10-27");
			Mockito.when(recordMockito.getCampo("fiduciari2DataNascita")).thenReturn("TR");
			Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("NO");
			List<Esito> result = regola.valida("donatoreDataNascita", recordMockito);
			for (Esito e : result) {
				assertFalse(e.isValoreEsito());
				assertEquals("5000", e.getErroriValidazione().get(0).getCodice());
			}
		}
	}

    @Test
    void testKODataException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari("regolaObbligatorietaEtaDisponentiFiduciari", "5000", "5000", null);
        Mockito.when(recordMockito.getCampo("donatoreDataNascita")).thenReturn("2005-13-27");
        Mockito.when(recordMockito.getCampo("disponenti1DataNascita")).thenReturn("2004-10-27");
        Mockito.when(recordMockito.getCampo("disponenti2DataNascita")).thenReturn("2004-10-27");
        Mockito.when(recordMockito.getCampo("fiduciari1DataNascita")).thenReturn("2004-10-27");
        Mockito.when(recordMockito.getCampo("fiduciari2DataNascita")).thenReturn("TR");
        Mockito.when(recordMockito.getCampo("donatoreMinorenne")).thenReturn("NO");
        List<Esito> result = regola.valida("donatoreDataNascita", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("5000", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaEtaDisponentiFiduciari regola = new RegolaObbligatorietaEtaDisponentiFiduciari("regolaObbligatorietaEtaDisponentiFiduciari", "5000", "5000", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("donatoreDataNascita");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("donatoreDataNascita", recordMockito));
    }
}