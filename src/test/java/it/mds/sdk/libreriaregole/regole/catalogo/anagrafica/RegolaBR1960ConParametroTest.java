package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.TabellaAnagrafica;
import it.mds.sdk.gestoreesiti.modelli.ErroreValidazione;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaBR1960ConParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;
    Parametri parametriTest;

    RegolaBR1960ConParametro regola;


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_AVN_ASL");
        parametri.put("separatore", "|");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaBR1960ConParametro("TEST", "TEST", "TEST", parametriTest));
    }

    @Test
    void costruttoreVuoto() {
        RegolaBR1960ConParametro regola = new RegolaBR1960ConParametro();
        assertTrue(regola instanceof RegolaBR1960ConParametro);
    }

    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        Mockito.when(recordMockito.getCampo("progid")).thenReturn("NOT_999");

        var list = new ArrayList<Esito>();
        var errorList = new ArrayList<ErroreValidazione>();
        list.add(new Esito("test", "test", false, errorList));
        Mockito.doReturn(list).when((RegolaDominioValoriAnagraficaConcatenatiConParametro)regola).valida(any(), any());

        List<Esito> result = regola.valida("progid", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }
    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        Mockito.when(recordMockito.getCampo("progid")).thenReturn("999");

        var list = new ArrayList<Esito>();
        var errorList = new ArrayList<ErroreValidazione>();
        list.add(new Esito("test", "test", true, errorList));
   
        List<Esito> result = regola.valida("progid", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        Mockito.when(recordMockito.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("progid", recordMockito));
    }

}