package it.mds.sdk.libreriaregole.regole.catalogo.date;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
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
class RegolaDataNonCompresaNelPeriodoRiferimentoTest {
    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    CampiInputBean campiInputBean;

    @Test
    void validaCostruttore() {
        RegolaDataNonCompresaNelPeriodoRiferimento regola = new RegolaDataNonCompresaNelPeriodoRiferimento();
        assertTrue(regola instanceof RegolaDataNonCompresaNelPeriodoRiferimento);
    }
    @Test
    void testDataTrasferimentoOK() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataNonCompresaNelPeriodoRiferimento regola = new RegolaDataNonCompresaNelPeriodoRiferimento("regolaDataNonCompresaNelPeriodoRiferimento", "regolaDataNonCompresaNelPeriodoRiferimento", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn("2022-10-20");
        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn("Q1");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }@Test
    void testDataTrasferimentoKO() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDataNonCompresaNelPeriodoRiferimento regola = new RegolaDataNonCompresaNelPeriodoRiferimento("regolaDataNonCompresaNelPeriodoRiferimento", "regolaDataNonCompresaNelPeriodoRiferimento", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("dataTrasferimento")).thenReturn(null);
        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn("Q1");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("dataTrasferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("regolaDataNonCompresaNelPeriodoRiferimento", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDataNonCompresaNelPeriodoRiferimento regola = new RegolaDataNonCompresaNelPeriodoRiferimento("regolaDataNonCompresaNelPeriodoRiferimento", "regolaDataNonCompresaNelPeriodoRiferimento", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("dataTrasferimento");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("dataTrasferimento", recordMockito));
    }
}