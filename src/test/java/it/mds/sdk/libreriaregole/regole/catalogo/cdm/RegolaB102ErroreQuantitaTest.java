/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.cdm;

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
class RegolaB102ErroreQuantitaTest {

    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
    TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
    Parametri parametriTest;
    RegolaB102ErroreQuantita regola;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_CDM_CND_ALLEGATO");
        parametri.put("separatore", "#");
        parametri.put("listaCampiDaConcatenare", "identificativoDiIscrizioneInBancaDatiRepertorio|tipoDispositivoMedico");
        parametri.put("quantitaContrattualizzata", "quantitaContrattualizzata");
        parametri.put("annoStipulaContratto", "annoStipulaContratto");
        parametri.put("annoConfronto", "2016");

        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaB102ErroreQuantita("TEST", "TEST", "TEST", parametriTest));
    }

    @Test
    void validaCostruttore() {
        RegolaB102ErroreQuantita regola = new RegolaB102ErroreQuantita();
        assertTrue(regola instanceof RegolaB102ErroreQuantita);
    }

    @Test
    void validaOkSeCampoDaValidareNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        Mockito.when(recordMockito.getCampo("quantitaAggiudicata")).thenReturn(null);
        Mockito.when(recordMockito.getCampo("annoStipulaContratto")).thenReturn("2017");
        Mockito.when(recordMockito.getCampo("identificativoDiIscrizioneInBancaDatiRepertorio")).thenReturn("smcb");
        Mockito.when(recordMockito.getCampo("tipoDispositivoMedico")).thenReturn("smcl");

        List<Esito> result = regola.valida("durataDelContratto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaOk() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "smcb#smcl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        Mockito.when(recordMockito.getCampo("quantitaAggiudicata")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("annoStipulaContratto")).thenReturn("2017");
        Mockito.when(recordMockito.getCampo("quantitaContrattualizzata")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("identificativoDiIscrizioneInBancaDatiRepertorio")).thenReturn("smcb");
        Mockito.when(recordMockito.getCampo("tipoDispositivoMedico")).thenReturn("smcl");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("quantitaAggiudicata", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaOkSeAnnoStipulaMinoreAnnoConfronto() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "smcb#smcl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        Mockito.when(recordMockito.getCampo("quantitaAggiudicata")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("annoStipulaContratto")).thenReturn("2015");
        Mockito.when(recordMockito.getCampo("quantitaContrattualizzata")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("identificativoDiIscrizioneInBancaDatiRepertorio")).thenReturn("smcb");
        Mockito.when(recordMockito.getCampo("tipoDispositivoMedico")).thenReturn("smcl");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("quantitaAggiudicata", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaOkSeValoriNonInAnagrafica() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "smcb#smcl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        Mockito.when(recordMockito.getCampo("quantitaAggiudicata")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("annoStipulaContratto")).thenReturn("2017");
        Mockito.when(recordMockito.getCampo("quantitaContrattualizzata")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("identificativoDiIscrizioneInBancaDatiRepertorio")).thenReturn("smcb");
        Mockito.when(recordMockito.getCampo("tipoDispositivoMedico")).thenReturn("smbc");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("quantitaAggiudicata", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOSeQtaAggiudicataMinoreContrattualizzata() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "smcb#smcl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        Mockito.when(recordMockito.getCampo("quantitaAggiudicata")).thenReturn("99");
        Mockito.when(recordMockito.getCampo("annoStipulaContratto")).thenReturn("2017");
        Mockito.when(recordMockito.getCampo("quantitaContrattualizzata")).thenReturn("100");
        Mockito.when(recordMockito.getCampo("identificativoDiIscrizioneInBancaDatiRepertorio")).thenReturn("smcb");
        Mockito.when(recordMockito.getCampo("tipoDispositivoMedico")).thenReturn("smcl");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("quantitaAggiudicata", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("TEST", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Mockito.when(recordMockito.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("quantitaAggiudicata", recordMockito));
    }
}