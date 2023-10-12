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
public class RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaListaTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoDaValidare", "valoreCampoDaValidare");
        parametri.put("valoreCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("valoreCampoCondizionanteDue", "valoreCampoCondizionanteDue");

        parametri.put("parametroCampoCondizionante", "AT06A");

        parametri.put("listaValoriNonAmmessi", "MCG|MON");
        parametri.put("listaValoriNonAmmessiDue", "J003A|J041A");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
    void validaCostruttore() {
        RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista regola = new RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista);
    }

    @Test
    void RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaListaTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_MCG");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("AT06A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("NOT_J003A");

        var regola = new RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista(
                "regolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista", "BR161", "Codice BR161", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));

    }

    @Test
    void RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaListaTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn("NOT_MCG");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn("AT06A");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("J003A");

        var regola = new RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista(
                "regolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista", "BR161", "Codice BR161", parametri
        );
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));

    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista regola = new RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista("regolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista", "", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("valoreCampoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("valoreCampoDaValidare", dtoGenerico));
    }
}
