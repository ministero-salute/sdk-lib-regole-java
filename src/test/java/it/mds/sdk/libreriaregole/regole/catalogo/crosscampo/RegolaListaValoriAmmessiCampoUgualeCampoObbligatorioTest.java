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
public class RegolaListaValoriAmmessiCampoUgualeCampoObbligatorioTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("valoreCampoCondizionanteDue", "valoreCampoCondizionanteDue");
        parametri.put("parametroCampoCondizionante", "J038A");

        parametri.put("listaValoriNonAmmessi", "VIG001CP|MON");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio regola = new RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio);
    }

    @Test
    void RegolaListaValoriAmmessiCampoUgualeCampoObbligatorioTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("VALUE_NOT_IN_LIST");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("J038A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("J038A");

        var regola = new RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio(
                "regolaListaValoriAmmessiCampoUgualeCampoObbligatorio", "BR178", "Codice BR178", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void RegolaListaValoriAmmessiCampoUgualeCampoObbligatorioTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("VALUE_NOT_IN_LIST");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("J038A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("NOT_J038A");

        var regola = new RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio(
                "regolaListaValoriAmmessiCampoUgualeCampoObbligatorio", "BR178", "Codice BR178", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void RegolaListaValoriAmmessiCampoUgualeCampoObbligatorioTestOKDue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("VIG001CP");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("J038A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("NOT_J038A");

        var regola = new RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio(
                "regolaListaValoriAmmessiCampoUgualeCampoObbligatorio", "BR178", "Codice BR178", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio regola = new RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio("regolaListaValoriAmmessiCampoUgualeCampoObbligatorio", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("valoreCampoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("valoreCampoDaValidare", dtoGenerico));
    }
}
