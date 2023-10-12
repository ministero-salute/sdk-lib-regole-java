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
class RegolaValidazionePeriodoRiferimentoTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaValidazionePeriodoRiferimento regola = new RegolaValidazionePeriodoRiferimento();
        assertTrue(regola instanceof RegolaValidazionePeriodoRiferimento);
    }
    @Test
    void testDataDaValidareNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaValidazionePeriodoRiferimento regola = new RegolaValidazionePeriodoRiferimento("regolaValidazionePeriodoRiferimento", "regolaValidazionePeriodoRiferimento", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("periodoRiferimento")).thenReturn(null);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testDataDaValidareOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaValidazionePeriodoRiferimento regola = new RegolaValidazionePeriodoRiferimento("regolaValidazionePeriodoRiferimento", "regolaValidazionePeriodoRiferimento", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("2020-06-30");
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn("2020");
        Mockito.when(recordMockito.getCampo("periodoRiferimento")).thenReturn("s1");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testDataDaValidareKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaValidazionePeriodoRiferimento regola = new RegolaValidazionePeriodoRiferimento("regolaValidazionePeriodoRiferimento", "regolaValidazionePeriodoRiferimento", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("2021-01-31");
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn("2020");
        Mockito.when(recordMockito.getCampo("periodoRiferimento")).thenReturn("s2");
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaValidazionePeriodoRiferimento", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaValidazionePeriodoRiferimento regola = new RegolaValidazionePeriodoRiferimento("regolaValidazionePeriodoRiferimento", "regolaValidazionePeriodoRiferimento", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataTrasferimento");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataTrasferimento", recordMockito));
    }
}