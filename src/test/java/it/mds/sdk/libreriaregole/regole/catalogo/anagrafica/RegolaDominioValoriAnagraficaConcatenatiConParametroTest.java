package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.TabellaAnagrafica;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaDominioValoriAnagraficaConcatenatiConParametroTest {

    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    GestoreAnagrafica gestoreAnagrafica;
    @Mock
    CampiInputBean campiInputBean;
    TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);
    RegolaDominioValoriAnagraficaConcatenatiConParametro regola;
    Parametri parametriTest;

    void initY() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_SISM_ASL");
        parametri.put("separatore", "#");
        parametri.put("modalitaRegola", "Y");
        parametri.put("listaCampiDaConcatenare", "anno|codRegione|codAsl");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaDominioValoriAnagraficaConcatenatiConParametro("RegolaDominioValoriAnagraficaConcatenatiConParametro", "AAA", "AAA", parametriTest));
    }

    void initD() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_SISM_ASL");
        parametri.put("separatore", "#");
        parametri.put("modalitaRegola", "D");
        parametri.put("dataRiferimento", "dataRiferimento");
        parametri.put("listaCampiDaConcatenare", "anno|codRegione|codAsl");
        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        regola = spy(new RegolaDominioValoriAnagraficaConcatenatiConParametro("RegolaDominioValoriAnagraficaConcatenatiConParametro", "AAA", "AAA", parametriTest));
    }

    @Test
    void testinitYOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        initY();
        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "2022#200#308"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "2022#200#309"));

        Mockito.when(campiInputBean.getAnnoRiferimentoInput()).thenReturn("2022");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);

        Mockito.when(recordMockito.getCampo("anno")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("codRegione")).thenReturn("200");
        Mockito.when(recordMockito.getCampo("codAsl")).thenReturn("308");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testinitDOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        initD();
        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "2022#200#308"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "2022#200#309"));

        Mockito.when(campiInputBean.getAnnoRiferimentoInput()).thenReturn("2022");
        Mockito.when(recordMockito.getCampiInput()).thenReturn(campiInputBean);

        Mockito.when(recordMockito.getCampo("anno")).thenReturn("2022");
        Mockito.when(recordMockito.getCampo("codRegione")).thenReturn("200");
        Mockito.when(recordMockito.getCampo("codAsl")).thenReturn("308");
        Mockito.when(recordMockito.getCampo("dataRiferimento")).thenReturn("2022-08-08");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);

        List<Esito> result = regola.valida("campoDaValidare", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

}