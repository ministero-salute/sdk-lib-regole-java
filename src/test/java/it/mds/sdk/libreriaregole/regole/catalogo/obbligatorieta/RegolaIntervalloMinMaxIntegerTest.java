package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.catalogo.RegolaVerificaCodiceFiscale;
import it.mds.sdk.libreriaregole.regole.catalogo.lunghezza.RegolaIntervalloLunghezza;
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
class RegolaIntervalloMinMaxIntegerTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaIntervalloMinMaxInteger regola = new RegolaIntervalloMinMaxInteger();
        assertTrue(regola instanceof RegolaIntervalloMinMaxInteger);
    }

    @Test
    void validaOKCampoDaValidareDiversoNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroMin", "1");
        parametri.put("parametroMax", "99999");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIntervalloMinMaxInteger regola = new RegolaIntervalloMinMaxInteger("intervalloMinMaxInteger", "intervalloMinMaxInteger", "intervalloMinMaxInteger", parametriTest);
        Mockito.when(recordMockito.getCampo("progressivoPrestazioniContatto")).thenReturn(10);
        List<Esito> result = regola.valida("progressivoPrestazioniContatto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void validaKOCampoDaValidareDiversoNulleParametri() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroMin", "1");
        parametri.put("parametroMax", "99999");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaIntervalloMinMaxInteger regola = new RegolaIntervalloMinMaxInteger("intervalloMinMaxInteger", "intervalloMinMaxInteger", "intervalloMinMaxInteger", parametriTest);
        Mockito.when(recordMockito.getCampo("progressivoPrestazioniContatto")).thenReturn(0);
        List<Esito> result = regola.valida("progressivoPrestazioniContatto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("intervalloMinMaxInteger", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaIntervalloMinMaxInteger regola = new RegolaIntervalloMinMaxInteger("intervalloMinMaxInteger", "intervalloMinMaxInteger", "intervalloMinMaxInteger", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("progressivoPrestazioniContatto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("progressivoPrestazioniContatto", recordMockito));
    }
}