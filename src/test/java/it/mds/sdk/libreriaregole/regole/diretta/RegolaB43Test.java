package it.mds.sdk.libreriaregole.regole.diretta;

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
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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
class RegolaB43Test {

    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
    TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
    RegolaB43 regola;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_DIR_STRUTT_EROG_R");
        parametri.put("parametroCondizionante", "tipoErogatore");
        parametri.put("listaValoriAmmessi", "01|02|03|06");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaB43("TEST", "TEST", "TEST", parametriTest));
    }

    @Test
    void validaCostruttore() {
        RegolaB43 regola = new RegolaB43();
        assertTrue(regola instanceof RegolaB43);
    }

    @Test
    void codiceStrutturaEroganteNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn(null);
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void tipoCanaleDiversoDaROK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("T");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("12354");
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void listaValoriDiversoParametroCondizionanteOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("05");
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceStrutturaErogante");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceStrutturaErogante", recordMockito));
    }

    @Test
    void campoDaValidareAnagraficaOK1() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("07");
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoDaValidareAnagraficaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "02#123#123"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("09");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoDaValidareAnagraficaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "03#123#123"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("09");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void campoDaValidareAnagraficaOK2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "03#123"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("03");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("09");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void campoDaValidareAnagraficaKO2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "04#123"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("03");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("09");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void validaOK_DateNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(null, null, "03#123"));

        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("03");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("09");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaOK1_DateNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(null, null, "02#123"));

        Mockito.when(recordMockito.getCampo("tipoCanale")).thenReturn("R");
        Mockito.when(recordMockito.getCampo("codiceStrutturaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("123");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("02");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("09");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("codiceStrutturaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

}