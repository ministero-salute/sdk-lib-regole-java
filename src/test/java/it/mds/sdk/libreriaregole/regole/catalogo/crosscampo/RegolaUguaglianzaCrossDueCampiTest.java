/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
class RegolaUguaglianzaCrossDueCampiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaUguaglianzaCrossDueCampi regola = new RegolaUguaglianzaCrossDueCampi();
        assertTrue(regola instanceof RegolaUguaglianzaCrossDueCampi);
    }

    @Test
    void valida1OK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("primoCampoValidatore", "regioneResidenza");
        parametri.put("secondoCampoValidatore", "regioneDomicilio");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossDueCampi regola = new RegolaUguaglianzaCrossDueCampi("uguaglianzaCrossDueCamp1", "E6115", "descrizioneErrore",parametriTest);

        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("190");
        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn("190");
        Mockito.when(recordMockito.getCampo("regioneDomicilio")).thenReturn("180");
        List<Esito> result = regola.valida("codiceRegioneSomministrazione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void valida2OK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("primoCampoValidatore", "regioneResidenza");
        parametri.put("secondoCampoValidatore", "regioneDomicilio");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossDueCampi regola = new RegolaUguaglianzaCrossDueCampi("uguaglianzaCrossDueCamp1", "E6115", "descrizioneErrore",parametriTest);

        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("190");
        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("regioneDomicilio")).thenReturn("190");
        List<Esito> result = regola.valida("codiceRegioneSomministrazione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void valida1KO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("primoCampoValidatore", "regioneResidenza");
        parametri.put("secondoCampoValidatore", "regioneDomicilio");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossDueCampi regola = new RegolaUguaglianzaCrossDueCampi("uguaglianzaCrossDueCamp1", "E6115", "descrizioneErrore",parametriTest);

        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("190");
        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn("180");
        Mockito.when(recordMockito.getCampo("regioneDomicilio")).thenReturn("170");
        List<Esito> result = regola.valida("codiceRegioneSomministrazione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E6115" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void valida2KO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("primoCampoValidatore", "regioneResidenza");
        parametri.put("secondoCampoValidatore", "regioneDomicilio");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossDueCampi regola = new RegolaUguaglianzaCrossDueCampi("uguaglianzaCrossDueCamp1", "E6115", "descrizioneErrore",parametriTest);

        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("190");
        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("regioneDomicilio")).thenReturn(null);
        List<Esito> result = regola.valida("codiceRegioneSomministrazione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E6115" ,e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaUguaglianzaCrossDueCampi regola = new RegolaUguaglianzaCrossDueCampi("TEST", "TEST", "TEST", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

}