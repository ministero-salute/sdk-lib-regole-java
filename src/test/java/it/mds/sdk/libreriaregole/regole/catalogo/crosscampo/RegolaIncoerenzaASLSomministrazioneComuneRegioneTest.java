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
class RegolaIncoerenzaASLSomministrazioneComuneRegioneTest {
    @Mock
    RecordDtoGenerico recordMockito;
    //se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaASLSomministrazioneComuneRegione regola = new RegolaIncoerenzaASLSomministrazioneComuneRegione();
        assertTrue(regola instanceof RegolaIncoerenzaASLSomministrazioneComuneRegione);
    }

    @Test
    void RegolaIncoerenzaASLSomministrazioneComuneRegioneOK1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RegolaIncoerenzaASLSomministrazioneComuneRegione regola = new RegolaIncoerenzaASLSomministrazioneComuneRegione("regolaIncoerenzaASLSomministrazioneComuneRegione", "", "desrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("999");
        Mockito.when(recordMockito.getCampo("codiceComuneResidenza")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceAslResidenza")).thenReturn("999");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void RegolaIncoerenzaASLSomministrazioneComuneRegioneOK2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RegolaIncoerenzaASLSomministrazioneComuneRegione regola = new RegolaIncoerenzaASLSomministrazioneComuneRegione("regolaIncoerenzaASLSomministrazioneComuneRegione", "", "desrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("899");
        Mockito.when(recordMockito.getCampo("codiceComuneResidenza")).thenReturn("899999");
        Mockito.when(recordMockito.getCampo("codiceAslResidenza")).thenReturn("899");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void RegolaIncoerenzaASLSomministrazioneComuneRegioneKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        RegolaIncoerenzaASLSomministrazioneComuneRegione regola = new RegolaIncoerenzaASLSomministrazioneComuneRegione("regolaIncoerenzaASLSomministrazioneComuneRegione", "", "desrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("899");
        Mockito.when(recordMockito.getCampo("codiceComuneResidenza")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceAslResidenza")).thenReturn("899");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaIncoerenzaASLSomministrazioneComuneRegione regola = new RegolaIncoerenzaASLSomministrazioneComuneRegione("regolaIncoerenzaASLSomministrazioneComuneRegione", "", "desrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}