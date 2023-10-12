package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void costruttoreVuoto() {
        RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro regola = new RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro();
        assertTrue(regola instanceof RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro);
    }

    @Test
    void testOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro regola = new RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro("RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro", "AAA", "AAA", null);
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenThrow(new IllegalAccessException());
        RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro regola = new RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro("RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro", "AAA", "AAA", null);
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));

    }
}