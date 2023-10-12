package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
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
class RegolaBR1990Test {

    @Mock
    RecordDtoGenerico recordMockito;

    @Mock
    CampiInputBean campiInputBean;

    @Test
    void validaCostruttore() {
        RegolaBR1990 regola = new RegolaBR1990();
        assertTrue(regola instanceof RegolaBR1990);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaBR1990 regola = new RegolaBR1990("RegolaBR1990", "1990", "Codice 1990", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @Test
    void modalitaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("modalita", "modalita");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaBR1990 regola = new RegolaBR1990("RegolaBR1990", "1990", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("120");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("REM");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }


    @Test
    void modalitaK0() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("modalita", "modalita");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaBR1990 regola = new RegolaBR1990("RegolaBR1990", "1990", "Codice 1990", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("120");
        Mockito.when(campiInputBean.getCodiceRegioneInput()).thenReturn("020");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("RE");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("1990", e.getErroriValidazione().get(0).getCodice());
        }
    }
}