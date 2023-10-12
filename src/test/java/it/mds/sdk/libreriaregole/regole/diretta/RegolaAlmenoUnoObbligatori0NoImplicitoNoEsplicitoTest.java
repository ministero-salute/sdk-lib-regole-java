package it.mds.sdk.libreriaregole.regole.diretta;

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
class RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicitoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito regola = new RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito();
        assertTrue(regola instanceof RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito);
    }

    @Test
    void noImplicitoNoEsplicitoCampoObbligatorioAlmenoUnCampoObbOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito regola = new RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito("RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito", "B28", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("aslAssistito")).thenReturn("id00");
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("id00");
        Mockito.when(recordMockito.getCampo("statoEsteroDiResidenza")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("aslAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void noImplicitoNoEsplicitoCampoObbligatorioNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito regola = new RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito("RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito", "B28", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("aslAssistito")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("id00");
        Mockito.when(recordMockito.getCampo("statoEsteroDiResidenza")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("aslAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B28", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito regola = new RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito("RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("aslAssistito");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("aslAssistito", recordMockito));
    }

}