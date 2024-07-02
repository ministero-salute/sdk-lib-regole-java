/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RegolaIncoerenzaSitoInoculazioneTest
{
    @Mock
    RecordDtoGenerico dtoGenerico;
    RegolaIncoerenzaSitoIncoculazione regola;

    @BeforeEach
    void init(){
        regola = new RegolaIncoerenzaSitoIncoculazione("TEST", "TEST", "TEST", null);
    }

    @Test
    void validaOk() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Mockito.when(dtoGenerico.getCampo("sitoInoculazione")).thenReturn("07");
        Mockito.when(dtoGenerico.getCampo("viaSomministrazione")).thenReturn("99");
        var listaEsiti = regola.valida("sitoInoculazione", dtoGenerico);
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }


    @Test
    void validaO2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Mockito.when(dtoGenerico.getCampo("sitoInoculazione")).thenReturn("071");
        Mockito.when(dtoGenerico.getCampo("viaSomministrazione")).thenReturn("199");
        var listaEsiti = regola.valida("sitoInoculazione", dtoGenerico);
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));

    }

    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Mockito.when(dtoGenerico.getCampo("sitoInoculazione")).thenReturn("07");
        Mockito.when(dtoGenerico.getCampo("viaSomministrazione")).thenReturn("199");
        var listaEsiti = regola.valida("sitoInoculazione", dtoGenerico);
        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));

    }

    @Test
    void costruttoreVuoto() {
        RegolaIncoerenzaSitoIncoculazione regola = new RegolaIncoerenzaSitoIncoculazione();
        assertTrue(regola instanceof RegolaIncoerenzaSitoIncoculazione);
    }
    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        var regola = new RegolaIncoerenzaSitoIncoculazione(
                "TEST", "TEST", "TEST", null
        );
        Mockito.when(dtoGenerico.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", dtoGenerico));
    }
}
