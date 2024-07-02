/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegolaListaValoriAmmessiCampoDiversoCampoUgualeTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoDaValidare", "valoreCampoDaValidare");
        parametri.put("valoreCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("valoreCampoCondizionanteDue", "valoreCampoCondizionanteDue");
        parametri.put("parametroCampoCondizionante", "W012A");
        parametri.put("parametroCampoCondizionanteDue", "VAL");

        parametri.put("listaValoriAmmessi", "J003A|J031A");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCampoDiversoCampoUguale regola = new RegolaListaValoriAmmessiCampoDiversoCampoUguale();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCampoDiversoCampoUguale);
    }

    @Test
        // TEST OK: valoreCampoDaValidare presente in lista v.a. e parametroCampoCondizionante!= valoreCampoCondizionanteDue e valoreCampoDaValidare == parametroCampoCondizionanteDue
    void RegolaListaValoriAmmessiCondizionataCampoDiversoCampoUgualeOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("VAL");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("J031A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("!W012A");


        var regola = new RegolaListaValoriAmmessiCampoDiversoCampoUguale(
                "regolaListaValoriAmmessiCampoDiversoCampoUguale", "BR157", "Codice BR157", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
        // TEST KO: valoreCampoDaValidare presente in lista v.a. e parametroCampoCondizionante!= valoreCampoCondizionanteDue e valoreCampoDaValidare != parametroCampoCondizionanteDue
    void RegolaListaValoriAmmessiCondizionataCampoDiversoCampoUgualeKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_VAL");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("J031A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("!W012A");


        var regola = new RegolaListaValoriAmmessiCampoDiversoCampoUguale(
                "regolaListaValoriAmmessiCampoDiversoCampoUguale", "BR157", "Codice BR157", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriAmmessiCampoDiversoCampoUguale regola = new RegolaListaValoriAmmessiCampoDiversoCampoUguale("regolaListaValoriAmmessiCampoDiversoCampoUguale", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("valoreCampoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("valoreCampoDaValidare", dtoGenerico));
    }
}
