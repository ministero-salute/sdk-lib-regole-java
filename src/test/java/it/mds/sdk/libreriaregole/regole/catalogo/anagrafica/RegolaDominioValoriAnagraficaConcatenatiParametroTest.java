/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaDominioValoriAnagraficaConcatenatiParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;
    RegolaDominioValoriAnagraficaConcatenatiParametro regola;
    Parametri parametriTest;

    @Test
    void costruttoreVuoto() {
        RegolaDominioValoriAnagraficaConcatenatiParametro regola = new RegolaDominioValoriAnagraficaConcatenatiParametro();
        assertTrue(regola instanceof RegolaDominioValoriAnagraficaConcatenatiParametro);
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("parametro", "parametro");
        parametri.put("valoreParametro", "IT");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaDominioValoriAnagraficaConcatenatiParametro("RegolaDominioValoriAnagraficaConcatenatiParametro", "AAA", "AAA", parametriTest));
    }

    @Test
    void testOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn("G050A");
        Mockito.when(recordMockito.getCampo("parametro")).thenReturn("FR");

        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
}