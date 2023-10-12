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
class RegolaNoImplicitoNoEsplicitoMaggioreZeroTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaNoImplicitoNoEsplicitoMaggioreZero regola = new RegolaNoImplicitoNoEsplicitoMaggioreZero();
        assertTrue(regola instanceof RegolaNoImplicitoNoEsplicitoMaggioreZero);
    }


    @Test
    void noImplicitoNoEsplicitoValoreDiversoDaZeroOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaNoImplicitoNoEsplicitoMaggioreZero regola = new RegolaNoImplicitoNoEsplicitoMaggioreZero("RegolaNoImplicitoNoEsplicitoMaggioreZero", "D07", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("idContatto")).thenReturn("0001");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("idContatto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void siImplicitoNoEsplicitoValoreSoloZeriOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaNoImplicitoNoEsplicitoMaggioreZero regola = new RegolaNoImplicitoNoEsplicitoMaggioreZero("RegolaNoImplicitoNoEsplicitoMaggioreZero", "D07", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("idContatto")).thenReturn("00000");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("idContatto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void noImplicitoNoEsplicitoValoreSoliZeriKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaNoImplicitoNoEsplicitoMaggioreZero regola = new RegolaNoImplicitoNoEsplicitoMaggioreZero("RegolaNoImplicitoNoEsplicitoMaggioreZero", "D07", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("idContatto")).thenReturn("0000");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("idContatto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D07", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void idContattoNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaNoImplicitoNoEsplicitoMaggioreZero regola = new RegolaNoImplicitoNoEsplicitoMaggioreZero("RegolaNoImplicitoNoEsplicitoMaggioreZero", "D07", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("idContatto")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = regola.valida("idContatto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("D07", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaNoImplicitoNoEsplicitoMaggioreZero regola = new RegolaNoImplicitoNoEsplicitoMaggioreZero("RegolaNoImplicitoNoEsplicitoMaggioreZero", "D07", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("idContatto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("idContatto", recordMockito));
    }
}