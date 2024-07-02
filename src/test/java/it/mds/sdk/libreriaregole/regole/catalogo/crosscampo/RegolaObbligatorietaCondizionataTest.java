/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RegolaObbligatorietaCondizionataTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaCondizionata regola = new RegolaObbligatorietaCondizionata();
        assertTrue(regola instanceof RegolaObbligatorietaCondizionata);
    }

    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "regioneDomicilioSanitario");
        parametri.put("validatore1", "comuneDomicilio");
        parametri.put("validatore2", "aslDomicilio");
        parametri.put("validatore3", "aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionata regola = new RegolaObbligatorietaCondizionata("regolaObbligatorietaCondizionata", "E2042", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn("150");
        Mockito.when(recordMockito.getCampo("comuneDomicilio")).thenReturn("160");
        Mockito.when(recordMockito.getCampo("aslDomicilio")).thenReturn("170");
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn("180");
        List<Esito> result = regola.valida("regioneDomicilioSanitario", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void valida2OK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "regioneDomicilioSanitario");
        parametri.put("validatore1", "comuneDomicilio");
        parametri.put("validatore2", "aslDomicilio");
        parametri.put("validatore3", "aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionata regola = new RegolaObbligatorietaCondizionata("regolaObbligatorietaCondizionata", "E2042", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("comuneDomicilio")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("aslDomicilio")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn("140");
        List<Esito> result = regola.valida("regioneDomicilioSanitario", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void valida1KO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //la regola non viene validata perchè la regione di domicilio sanitario è uguale all'asl di residenza ed entrambe sono nulle
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "regioneDomicilioSanitario");
        parametri.put("validatore1", "comuneDomicilio");
        parametri.put("validatore2", "aslDomicilio");
        parametri.put("validatore3", "aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionata regola = new RegolaObbligatorietaCondizionata("regolaObbligatorietaCondizionata", "E2042","descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("comuneDomicilio")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("aslDomicilio")).thenReturn("170");
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn(null);
        List<Esito> result = regola.valida("regioneDomicilioSanitario", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E2042" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void valida2KO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //la regola non viene validata perchè il comune di Domicilio e asl Domicilio sono entrambe non valorizzate
        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoValidatore", "regioneDomicilioSanitario");
        parametri.put("validatore1", "comuneDomicilio");
        parametri.put("validatore2", "aslDomicilio");
        parametri.put("validatore3", "aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionata regola = new RegolaObbligatorietaCondizionata("regolaObbligatorietaCondizionata", "E2042", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("comuneDomicilio")).thenReturn("160");
        Mockito.when(recordMockito.getCampo("aslDomicilio")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn(null);
        List<Esito> result = regola.valida("regioneDomicilioSanitario", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E2042" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        var regola = new RegolaObbligatorietaCondizionata(
                "TEST", "TEST", "TEST",null
        );
        Mockito.when(recordMockito.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));
    }
}