package it.mds.sdk.libreriaregole.regole.catalogo.lunghezza;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.apache.commons.lang3.RandomStringUtils;
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

@ExtendWith(MockitoExtension.class)
public class RegolaMaxLunghezzaCampoObbligatorioTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaMaxLunghezzaCampoObbligatorio regolaMaxLunghezzaCampoObbligatorio = new RegolaMaxLunghezzaCampoObbligatorio();
        assertTrue(regolaMaxLunghezzaCampoObbligatorio instanceof RegolaMaxLunghezzaCampoObbligatorio);
    }

    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("maxLunghezza", "100");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaMaxLunghezzaCampoObbligatorio regolaMax = new RegolaMaxLunghezzaCampoObbligatorio("maxLunghezzaCampoObbligatorio", "E6060","descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("vaccinoCOVID");
        List<Esito> result = regolaMax.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOMaggioreSoglia() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("maxLunghezza", "100");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaMaxLunghezzaCampoObbligatorio regolaMax = new RegolaMaxLunghezzaCampoObbligatorio("maxLunghezzaCampoObbligatorio", "E6060", "descrizioneErrore",parametriTest);
        String stringaDa101 = RandomStringUtils.randomAlphanumeric(101);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(stringaDa101);
        List<Esito> result = regolaMax.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
           // assertEquals("E6060" ,e.getErroriValidazione().get(0).getCodice());
            assertEquals("E6060" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testLunghezzaException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("maxLunghezza", "testSbagliato");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaMaxLunghezzaCampoObbligatorio regolaMax = new RegolaMaxLunghezzaCampoObbligatorio("maxLunghezzaCampoObbligatorio", "E6060", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("diecicarat");

        assertThrows(ValidazioneImpossibileException.class, () -> regolaMax.valida("nomeCampoConfronto", recordMockito));
    }
}