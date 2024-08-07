/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
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
class RegolaEsistenzaUguaglianzaCrossCampoTest {
    @Mock
    RecordDtoGenerico recordMockito;
    //se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE

    @Test
    void validaCostruttore() {
        RegolaEsistenzaUguaglianzaCrossCampo regola = new RegolaEsistenzaUguaglianzaCrossCampo();
        assertTrue(regola instanceof RegolaEsistenzaUguaglianzaCrossCampo);
    }

    @Test
    void esistenzaUguaglianzaCrossCampoValidareNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        //Esempio: se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "statoEsteroResidenza");
        parametri.put("listaValoriCoerenti", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaUguaglianzaCrossCampo regola = new RegolaEsistenzaUguaglianzaCrossCampo("regolaEsistenzaUguaglianzaCrossCampo", "", "desrizioneErrore", parametriTest);

        Mockito.when(recordMockito.getCampo("campoConfronto")).thenReturn(null);
        List<Esito> result = regola.valida("campoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void esistenzaUguaglianzaCrossCampoValidareDiversoNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        //Esempio: se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "statoEsteroResidenza");
        parametri.put("listaValoriCoerenti", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaUguaglianzaCrossCampo regola = new RegolaEsistenzaUguaglianzaCrossCampo("regolaEsistenzaUguaglianzaCrossCampo", "", "desrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("campoConfronto")).thenReturn("ABC");
        Mockito.when(recordMockito.getCampo("statoEsteroResidenza")).thenReturn(null);
        List<Esito> result = regola.valida("campoConfronto", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void esistenzaUguaglianzaCrossCampoValidareDiversoNulleCampoDaValidareDiversoNullOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        //Esempio: se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "statoEsteroResidenza");
        parametri.put("listaValoriCoerenti", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaUguaglianzaCrossCampo regola = new RegolaEsistenzaUguaglianzaCrossCampo("regolaEsistenzaUguaglianzaCrossCampo", "", "desrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("campoConfronto")).thenReturn("ABC");
        Mockito.when(recordMockito.getCampo("statoEsteroResidenza")).thenReturn("IT");
        List<Esito> result = regola.valida("campoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeCampoCoerente", "statoEsteroResidenza");
        parametri.put("listaValoriCoerenti", "IT");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaEsistenzaUguaglianzaCrossCampo regola = new RegolaEsistenzaUguaglianzaCrossCampo("regolaEsistenzaUguaglianzaCrossCampo", "", "desrizioneErrore", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("campoConfronto");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoConfronto", recordMockito));
    }
}