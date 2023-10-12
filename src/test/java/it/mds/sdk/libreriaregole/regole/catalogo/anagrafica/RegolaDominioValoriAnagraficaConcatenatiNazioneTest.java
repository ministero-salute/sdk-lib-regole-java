package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

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
class RegolaDominioValoriAnagraficaConcatenatiNazioneTest {

    @Mock
    RecordDtoGenerico recordMockito;
    RegolaDominioValoriAnagraficaConcatenatiNazione regola;
    Parametri parametriTest;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("codiceNazione", "codiceNazione");
        parametri.put("parametroCodiceNazione", "IT");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaDominioValoriAnagraficaConcatenatiNazione("RegolaDominioValoriAnagraficaConcatenatiNazione", "AAA", "AAA", parametriTest));
    }

    @Test
    void costruttoreVuoto() {
        RegolaDominioValoriAnagraficaConcatenatiNazione regola = new RegolaDominioValoriAnagraficaConcatenatiNazione();
        assertTrue(regola instanceof RegolaDominioValoriAnagraficaConcatenatiNazione);
    }

    @Test
    void testOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("campoDaValidare")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("codiceNazione")).thenReturn("FR");
        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
}