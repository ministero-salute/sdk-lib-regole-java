package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioTest {

    @Mock
    RecordDtoGenerico recordMockito;
    RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilio regola;
    Parametri parametriTest;

    @Test
    void costruttoreVuoto() {
        RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilio regola = new RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilio();
        assertTrue(regola instanceof RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilio);
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("codiceRegioneResidenza", "codiceRegioneResidenza");
        parametri.put("codiceRegioneDomicilio", "codiceRegioneDomicilio");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilio("RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilio", "AAA", "AAA", parametriTest));
    }

    @Test
    void testOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        Mockito.when(recordMockito.getCampo("regioneSomministazione")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("codiceRegioneResidenza")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("codiceRegioneDomicilio")).thenReturn("100");

        List<Esito> result = regola.valida("regioneSomministazione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        Mockito.when(recordMockito.getCampo("regioneSomministazione")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("codiceRegioneResidenza")).thenReturn("110");
        Mockito.when(recordMockito.getCampo("codiceRegioneDomicilio")).thenReturn("120");

        List<Esito> result = regola.valida("regioneSomministazione", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("AAA", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("regioneSomministazione")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("regioneSomministazione", recordMockito));

    }
}