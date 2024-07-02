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
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaBR4020ConParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;
    Parametri parametriTest;
    RegolaBR4020ConParametro regola;



    void init(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_AVN_ASL");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaBR4020ConParametro("TEST", "TEST", "TEST", parametriTest));
    }

    void initY(){

        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "nomeTabella");
        parametri.put("modalitaRegola", "Y");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaBR4020ConParametro("TEST", "TEST", "TEST", parametriTest));
    }

    void initD(){
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "nomeTabella");
        parametri.put("modalitaRegola", "D");
        parametri.put("dataRiferimento", "dataRiferimento");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaBR4020ConParametro("TEST", "TEST", "TEST", parametriTest));
    }

    @Test
    void costruttoreVuoto() {
        RegolaBR4020ConParametro regola = new RegolaBR4020ConParametro();
        assertTrue(regola instanceof RegolaBR4020ConParametro);
    }

    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        init();
        Mockito.when(recordMockito.getCampo("nomeCampo")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceAslSomministrazione")).thenReturn("999");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("999");


        List<Esito> list = regola.valida("nomeCampo", recordMockito);
        for (Esito e : list) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        init();
        Mockito.when(recordMockito.getCampo("nomeCampo")).thenReturn("!999999");
        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceAslSomministrazione")).thenReturn("999");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("999");

        List<Esito> list = regola.valida("nomeCampo", recordMockito);
        for (Esito e : list) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void validaKO2Exception() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        init();
        TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
        GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
        Mockito.when(recordMockito.getCampo("nomeCampo")).thenReturn("!999999");
        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceAslSomministrazione")).thenReturn("!999");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("!999");


        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"com#reg#asl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"mock#mock"));

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);


        Assertions.assertThrows(ValidazioneImpossibileException.class, ()->regola.valida("nomeCampo", recordMockito));
    }

    @Test
    void validaOK2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        initD();
        TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
        GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
        Mockito.when(recordMockito.getCampo("nomeCampo")).thenReturn("!999999");
        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceAslSomministrazione")).thenReturn("!999");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("!999");


        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"com#reg#asl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"mock#mock"));

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        Mockito.when(recordMockito.getCampo("dataRiferimento")).thenReturn("2022-01-02");

        List<Esito> list = regola.valida("nomeCampo", recordMockito);
        for (Esito e : list) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaOK3() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        initY();
        TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
        GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
        CampiInputBean campiInputBean = Mockito.mock(CampiInputBean.class);

        Mockito.when(recordMockito.getCampo("nomeCampo")).thenReturn("!999999");
        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("999999");
        Mockito.when(recordMockito.getCampo("codiceAslSomministrazione")).thenReturn("!999");
        Mockito.when(recordMockito.getCampo("codiceComuneSomministrazione")).thenReturn("!999");


        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"com#reg#asl"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"mock#mock"));

        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);
        Mockito.when(campiInputBean.getAnnoRiferimentoInput()).thenReturn("2022");

        List<Esito> list = regola.valida("nomeCampo", recordMockito);
        for (Esito e : list) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        init();
        when(recordMockito.getCampo(any())).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("nomeCampo", recordMockito));
    }

}