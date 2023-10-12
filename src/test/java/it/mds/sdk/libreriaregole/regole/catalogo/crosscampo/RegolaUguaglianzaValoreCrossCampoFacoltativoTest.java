package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaDataCorrentePosterioreAnnoMeseRif;
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
class RegolaUguaglianzaValoreCrossCampoFacoltativoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaUguaglianzaValoreCrossCampoFacoltativo regola = new RegolaUguaglianzaValoreCrossCampoFacoltativo();
        assertTrue(regola instanceof RegolaUguaglianzaValoreCrossCampoFacoltativo);
    }

    @Test
    void testOKCodiceFarmacoNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaUguaglianzaValoreCrossCampoFacoltativo regola = new RegolaUguaglianzaValoreCrossCampoFacoltativo("RegolaUguaglianzaValoreCrossCampoFacoltativo", "D33", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("codiceFarmaco")).thenReturn(null);
        List<Esito> result = regola.valida("codiceFarmaco", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testOKCampoDipDiversoDaValoreDipendente() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "V03AN01");
        parametri.put("valoreDipendente", "4");
        parametri.put("campoDipendente", "tipoFarmaco");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaValoreCrossCampoFacoltativo regola = new RegolaUguaglianzaValoreCrossCampoFacoltativo("RegolaUguaglianzaValoreCrossCampoFacoltativo", "D33", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceFarmaco")).thenReturn("abc");
        Mockito.when(recordMockito.getCampo("tipoFarmaco")).thenReturn("5");
        List<Esito> result = regola.valida("codiceFarmaco", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKOCampoDaValidareDiversoDaValoreCampo() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampo", "V03AN01");
        parametri.put("valoreDipendente", "4");
        parametri.put("campoDipendente", "tipoFarmaco");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaValoreCrossCampoFacoltativo regola = new RegolaUguaglianzaValoreCrossCampoFacoltativo("RegolaUguaglianzaValoreCrossCampoFacoltativo", "D33", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceFarmaco")).thenReturn("abc");
        Mockito.when(recordMockito.getCampo("tipoFarmaco")).thenReturn("4");
        List<Esito> result = regola.valida("codiceFarmaco", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D33", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaUguaglianzaValoreCrossCampoFacoltativo regola = new RegolaUguaglianzaValoreCrossCampoFacoltativo("RegolaUguaglianzaValoreCrossCampoFacoltativo", "D33", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceFarmaco");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("codiceFarmaco", recordMockito));
    }
}