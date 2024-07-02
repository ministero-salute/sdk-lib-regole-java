/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegolaDominioValoriAnagraficaDiscriminataTipoStrutturaTest {
    @Mock
    RecordDtoGenerico recordDtoGenerico;
    @Mock
    GestoreAnagrafica gestoreAnagrafica;
    @Mock
    TabellaAnagrafica tabellaAnagrafica;

    RegolaDominioValoriAnagraficaDiscriminataTipoStruttura regola;
    Parametri parametriTest;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_SISM_DSM");
        parametri.put("parametroCondizionante", "parametroCondizionante");
        parametri.put("listaValoriAmmessi", "01|02");

        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        regola = spy(new RegolaDominioValoriAnagraficaDiscriminataTipoStruttura("TEST", "TEST", "TEST", parametriTest));
    }
    @Test
    // primo if
    void validaOk1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
//        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "2022#080#030#ADD"));
//        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));
//
        doReturn("01").when(recordDtoGenerico).getCampo("parametroCondizionante");
        doReturn("01").when(recordDtoGenerico).getCampo("nomeCampo");
        doReturn(gestoreAnagrafica).when(regola).getGestoreAnagrafica();
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        doReturn(recordAnagraficas).when(tabellaAnagrafica).getRecordsAnagrafica();

        var result = regola.valida("nomeCampo", recordDtoGenerico);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
        // primo if
    void validaOk2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "2022#080#030#ADD"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        doReturn("01").when(recordDtoGenerico).getCampo("parametroCondizionante");
        doReturn("8359500").when(recordDtoGenerico).getCampo("nomeCampo");
        doReturn(gestoreAnagrafica).when(regola).getGestoreAnagrafica();
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        doReturn(recordAnagraficas).when(tabellaAnagrafica).getRecordsAnagrafica();

        var result = regola.valida("nomeCampo", recordDtoGenerico);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }


    @Test
        // primo if
    void validaOk3() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();

        doReturn("101").when(recordDtoGenerico).getCampo("parametroCondizionante");
        doReturn("8359500").when(recordDtoGenerico).getCampo("nomeCampo");
        var result = regola.valida("nomeCampo", recordDtoGenerico);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
        // primo if
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {

        doThrow(IllegalAccessException.class).when(recordDtoGenerico).getCampo("parametroCondizionante");

        Assertions.assertThrows(
                ValidazioneImpossibileException.class,
                ()->regola.valida("nomeCampo", recordDtoGenerico)
        );
    }

    @Test
    void costruttoreVuoto() {
        RegolaDominioValoriAnagraficaDiscriminataTipoStruttura regola = new RegolaDominioValoriAnagraficaDiscriminataTipoStruttura();
        assertTrue(regola instanceof RegolaDominioValoriAnagraficaDiscriminataTipoStruttura);
    }


}
