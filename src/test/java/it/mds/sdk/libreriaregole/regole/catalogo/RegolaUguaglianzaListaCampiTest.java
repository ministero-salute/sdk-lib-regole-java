/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegolaUguaglianzaListaCampiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    public void uguaglianzaListaCampiOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDaConfrontare", "regioneDomicilioSanitario|comuneDomicilioSanitario|aslDomicilioSanitario");
        parametri.put("listaCampiConfronto", "regioneResidenza|comuneResidenza|aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaUguaglianzaListaCampi regola =
                new RegolaUguaglianzaListaCampi("regolaUguaglianzaListaCampi", "2065", "Dati di Domicilio incoerenti con i dati di Residenza", parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn("REGIONE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("comuneDomicilioSanitario")).thenReturn("COMUNE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("aslDomicilioSanitario")).thenReturn("ASL_DOMICILIO_SANITARIO_TEST");

        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn("REGIONE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("comuneResidenza")).thenReturn("COMUNE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn("ASL_DOMICILIO_SANITARIO_TEST");

        List<Esito> result = regola.valida("regolaUguaglianzaListaCampi", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    public void uguaglianzaListaCampiKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDaConfrontare", "regioneDomicilioSanitario|comuneDomicilioSanitario|aslDomicilioSanitario");
        parametri.put("listaCampiConfronto", "regioneResidenza|comuneResidenza|aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaUguaglianzaListaCampi regola =
                new RegolaUguaglianzaListaCampi("regolaUguaglianzaListaCampi", "2065", "Dati di Domicilio incoerenti con i dati di Residenza", parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn("REGIONE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("comuneDomicilioSanitario")).thenReturn("COMUNE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("aslDomicilioSanitario")).thenReturn("ASL_DOMICILIO_SANITARIO_TEST");

        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn("REGIONE_RESIDENZA_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("comuneResidenza")).thenReturn("COMUNE_RESIDENZA_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn("ASL_RESIDENZA_SANITARIO_TEST");

        List<Esito> result = regola.valida("regolaUguaglianzaListaCampi", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    public void uguaglianzaListaCampiDomicilioNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDaConfrontare", "regioneDomicilioSanitario|comuneDomicilioSanitario|aslDomicilioSanitario");
        parametri.put("listaCampiConfronto", "regioneResidenza|comuneResidenza|aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaUguaglianzaListaCampi regola =
                new RegolaUguaglianzaListaCampi("regolaUguaglianzaListaCampi", "2065", "Dati di Domicilio incoerenti con i dati di Residenza", parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("comuneDomicilioSanitario")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("aslDomicilioSanitario")).thenReturn(null);

        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn("REGIONE_RESIDENZA_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("comuneResidenza")).thenReturn("COMUNE_RESIDENZA_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn("ASL_RESIDENZA_SANITARIO_TEST");

        List<Esito> result = regola.valida("regolaUguaglianzaListaCampi", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaUguaglianzaListaCampi regola = new RegolaUguaglianzaListaCampi();
        assertTrue(regola instanceof RegolaUguaglianzaListaCampi);
    }
}