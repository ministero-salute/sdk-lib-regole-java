package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.catalogo.RegolaListaValoriAmmessi;
import it.mds.sdk.libreriaregole.regole.catalogo.input.RegolaCheckCampiInput;
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
class RegolaObbligatorietaCondizionataDaListaValoriTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaCondizionataDaListaValori regola = new RegolaObbligatorietaCondizionataDaListaValori();
        assertTrue(regola instanceof RegolaObbligatorietaCondizionataDaListaValori);
    }

    @Test
    void testOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "tipoFarmaco");
        parametri.put("listaValoriAmmessi", "1|4|5|6");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataDaListaValori regola = new RegolaObbligatorietaCondizionataDaListaValori("RegolaObbligatorietaCondizionataDaListaValori", "B11", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceFarmaco")).thenReturn("abc");
        Mockito.when(recordMockito.getCampo("tipoFarmaco")).thenReturn("1");
        List<Esito> result = regola.valida("codiceFarmaco", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testKOCampoDaValidareNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "tipoFarmaco");
        parametri.put("listaValoriAmmessi", "1|4|5|6");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataDaListaValori regola = new RegolaObbligatorietaCondizionataDaListaValori("RegolaObbligatorietaCondizionataDaListaValori", "B11", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceFarmaco")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipoFarmaco")).thenReturn("1");
        List<Esito> result = regola.valida("codiceFarmaco", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B11", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("campoCondizionante", "tipoFarmaco");
        parametri.put("listaValoriAmmessi", "1|4|5|6");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaObbligatorietaCondizionataDaListaValori regola = new RegolaObbligatorietaCondizionataDaListaValori("RegolaObbligatorietaCondizionataDaListaValori", "B11", "descrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceFarmaco");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("codiceFarmaco", recordMockito));
    }
}