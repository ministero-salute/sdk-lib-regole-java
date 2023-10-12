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
class RegolaDataAntecedenteDataTrasmissioneCampoFacoltativoTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo regola = new RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo();
        assertTrue(regola instanceof RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo);
    }

    @Test
    void testDataTrasferimentoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo regola = new RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo("regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("1979-01-25");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testDataTrasferimentoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo regola = new RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo("regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("2050-11-03");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testDataTrasferimentoNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo regola = new RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo("regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn(null);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testDataTrasferimentoException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo regola = new RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo("regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("2022-13-01");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo regola = new RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo("regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "regolaDataAntecedenteDataTrasmissioneCampoFacoltativo", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataTrasferimento");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataTrasferimento", recordMockito));
    }
}