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
class RegolaObbligatorietaNoImplicitoNoEsplicitoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaObbligatorietaNoImplicitoNoEsplicito regola = new RegolaObbligatorietaNoImplicitoNoEsplicito();
        assertTrue(regola instanceof RegolaObbligatorietaNoImplicitoNoEsplicito);
    }

    @Test
    void noImplicitoNoEsplicitoCampoObbligatorioOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaNoImplicitoNoEsplicito regola = new RegolaObbligatorietaNoImplicitoNoEsplicito("RegolaObbligatorietaNoImplicitoNoEsplicito", "D37", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn("id00");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void noImplicitoNoEsplicitoCampoObbligatorioNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaNoImplicitoNoEsplicito regola = new RegolaObbligatorietaNoImplicitoNoEsplicito("RegolaObbligatorietaNoImplicitoNoEsplicito", "D37", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D37", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void siImplicitoNoEsplicitoCampoObbligatorioNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaNoImplicitoNoEsplicito regola = new RegolaObbligatorietaNoImplicitoNoEsplicito("RegolaObbligatorietaNoImplicitoNoEsplicito", "D37", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("identificativoAssistito")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("identificativoAssistito", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaObbligatorietaNoImplicitoNoEsplicito regola = new RegolaObbligatorietaNoImplicitoNoEsplicito("RegolaObbligatorietaNoImplicitoNoEsplicito", "D37", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("identificativoAssistito");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("identificativoAssistito", recordMockito));
    }
}