package it.mds.sdk.libreriaregole.regole.catalogo.lunghezza;

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
class RegolaLunghezzaCampoObbligatorioTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void testLunghezzaCOValidaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("size", "3");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaLunghezzaCampoObbligatorio regola = new RegolaLunghezzaCampoObbligatorio("lunghezza", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("OKI");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testLunghezzaCOValidaKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("size", "3");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaLunghezzaCampoObbligatorio regola = new RegolaLunghezzaCampoObbligatorio("lunghezza", "E01", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("nomeCampoConfronto")).thenReturn("TESTKO");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            // assertEquals("E01",e.getCodice());
            assertEquals("E01", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaLunghezzaCampoObbligatorio regola = new RegolaLunghezzaCampoObbligatorio();
        assertTrue(regola instanceof RegolaLunghezzaCampoObbligatorio);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaLunghezzaCampoObbligatorio regola = new RegolaLunghezzaCampoObbligatorio();
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("nomeCampoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("nomeCampoConfronto", recordMockito));
    }
}
