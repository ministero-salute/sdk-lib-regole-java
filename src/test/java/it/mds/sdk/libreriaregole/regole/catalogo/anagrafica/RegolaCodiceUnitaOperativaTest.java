package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.TabellaAnagrafica;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaCodiceUnitaOperativaTest {

    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    GestoreAnagrafica gestoreAnagrafica;
    TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
    RegolaCodiceUnitaOperativa regola;
    Parametri parametriTest;


    @Test
    void costruttoreVuoto() {
        RegolaCodiceUnitaOperativa regola = new RegolaCodiceUnitaOperativa();
        assertTrue(regola instanceof RegolaCodiceUnitaOperativa);
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_CDM_ASL");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaCodiceUnitaOperativa("RegolaCodiceUnitaOperativa", "B05", "descrizioneErrore", parametriTest));
    }

    @Test
    void test0000OK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        Mockito.when(recordMockito.getCampo("cod_un_op")).thenReturn("0000");
        List<Esito> result = regola.valida("cod_un_op", recordMockito);

        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testInAnagOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "11"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "12"));

        Mockito.when(recordMockito.getCampo("cod_un_op")).thenReturn("1110");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("cod_un_op", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testInAnagKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "11"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "12"));

        Mockito.when(recordMockito.getCampo("cod_un_op")).thenReturn("111A");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("cod_un_op", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B05", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("cod_un_op")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("cod_un_op", recordMockito));
    }
}