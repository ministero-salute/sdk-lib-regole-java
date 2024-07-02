/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
class RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalitaTest {
    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    CampiInputBean campiInputBean;

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita();
        assertTrue(regola instanceof RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita);
    }

    @Test
    void testKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita("regolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita", "", "descrizioneErrore", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("080");
        Mockito.when(recordMockito.getCampo("codiceRegioneResidenza")).thenReturn("090");
        Mockito.when(campiInputBean.getCodiceRegioneInput()).thenReturn("020");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("MV");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita("regolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita", "", "descrizioneErrore", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("090");
        Mockito.when(recordMockito.getCampo("codiceRegioneResidenza")).thenReturn("090");
        Mockito.when(campiInputBean.getCodiceRegioneInput()).thenReturn("020");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("MV");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita("regolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}