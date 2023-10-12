package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta.RegolaObbligatorietaNullCondizionataValoreCampo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaListaValoriAmmessiCondizionataListaValoriTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("listaValoriAmmessi", "AOCS1|AOCS2|AOCS3");
        parametri.put("listaValoriAmmessiCampoCondizionante", "RF-00002832-PAR|RF-00002833-PAR|RF-00002834-PAR|RF-00001344-PAR|RF-00000377-ORG|RF-00000380-ORG|RF-00000378-ORG");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataListaValori regola = new RegolaListaValoriAmmessiCondizionataListaValori("regolaListaValoriAmmessiCondizionataListaValori", "BR026", "Codice BR008", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("AOCS2");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("RF-00002833-PAR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testCampoCondizionanteNonInListaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("listaValoriAmmessi", "AOCS1|AOCS2|AOCS3");
        parametri.put("listaValoriAmmessiCampoCondizionante", "RF-00002832-PAR|RF-00002833-PAR|RF-00002834-PAR|RF-00001344-PAR|RF-00000377-ORG|RF-00000380-ORG|RF-00000378-ORG");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataListaValori regola = new RegolaListaValoriAmmessiCondizionataListaValori("regolaListaValoriAmmessiCondizionataListaValori", "BR026", "Codice BR008", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("AOCS2");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("valore random");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("listaValoriAmmessi", "AOCS1|AOCS2|AOCS3");
        parametri.put("listaValoriAmmessiCampoCondizionante", "RF-00002832-PAR|RF-00002833-PAR|RF-00002834-PAR|RF-00001344-PAR|RF-00000377-ORG|RF-00000380-ORG|RF-00000378-ORG");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataListaValori regola = new RegolaListaValoriAmmessiCondizionataListaValori("regolaListaValoriAmmessiCondizionataListaValori", "BR026", "Codice BR008", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("valore non ammesso");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante")).thenReturn("RF-00002834-PAR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR026", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCondizionataListaValori regola = new RegolaListaValoriAmmessiCondizionataListaValori();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataListaValori);
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriAmmessiCondizionataListaValori regola = new RegolaListaValoriAmmessiCondizionataListaValori("TEST", "TEST", "TEST", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("targatura");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("targatura", recordMockito));
    }
}