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
public class RegolaIncoerenzaStatoEsteroConComuneASL2Test
{
    @Mock
    RecordDtoGenerico dtoGenerico;
    RegolaIncoerenzaStatoEsteroConComuneASL2 regola;

    @BeforeEach
    void init(){
        regola = new RegolaIncoerenzaStatoEsteroConComuneASL2("TEST", "TEST", "TEST", null);
    }
    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Mockito.when(dtoGenerico.getCampo("statoEsteroSomministrazione")).thenReturn("IT");
        Mockito.when(dtoGenerico.getCampo("codiceComuneResidenza")).thenReturn("666666");
        Mockito.when(dtoGenerico.getCampo("codiceAslResidenza")).thenReturn("666");
        var listaEsiti = regola.valida("statoEsteroSomministrazione", dtoGenerico);
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void validaKO1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Mockito.when(dtoGenerico.getCampo("statoEsteroSomministrazione")).thenReturn("IT");
        Mockito.when(dtoGenerico.getCampo("codiceComuneResidenza")).thenReturn("999999");
        Mockito.when(dtoGenerico.getCampo("codiceAslResidenza")).thenReturn("666");
        var listaEsiti = regola.valida("statoEsteroSomministrazione", dtoGenerico);
        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }
    @Test
    void validaKO2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Mockito.when(dtoGenerico.getCampo("statoEsteroSomministrazione")).thenReturn("IT");
        Mockito.when(dtoGenerico.getCampo("codiceComuneResidenza")).thenReturn("666666");
        Mockito.when(dtoGenerico.getCampo("codiceAslResidenza")).thenReturn("999");
        var listaEsiti = regola.valida("statoEsteroSomministrazione", dtoGenerico);
        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }


    @Test
    void costruttoreVuoto() {
        RegolaIncoerenzaStatoEsteroConComuneASL2 regola = new RegolaIncoerenzaStatoEsteroConComuneASL2();
        assertTrue(regola instanceof RegolaIncoerenzaStatoEsteroConComuneASL2);
    }
    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        var regola = new RegolaIncoerenzaStatoEsteroConComuneASL2(
                "TEST", "TEST", "TEST", null
        );
        Mockito.when(dtoGenerico.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", dtoGenerico));
    }
}
