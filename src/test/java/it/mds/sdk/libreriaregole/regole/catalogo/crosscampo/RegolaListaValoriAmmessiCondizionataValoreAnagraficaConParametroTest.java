/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
    TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
    CampiInputBean campiInputBean = Mockito.mock(CampiInputBean.class);
    Parametri parametriTest;

    RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro regola;


    void initY(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("codiceNazione", "valoreCodiceNazione");
        parametri.put("campoCondizionante", "tipoErogatore");
        parametri.put("listaValoriAmmessi", "2|3|4|5|7|10|11|12");
        parametri.put("modalitaRegola", "Y");
        parametri.put("nomeTabella", "ANAG_AVN_ASL");
        parametri.put("dataRiferimento", "2022-01-01");

        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(
                new RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro("TEST", "TEST", "TEST", parametriTest)
        );
    }

    void initD(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("codiceNazione", "valoreCodiceNazione");
        parametri.put("campoCondizionante", "tipoErogatore");
        parametri.put("listaValoriAmmessi", "2|3|4|5|7|10|11|12");
        parametri.put("modalitaRegola", "D");
        parametri.put("nomeTabella", "ANAG_AVN_ASL");
        parametri.put("dataRiferimento", "dataSomministrazione");

        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(
                new RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro("TEST", "TEST", "TEST", parametriTest)
        );
    }

    void initModalitaNull(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("codiceNazione", "valoreCodiceNazione");
        parametri.put("campoCondizionante", "tipoErogatore");
        parametri.put("listaValoriAmmessi", "2|3|4|5|7|10|11|12");
        parametri.put("nomeTabella", "ANAG_AVN_ASL");
        parametri.put("dataRiferimento", "dataSomministrazione");

        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(
                new RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro("TEST", "TEST", "TEST", parametriTest)
        );
    }

    @Test
    void costruttoreVuoto() {
        RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro regola = new RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro);
    }
    @Test
    void validaOKmodalitaY() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        initY();
        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"150"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"150"));

        Mockito.when(recordMockito.getCampo("codStruttura")).thenReturn("08000401");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("2");
        Mockito.when(recordMockito.getCampo("modalitaRegola")).thenReturn("Y");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        Mockito.when(campiInputBean.getAnnoRiferimentoInput()).thenReturn("2022");
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("codStruttura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaOKmodalitaD() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        initD();
        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"150"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"150"));

        Mockito.when(recordMockito.getCampo("codStruttura")).thenReturn("08000401");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("2");
        Mockito.when(recordMockito.getCampo("modalitaRegola")).thenReturn("D");
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2022-01-01");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        Mockito.when(campiInputBean.getAnnoRiferimentoInput()).thenReturn("2022");
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);


        List<Esito> result = regola.valida("codStruttura", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOModalitaNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        initModalitaNull();
        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"150"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"150"));

        Mockito.when(recordMockito.getCampo("codStruttura")).thenReturn("08000401");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("2");
        Mockito.when(recordMockito.getCampo("modalitaRegola")).thenReturn("D");
        Mockito.when(recordMockito.getCampo("dataSomministrazione")).thenReturn("2022-01-01");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        Mockito.when(campiInputBean.getAnnoRiferimentoInput()).thenReturn("2022");
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));

    }

    @Test
    void validaOKCampoDaValidareNull(){
        initY();
        List<Esito> result = regola.valida(null, recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOListaVuota() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        initY();
        Mockito.when(recordMockito.getCampo("codStruttura")).thenReturn("08000401");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("2");
        Mockito.when(recordMockito.getCampo("modalitaRegola")).thenReturn("Y");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        Mockito.when(campiInputBean.getAnnoRiferimentoInput()).thenReturn("2022");
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(Collections.EMPTY_LIST);

        List<Esito> result = regola.valida("codStruttura", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        initY();
        Mockito.when(recordMockito.getCampo("codStruttura")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));

    }

}