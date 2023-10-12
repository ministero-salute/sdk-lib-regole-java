package it.mds.sdk.libreriaregole.regole.catalogo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaDirettaD39;
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
public class RegolaDiversitaValoreParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDiversitaValoreParametro regola = new RegolaDiversitaValoreParametro();
        assertTrue(regola instanceof RegolaDiversitaValoreParametro);
    }

    @Test
    void valida() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroInput", "200");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreParametro regola = new RegolaDiversitaValoreParametro("diversitaValoreParametro", "E1905","descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("codRegione")).thenReturn("190");
        List<Esito> result = regola.valida("codRegione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroInput", "190");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDiversitaValoreParametro regola = new RegolaDiversitaValoreParametro("diversitaValoreParametro", "E1905","descrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("codRegione")).thenReturn("190");
        List<Esito> result = regola.valida("codRegione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("E1905" ,e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDiversitaValoreParametro regola = new RegolaDiversitaValoreParametro("diversitaValoreParametro", "E1905","descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codRegione");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codRegione", recordMockito));
    }
}