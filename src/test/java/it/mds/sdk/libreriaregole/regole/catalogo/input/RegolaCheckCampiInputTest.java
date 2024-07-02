/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.input;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaB29Diretta;
import it.mds.sdk.libreriaregole.regole.diretta.RegolaDataCorrentePosterioreAnnoMeseRif;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaCheckCampiInputTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Mock
    CampiInputBean campiInputBean;

    @Test
    void validaCostruttore() {
        RegolaCheckCampiInput regola = new RegolaCheckCampiInput();
        assertTrue(regola instanceof RegolaCheckCampiInput);
    }

    @Test
    void testRegolaCheckCampiInputOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroInput", "codiceRegioneInput");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaCheckCampiInput regola = new RegolaCheckCampiInput("RegolaCheckCampiInput", "B01", "descrizioneErrore", parametriTest);
        Mockito.when(campiInputBean.getCodiceRegioneInput()).thenReturn("020");
        Mockito.when(recordMockito.getCampo("regioneErogante")).thenReturn("020");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("regioneErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testRegolaCheckCampiInputAnnoRifOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroInput", "annoRiferimentoInput");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaCheckCampiInput regola = new RegolaCheckCampiInput("RegolaCheckCampiInput", "B01", "descrizioneErrore", parametriTest);
        Mockito.when(campiInputBean.getAnnoRiferimentoInput()).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("annoRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("annoRiferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testRegolaCheckCampiInputPeriodoRifOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroInput", "periodoRiferimentoInput");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaCheckCampiInput regola = new RegolaCheckCampiInput("RegolaCheckCampiInput", "B01", "descrizioneErrore", parametriTest);
        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn("S1");
        Mockito.when(recordMockito.getCampo("periodoRiferimento")).thenReturn("S1");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("periodoRiferimento", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void testRegolaCheckCampiInputDefaultKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametroInput", "periodoRiferimentoInput");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        RegolaCheckCampiInput regola = new RegolaCheckCampiInput("RegolaCheckCampiInput", "B01", "descrizioneErrore", parametriTest);
        Mockito.when(campiInputBean.getPeriodoRiferimentoInput()).thenReturn("S1");
        Mockito.when(recordMockito.getCampo("periodoRiferimento")).thenReturn("Invalid Value");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("periodoRiferimento", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B01", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCheckCampiInput regola = new RegolaCheckCampiInput("RegolaCheckCampiInput", "B01", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("periodoRiferimento");
        assertThrows(ValidazioneImpossibileException.class,() -> regola.valida("periodoRiferimento", recordMockito));
    }
}