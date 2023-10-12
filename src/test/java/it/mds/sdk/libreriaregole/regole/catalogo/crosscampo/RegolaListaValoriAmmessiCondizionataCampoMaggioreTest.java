package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RegolaListaValoriAmmessiCondizionataCampoMaggioreTest {


    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("parametroCampoCondizionante", "parametroCampoCondizionante");

        parametri.put("listaValoriAmmessi", "J003A|J038A");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
    void regolaListaValoriAmmessiCondizionataCampoMaggioreTestOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn(2D);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn(1D);

        var regola = new RegolaListaValoriAmmessiCondizionataCampoMaggiore("TEST", "TEST", "TEST", parametri);
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void regolaListaValoriAmmessiCondizionataCampoMaggioreTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn(2D);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn(3D);

        var regola = new RegolaListaValoriAmmessiCondizionataCampoMaggiore("regolaListaValoriAmmessiCondizionataCampoMaggiore", "BR139", "Codice BR139", parametri);
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void costruttoreVuoto() {
        RegolaListaValoriAmmessiCondizionataCampoMaggiore regola = new RegolaListaValoriAmmessiCondizionataCampoMaggiore();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataCampoMaggiore);
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        var regola = new RegolaListaValoriAmmessiCondizionataCampoMaggiore(
                "RegolaListaValoriAmmessiCondizionataCampoMaggiore", "BR201", "Codice BR201", parametri
        );
        Mockito.when(dtoGenerico.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codStruttura", dtoGenerico));
    }

    @Test
    void regolaListaValoriAmmessiCondizionataCapoDaValidareMinoreValoreTestKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(dtoGenerico.getCampo("valoreCampoDaValidare")).thenReturn(1D);
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn(2D);
        Mockito.when(dtoGenerico.getCampo("parametroCampoCondizionante")).thenReturn("J003A");

        var regola = new RegolaListaValoriAmmessiCondizionataCampoMaggiore("TEST", "TEST", "TEST", parametri);
        var listaEsiti = regola.valida("valoreCampoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }
}
