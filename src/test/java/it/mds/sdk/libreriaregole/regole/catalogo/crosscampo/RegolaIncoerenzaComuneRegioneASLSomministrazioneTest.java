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
class RegolaIncoerenzaComuneRegioneASLSomministrazioneTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaComuneRegioneASLSomministrazione regola = new RegolaIncoerenzaComuneRegioneASLSomministrazione();
        assertTrue(regola instanceof RegolaIncoerenzaComuneRegioneASLSomministrazione);
    }

    @Test
    void regolaIncoerenzaComuneRegioneASLResidenzaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RegolaIncoerenzaComuneRegioneASLSomministrazione regola = new RegolaIncoerenzaComuneRegioneASLSomministrazione("regolaIncoerenzaComuneRegioneASLSomministrazione", "", "desrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("999");
        Mockito.when(recordMockito.getCampo("codiceComuneResidenza")).thenReturn("999998");
        Mockito.when(recordMockito.getCampo("codiceAslResidenza")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void regolaIncoerenzaComuneRegioneASLResidenzaKO2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RegolaIncoerenzaComuneRegioneASLSomministrazione regola = new RegolaIncoerenzaComuneRegioneASLSomministrazione("regolaIncoerenzaComuneRegioneASLSomministrazione", "", "desrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("998");
        Mockito.when(recordMockito.getCampo("codiceComuneResidenza")).thenReturn("999998");
        Mockito.when(recordMockito.getCampo("codiceAslResidenza")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void regolaIncoerenzaComuneRegioneASLResidenzaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RegolaIncoerenzaComuneRegioneASLSomministrazione regola = new RegolaIncoerenzaComuneRegioneASLSomministrazione("regolaIncoerenzaComuneRegioneASLSomministrazione", "", "desrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("codiceComuneResidenza")).thenReturn("999998");
        Mockito.when(recordMockito.getCampo("codiceAslResidenza")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaIncoerenzaComuneRegioneASLSomministrazione regola = new RegolaIncoerenzaComuneRegioneASLSomministrazione("regolaIncoerenzaComuneRegioneASLSomministrazione", "", "desrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}