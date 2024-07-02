/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.TabellaAnagrafica;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaD51Test {

    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
    TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
    CampiInputBean campiInputBean = Mockito.mock(CampiInputBean.class);
    Parametri parametriTest;

    RegolaD51 regola;


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "nomeTabella");
        parametri.put("campoCondizionante", "campoCondizionante");
        parametri.put("mese", "mese");
        parametri.put("anno", "anno");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaD51("TEST", "TEST", "TEST", parametriTest));
    }

    @Test
    void costruttoreVuoto() {
        RegolaD51 regola = new RegolaD51();
        assertTrue(regola instanceof RegolaD51);
    }

    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"com#reg#asl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"mock#mock"));

        Mockito.when(recordMockito.getCampo("codiceUnitaOperativaUtilizzatrice")).thenReturn("0000");
        Mockito.when(recordMockito.getCampo("campoCondizionante")).thenReturn("NOT_01");
        Mockito.when(recordMockito.getCampo("codiceAslDomicilio")).thenReturn("asl");
        Mockito.when(recordMockito.getCampo("codiceComuneDomicilio")).thenReturn("com");
        Mockito.when(recordMockito.getCampo("mese")).thenReturn("06");
        Mockito.when(recordMockito.getCampo("anno")).thenReturn("2022");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("codiceUnitaOperativaUtilizzatrice", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void validaOK2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"com#reg#asl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"mock#mock"));

        Mockito.when(recordMockito.getCampo("codiceUnitaOperativaUtilizzatrice")).thenReturn("0000");
        Mockito.when(recordMockito.getCampo("campoCondizionante")).thenReturn("01");
        Mockito.when(recordMockito.getCampo("codiceAslDomicilio")).thenReturn("asl");
        Mockito.when(recordMockito.getCampo("codiceComuneDomicilio")).thenReturn("com");
        Mockito.when(recordMockito.getCampo("mese")).thenReturn("06");
        Mockito.when(recordMockito.getCampo("anno")).thenReturn("2022");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("codiceUnitaOperativaUtilizzatrice", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaOK3() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"00"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"00"));

        Mockito.when(recordMockito.getCampo("codiceUnitaOperativaUtilizzatrice")).thenReturn("0001");
        Mockito.when(recordMockito.getCampo("campoCondizionante")).thenReturn("01");
        Mockito.when(recordMockito.getCampo("codiceAslDomicilio")).thenReturn("asl");
        Mockito.when(recordMockito.getCampo("codiceComuneDomicilio")).thenReturn("com");
        Mockito.when(recordMockito.getCampo("mese")).thenReturn("06");
        Mockito.when(recordMockito.getCampo("anno")).thenReturn("2022");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("codiceUnitaOperativaUtilizzatrice", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"com#reg#asl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"mock#mock"));

        Mockito.when(recordMockito.getCampo("codiceUnitaOperativaUtilizzatrice")).thenReturn("00000");
        Mockito.when(recordMockito.getCampo("campoCondizionante")).thenReturn("01");
        Mockito.when(recordMockito.getCampo("codiceAslDomicilio")).thenReturn("asl");
        Mockito.when(recordMockito.getCampo("codiceComuneDomicilio")).thenReturn("com");
        Mockito.when(recordMockito.getCampo("mese")).thenReturn("06");
        Mockito.when(recordMockito.getCampo("anno")).thenReturn("2022");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("codiceUnitaOperativaUtilizzatrice", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void validaKO2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"001"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"001"));

        Mockito.when(recordMockito.getCampo("codiceUnitaOperativaUtilizzatrice")).thenReturn("0001");
        Mockito.when(recordMockito.getCampo("campoCondizionante")).thenReturn("01");
        Mockito.when(recordMockito.getCampo("codiceAslDomicilio")).thenReturn("asl");
        Mockito.when(recordMockito.getCampo("codiceComuneDomicilio")).thenReturn("com");
        Mockito.when(recordMockito.getCampo("mese")).thenReturn("06");
        Mockito.when(recordMockito.getCampo("anno")).thenReturn("2022");

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("codiceUnitaOperativaUtilizzatrice", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }


    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        Mockito.when(recordMockito.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));
    }

}