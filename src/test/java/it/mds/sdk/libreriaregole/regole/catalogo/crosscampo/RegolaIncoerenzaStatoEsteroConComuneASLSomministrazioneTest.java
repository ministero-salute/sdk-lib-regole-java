/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RegolaIncoerenzaStatoEsteroConComuneASLSomministrazioneTest
{
    @Mock
    RecordDtoGenerico dtoGenerico;
    RegolaIncoerenzaStatoEsteroConComuneASLSomministrazione regola;

    @BeforeEach
    void init(){
        regola = new RegolaIncoerenzaStatoEsteroConComuneASLSomministrazione("TEST", "TEST", "TEST", null);
    }
    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Mockito.when(dtoGenerico.getCampo("statoEsteroSomministrazione")).thenReturn("IT");
        Mockito.when(dtoGenerico.getCampo("codiceComuneSomministrazione")).thenReturn("666666");
        Mockito.when(dtoGenerico.getCampo("codiceAslSomministrazione")).thenReturn("666");
        Mockito.when(dtoGenerico.getCampo("codiceRegioneSomministrazione")).thenReturn("666");
        var listaEsiti = regola.valida("statoEsteroSomministrazione", dtoGenerico);
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @ParameterizedTest
    @CsvSource({
    	"999999,666,666",
    	"666666,666,999",
    	"999999,999,666"
    })
    void validaKO1(String codiceComuneSomministrazione, String codiceAslSomministrazione, String codiceRegioneSomministrazione) 
    		throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Mockito.when(dtoGenerico.getCampo("statoEsteroSomministrazione")).thenReturn("IT");
        Mockito.when(dtoGenerico.getCampo("codiceComuneSomministrazione")).thenReturn(codiceComuneSomministrazione);
        Mockito.when(dtoGenerico.getCampo("codiceAslSomministrazione")).thenReturn(codiceAslSomministrazione);
        Mockito.when(dtoGenerico.getCampo("codiceRegioneSomministrazione")).thenReturn(codiceRegioneSomministrazione);
        var listaEsiti = regola.valida("statoEsteroSomministrazione", dtoGenerico);
        listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));
    }

    @Test
    void costruttoreVuoto() {
        RegolaIncoerenzaStatoEsteroConComuneASLSomministrazione regola = new RegolaIncoerenzaStatoEsteroConComuneASLSomministrazione();
        assertTrue(regola instanceof RegolaIncoerenzaStatoEsteroConComuneASLSomministrazione);
    }
    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        var regola = new RegolaIncoerenzaStatoEsteroConComuneASLSomministrazione(
                "TEST", "TEST", "TEST", null
        );
        Mockito.when(dtoGenerico.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", dtoGenerico));
    }
}
