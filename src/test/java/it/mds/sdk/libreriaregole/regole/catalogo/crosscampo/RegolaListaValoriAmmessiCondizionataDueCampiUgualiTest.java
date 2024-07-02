/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RegolaListaValoriAmmessiCondizionataDueCampiUgualiTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void testValoriAmmessiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante1", "valoreCampoCondizionante1");
        parametri.put("nomeCampoCondizionante2", "valoreCampoCondizionante2");
        parametri.put("listaValoriAmmessi", "LOQ|VAL"); //Lista di un solo campo
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataDueCampiUguali regola = new RegolaListaValoriAmmessiCondizionataDueCampiUguali("regolaListaValoriAmmessiCondizionataDueCampiUguali", "BR037", "Codice BR037", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("VAL");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante1")).thenReturn("uguali");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante2")).thenReturn("uguali");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testValoriAmmessiK0() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante1", "valoreCampoCondizionante1");
        parametri.put("nomeCampoCondizionante2", "valoreCampoCondizionante2");
        parametri.put("listaValoriAmmessi", "LOQ|VAL"); //Lista di un solo campo
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataDueCampiUguali regola = new RegolaListaValoriAmmessiCondizionataDueCampiUguali("regolaListaValoriAmmessiCondizionataDueCampiUguali", "BR037", "Codice BR037", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("SDFG");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante1")).thenReturn("uguali");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante2")).thenReturn("uguali");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("BR037", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void testCampiCondizionantiDiversiOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();

        parametri.put("nomeCampoCondizionante1", "valoreCampoCondizionante1");
        parametri.put("nomeCampoCondizionante2", "valoreCampoCondizionante2");
        parametri.put("listaValoriAmmessi", "LOQ|VAL"); //Lista di un solo campo
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaListaValoriAmmessiCondizionataDueCampiUguali regola = new RegolaListaValoriAmmessiCondizionataDueCampiUguali("regolaListaValoriAmmessiCondizionataDueCampiUguali", "BR037", "Codice BR037", parametriTest);
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("SDFG");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante1")).thenReturn("diverso1");
        Mockito.when(recordMockito.getCampo("valoreCampoCondizionante2")).thenReturn("diverso2");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCondizionataDueCampiUguali regola = new RegolaListaValoriAmmessiCondizionataDueCampiUguali();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataDueCampiUguali);
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        var regola = new RegolaListaValoriAmmessiCondizionataDueCampiUguali(
                "TEST", "TEST", "TEST",null
        );
        Mockito.when(recordMockito.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));
    }

}