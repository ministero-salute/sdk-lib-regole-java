package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaObbligatorietaTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorieta regola = new RegolaObbligatorieta();
        assertTrue(regola instanceof RegolaObbligatorieta);
    }


    @Test
    void testObbligatorietaStringValidaOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorieta regola = new RegolaObbligatorieta("obbligatorieta", "E01", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("123");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testObbligatorietaStringValidaKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorieta regola = new RegolaObbligatorieta("obbligatorieta", "E01", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
            //assertEquals("E01" , e.getCodice());
        }
    }

    @Test
    void testObbligatorietaIntegerValidaOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorieta regola = new RegolaObbligatorieta("obbligatorieta", "E01", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(123);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testObbligatorietaIntegerValidaKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorieta regola = new RegolaObbligatorieta("obbligatorieta", "E01", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            // assertEquals("E01" , e.getCodice());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorieta regola = new RegolaObbligatorieta("obbligatorieta", "E01", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}
