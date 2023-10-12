package it.mds.sdk.libreriaregole.regole.catalogo.date;

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
class RegolaDataAntecedenteCondizionataCampoFacoltativoTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataAntecedenteCondizionataCampoFacoltativo regola = new RegolaDataAntecedenteCondizionataCampoFacoltativo();
        assertTrue(regola instanceof RegolaDataAntecedenteCondizionataCampoFacoltativo);
    }

    @Test
    void testDataDaValidareNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataAntecedenteCondizionataCampoFacoltativo regola = new RegolaDataAntecedenteCondizionataCampoFacoltativo("regolaDataAntecedenteCondizionataCampoFacoltativo", "DataAntecedenteCondizionataCampoFacoltativo", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn(null);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testDataDaValidareDiversoNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataAntecedenteCondizionataCampoFacoltativo regola = new RegolaDataAntecedenteCondizionataCampoFacoltativo("regolaDataAntecedenteCondizionataCampoFacoltativo", "regolaDataAntecedenteCondizionataCampoFacoltativo", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1986-05-27");
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("2022-10-20");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("RE");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataAntecedenteCondizionataCampoFacoltativo", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testDataDaValidareModalitaDiversaOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataAntecedenteCondizionataCampoFacoltativo regola = new RegolaDataAntecedenteCondizionataCampoFacoltativo("regolaDataAntecedenteCondizionataCampoFacoltativo", "DataAntecedenteCondizionataCampoFacoltativo", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1986-05-27");
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("2022-10-20");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("RT");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataAntecedenteCondizionataCampoFacoltativo regola = new RegolaDataAntecedenteCondizionataCampoFacoltativo("regolaDataAntecedenteCondizionataCampoFacoltativo", "regolaDataAntecedenteCondizionataCampoFacoltativo", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataTrasferimento");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataTrasferimento", recordMockito));
    }

    @Test
    void testDataTrasferimentoExceptionKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataAntecedenteCondizionataCampoFacoltativo regola = new RegolaDataAntecedenteCondizionataCampoFacoltativo("regolaDataAntecedenteCondizionataCampoFacoltativo", "regolaDataAntecedenteCondizionataCampoFacoltativo", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1986-13-27");
        Mockito.when(recordMockito.getCampo("dataNascita")).thenReturn("2022-10-20");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("RE");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataAntecedenteCondizionataCampoFacoltativo", e.getErroriValidazione().get(0).getCodice());
        }
    }
}