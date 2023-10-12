package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
class RegolaIncoerenzaRegioneComuneASLSomministrazioneTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaRegioneComuneASLSomministrazione regola = new RegolaIncoerenzaRegioneComuneASLSomministrazione();
        assertTrue(regola instanceof RegolaIncoerenzaRegioneComuneASLSomministrazione);
    }

    @Test
    void testOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaIncoerenzaRegioneComuneASLSomministrazione regola = new RegolaIncoerenzaRegioneComuneASLSomministrazione("regolaIncoerenzaRegioneComuneASLSomministrazione", "", "descrizioneErrore", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaIncoerenzaRegioneComuneASLSomministrazione regola = new RegolaIncoerenzaRegioneComuneASLSomministrazione("regolaIncoerenzaRegioneComuneASLSomministrazione", "", "descrizioneErrore", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("998");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceAslSomministrazione")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testKO2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaIncoerenzaRegioneComuneASLSomministrazione regola = new RegolaIncoerenzaRegioneComuneASLSomministrazione("regolaIncoerenzaRegioneComuneASLSomministrazione", "", "descrizioneErrore", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("998");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("999998");
        Mockito.when(recordMockito.getCampo("codiceAslSomministrazione")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaIncoerenzaRegioneComuneASLSomministrazione regola = new RegolaIncoerenzaRegioneComuneASLSomministrazione("regolaIncoerenzaRegioneComuneASLSomministrazione", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}