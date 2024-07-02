/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegolaCoerenzaRegioneCampoInputTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Mock
    CampiInputBean campiInputBean;

    @Test
    void validaCostruttore() {
        RegolaCoerenzaRegioneCampoInput regola = new RegolaCoerenzaRegioneCampoInput();
        assertTrue(regola instanceof RegolaCoerenzaRegioneCampoInput);
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCoerenzaRegioneCampoInput regola = new RegolaCoerenzaRegioneCampoInput("RegolaCoerenzaRegioneCampoInput", "AAA", "Codice", null);

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }

    @ParameterizedTest
    @CsvSource({
    	"020,",
    	",020",
    	"030,020"
    })
    void codRegionNullKO(String codiceRegioneInput, String campo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCoerenzaRegioneCampoInput regola = new RegolaCoerenzaRegioneCampoInput("RegolaCoerenzaRegioneCampoInput", "AAA", "Codice", null);

        Mockito.when(campiInputBean.getCodiceRegioneInput()).thenReturn(codiceRegioneInput);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(campo);
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("AAA", e.getErroriValidazione().get(0).getCodice());
        }
    }

//    @Test
//    void codRegioneInputNullKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//        RegolaCoerenzaRegioneCampoInput regola = new RegolaCoerenzaRegioneCampoInput("RegolaCoerenzaRegioneCampoInput", "AAA", "Codice", null);
//
//        Mockito.when(campiInputBean.getCodiceRegioneInput()).thenReturn(null);
//        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("020");
//        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
//        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
//        for (Esito e : result) {
//            assertFalse(e.isValoreEsito());
//            assertEquals("AAA", e.getErroriValidazione().get(0).getCodice());
//        }
//    }
//
//    @Test
//    void codiceRegioneDiversoDaInputKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//        RegolaCoerenzaRegioneCampoInput regola = new RegolaCoerenzaRegioneCampoInput("RegolaCoerenzaRegioneCampoInput", "AAA", "Codice", null);
//
//        Mockito.when(campiInputBean.getCodiceRegioneInput()).thenReturn("030");
//        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("020");
//        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
//        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
//        for (Esito e : result) {
//            assertFalse(e.isValoreEsito());
//            assertEquals("AAA", e.getErroriValidazione().get(0).getCodice());
//        }
//    }

    @Test
    void codiceRegioneUgualeAInputOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaCoerenzaRegioneCampoInput regola = new RegolaCoerenzaRegioneCampoInput("RegolaCoerenzaRegioneCampoInput", "AAA", "Codice", null);

        Mockito.when(campiInputBean.getCodiceRegioneInput()).thenReturn("030");
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("030");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
}