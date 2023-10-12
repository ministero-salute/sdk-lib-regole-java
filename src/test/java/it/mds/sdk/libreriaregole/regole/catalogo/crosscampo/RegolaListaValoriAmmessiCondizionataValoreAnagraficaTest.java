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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegolaListaValoriAmmessiCondizionataValoreAnagraficaTest {

    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
    TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
    CampiInputBean campiInputBean = Mockito.mock(CampiInputBean.class);
    Parametri parametriTest;

    RegolaListaValoriAmmessiCondizionataValoreAnagrafica regola;

    @BeforeEach
    void init(){
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
                new RegolaListaValoriAmmessiCondizionataValoreAnagrafica("TEST", "TEST", "TEST", parametriTest)
        );
    }

    @Test
    void costruttoreVuoto() {
        RegolaListaValoriAmmessiCondizionataValoreAnagrafica regola = new RegolaListaValoriAmmessiCondizionataValoreAnagrafica();
        assertTrue(regola instanceof RegolaListaValoriAmmessiCondizionataValoreAnagrafica);
    }


    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"080#004"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(),LocalDate.of(2100, 1, 1).atStartOfDay(),"080#005"));

        Mockito.when(recordMockito.getCampo("codStruttura")).thenReturn("08000401");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("2");
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
    void validaOKCampoDaValidareNull(){
        List<Esito> result = regola.valida(null, recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaKOListaVuota() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException
    {
        Mockito.when(recordMockito.getCampo("codStruttura")).thenReturn("08000401");
        Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("2");
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
        Mockito.when(recordMockito.getCampo("codStruttura")).thenThrow(new IllegalAccessException());
        assertThrows(ValidazioneImpossibileException.class,()->regola.valida("codStruttura", recordMockito));
    }
}
