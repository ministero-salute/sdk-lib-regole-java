/* SPDX-License-Identifier: BSD-3-Clause */

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
class RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalitaTest {
    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita();
        assertTrue(regola instanceof RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita);
    }

    @Test
    void testOK1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita("regolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita", "", "descrizioneErrore", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("codiceRegioneResidenza")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("codiceRegioneDomicilio")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("MR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testOK2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita("regolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita", "", "descrizioneErrore", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("010");
        Mockito.when(recordMockito.getCampo("codiceRegioneResidenza")).thenReturn("020");
        Mockito.when(recordMockito.getCampo("codiceRegioneDomicilio")).thenReturn("010");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("MV");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita("regolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita", "", "descrizioneErrore", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("010");
        Mockito.when(recordMockito.getCampo("codiceRegioneResidenza")).thenReturn("020");
        Mockito.when(recordMockito.getCampo("codiceRegioneDomicilio")).thenReturn("020");
        Mockito.when(recordMockito.getCampo("modalita")).thenReturn("MV");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita regola = new RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita("regolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}