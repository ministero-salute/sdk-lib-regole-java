/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

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
public class RegolaObbligatorietaResTypeTest {
    @Mock
    RecordDtoGenerico dtoGenerico;
    Parametri parametri;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("valoreCampoCondizionanteDue", "valoreCampoCondizionanteDue");
        parametri.put("valoreCampoCondizionante", "valoreCampoCondizionante");
        parametri.put("parametroCampoCondizionante", "StringaDaConfrontare");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
    }

    @Test
    void costruttoreVuoto() {
        RegolaObbligatorietaResType regola = new RegolaObbligatorietaResType();
        assertTrue(regola instanceof RegolaObbligatorietaResType);
    }

    @Test
    void regolaObbligatorietaResTypeOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaObbligatorietaResType regola = new RegolaObbligatorietaResType("RegolaObbligatorietaResType", "BR038", "Codice BR038", parametri);

        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("StringaDaConfrontare");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn(2D);
        Mockito.when(dtoGenerico.getCampo("campoDaValidare")).thenReturn(3D);

        var listaEsiti = regola.valida("campoDaValidare", dtoGenerico);
        listaEsiti.forEach(e -> assertTrue(e.isValoreEsito()));
    }

    @Test
    void regolaObbligatorietaResTypeKOValueNotBetterOrEquals() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaObbligatorietaResType regola = new RegolaObbligatorietaResType("RegolaObbligatorietaResType", "BR038", "Codice BR038", parametri);

        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("StringaDaConfrontare");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn(1D);
        Mockito.when(dtoGenerico.getCampo("campoDaValidare")).thenReturn(0D);

        var listaEsiti = regola.valida("campoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> {
            assertFalse(e.isValoreEsito());
            assertEquals("BR038", e.getErroriValidazione().get(0).getCodice());
        });
    }

    @Test
    void regolaObbligatorietaResTypeOKValuesNotEquals() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaObbligatorietaResType regola = new RegolaObbligatorietaResType("RegolaObbligatorietaResType", "BR038", "Codice BR038", parametri);

        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("StringaDaConfrontare1");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn(1D);
        Mockito.when(dtoGenerico.getCampo("campoDaValidare")).thenReturn(0D);

        var listaEsiti = regola.valida("campoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> {
            assertTrue(e.isValoreEsito());
        });
    }

    @Test
    void regolaObbligatorietaResTypeOKForNullValues() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RegolaObbligatorietaResType regola = new RegolaObbligatorietaResType("RegolaObbligatorietaResType", "BR038", "Codice BR038", parametri);

        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionanteDue")).thenReturn("StringaDaConfrontare");
        Mockito.when(dtoGenerico.getCampo("valoreCampoCondizionante")).thenReturn(0D);
        Mockito.when(dtoGenerico.getCampo("campoDaValidare")).thenReturn(null);

        var listaEsiti = regola.valida("campoDaValidare", dtoGenerico);

        listaEsiti.forEach(e -> {
            assertTrue(e.isValoreEsito());
        });
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaObbligatorietaResType regola = new RegolaObbligatorietaResType("RegolaObbligatorietaResType", "BR038", "Codice BR038", parametri);
        BDDMockito.willThrow(new IllegalAccessException()).given(dtoGenerico).getCampo("campoDaValidare");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", dtoGenerico));
    }

}
