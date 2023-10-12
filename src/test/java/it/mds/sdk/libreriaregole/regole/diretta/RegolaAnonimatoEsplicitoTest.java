package it.mds.sdk.libreriaregole.regole.diretta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta.RegolaObbligatorietaCondizionataDaListaValori;
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
class RegolaAnonimatoEsplicitoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaAnonimatoEsplicito regola = new RegolaAnonimatoEsplicito();
        assertTrue(regola instanceof RegolaAnonimatoEsplicito);
    }

    @Test
    void testIdAssOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaAnonimatoEsplicito regola = new RegolaAnonimatoEsplicito("testIdAssOK", "", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+Dms+s2mz2P3x90Y=");
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testIdAssKONotEquals() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaAnonimatoEsplicito regola = new RegolaAnonimatoEsplicito("testIdAssOK", "", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+Dms+s2mz2P3x90Y");
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testIdAssKOIsEmpty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaAnonimatoEsplicito regola = new RegolaAnonimatoEsplicito("testIdAssOK", "", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("");
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaAnonimatoEsplicito regola = new RegolaAnonimatoEsplicito("RegolaAnonimatoEsplicito", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("identificativoAssistito");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("identificativoAssistito", recordMockito));
    }
}