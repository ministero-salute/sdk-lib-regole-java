package it.mds.sdk.libreriaregole.regole.catalogo;

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
class RegolaDisuguagliancaListaCampiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    public void disuguaglianzaListaCampiOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDaConfrontare", "regioneDomicilioSanitario|comuneDomicilioSanitario|aslDomicilioSanitario");
        parametri.put("listaCampiConfronto", "regioneResidenza|comuneResidenza|aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaDisuguaglianzaListaCampi regola =
                new RegolaDisuguaglianzaListaCampi("regolaDisuguagliancaListaCampi", "2065", "Dati di Domicilio incoerenti con i dati di Residenza", parametriTest);

        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn("REGIONE_TEST");
        Mockito.when(recordMockito.getCampo("comuneDomicilioSanitario")).thenReturn("COMUNE_TEST");
        Mockito.when(recordMockito.getCampo("aslDomicilioSanitario")).thenReturn("ASL_TEST");

        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn("REGIONE_TEST");
        Mockito.when(recordMockito.getCampo("comuneResidenza")).thenReturn("COMUNE_TEST");
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn("ASL_TEST");

        List<Esito> result = regola.valida("regolaUguaglianzaListaCampi", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    public void disuguaglianzaListaCampiKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDaConfrontare", "regioneDomicilioSanitario|comuneDomicilioSanitario|aslDomicilioSanitario");
        parametri.put("listaCampiConfronto", "regioneResidenza|comuneResidenza|aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaDisuguaglianzaListaCampi regola =
                new RegolaDisuguaglianzaListaCampi("regolaDisuguagliancaListaCampi", "2065", "Dati di Domicilio incoerenti con i dati di Residenza", parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn("REGIONE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("comuneDomicilioSanitario")).thenReturn("COMUNE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("aslDomicilioSanitario")).thenReturn("ASL_DOMICILIO_SANITARIO_TEST");

        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn("REGIONE_DOMICILIO_SANITARIO_TEST_2");
        Mockito.when(recordMockito.getCampo("comuneResidenza")).thenReturn("COMUNE_DOMICILIO_SANITARIO_TEST_2");
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn("ASL_DOMICILIO_SANITARIO_TEST_2");

        List<Esito> result = regola.valida("regolaUguaglianzaListaCampi", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    public void disuguaglianzaListaCampiNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("listaCampiDaConfrontare", "regioneDomicilioSanitario|comuneDomicilioSanitario|aslDomicilioSanitario");
        parametri.put("listaCampiConfronto", "regioneResidenza|comuneResidenza|aslResidenza");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaDisuguaglianzaListaCampi regola =
                new RegolaDisuguaglianzaListaCampi("regolaDisuguagliancaListaCampi", "2065", "Dati di Domicilio incoerenti con i dati di Residenza", parametriTest);
        Mockito.when(recordMockito.getCampo("regioneDomicilioSanitario")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("comuneDomicilioSanitario")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("aslDomicilioSanitario")).thenReturn(null);

        Mockito.when(recordMockito.getCampo("regioneResidenza")).thenReturn("REGIONE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("comuneResidenza")).thenReturn("COMUNE_DOMICILIO_SANITARIO_TEST");
        Mockito.when(recordMockito.getCampo("aslResidenza")).thenReturn("ASL_DOMICILIO_SANITARIO_TEST");

        List<Esito> result = regola.valida("regolaUguaglianzaListaCampi", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaDisuguaglianzaListaCampi regola = new RegolaDisuguaglianzaListaCampi();
        assertTrue(regola instanceof RegolaDisuguaglianzaListaCampi);
    }
}