package it.mds.sdk.libreriaregole.regole.diretta;

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
class RegolaB42Test {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaB42 regola = new RegolaB42();
        assertTrue(regola instanceof RegolaB42);
    }

    @Test
    void campoDaValidareNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaB42 regola = new RegolaB42("RegolaB42", "B42", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn(null);
        List<Esito> result = regola.valida("codiceAziendaSanitariaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void meseNonValidoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("mese", "meseDiRiferimento");
        parametri.put("anno", "annoDiRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaB42 regola = new RegolaB42("RegolaB42", "B42", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("12345");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("13");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        List<Esito> result = regola.valida("codiceAziendaSanitariaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B42", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaB42 regola = new RegolaB42("RegolaB42", "B42", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceAziendaSanitariaErogante");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceAziendaSanitariaErogante", recordMockito));
    }
}