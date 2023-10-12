package it.mds.sdk.libreriaregole.regole.diretta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaStessoSegnoTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaStessoSegno regola = new RegolaStessoSegno();
        assertTrue(regola instanceof RegolaStessoSegno);
    }

    @ParameterizedTest
    @CsvSource({
    	"0001,1",
    	"0000,1",
    	"0001,"
    })
    void testStessoSegnoOK(String idContatto, String costoServizio) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaStessoSegno regola = new RegolaStessoSegno("stessoSegno", "B41", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("idContatto")).thenReturn(idContatto);
        Mockito.when(recordMockito.getCampo("costoServizio")).thenReturn(costoServizio);
        Mockito.when(recordMockito.getCampo("costoAcquisto")).thenReturn("2");
        List<Esito> result = regola.valida("idContatto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testDiversoSegnoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaStessoSegno regola = new RegolaStessoSegno("stessoSegno", "B41", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("idContatto")).thenReturn("0001");
        Mockito.when(recordMockito.getCampo("costoServizio")).thenReturn("1");
        Mockito.when(recordMockito.getCampo("costoAcquisto")).thenReturn("-2");
        List<Esito> result = regola.valida("idContatto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B41", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaStessoSegno regola = new RegolaStessoSegno("stessoSegno", "B41", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("idContatto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("idContatto", recordMockito));
    }

    @Test
    void testNullPointerException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaStessoSegno regola = new RegolaStessoSegno("stessoSegno", "B41", "descrizioneErrore", null);
        BDDMockito.willThrow(new NullPointerException()).given(recordMockito).getCampo("idContatto");
        List<Esito> result = regola.valida("idContatto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B41", e.getErroriValidazione().get(0).getCodice());
        }
    }

}