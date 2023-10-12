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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
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
class RegolaDominioValoriAnagraficaConcatenatiPeriodoAnnoTest {

    @Mock
    RecordDtoGenerico recordMockito;
    @Mock
    GestoreAnagrafica gestoreAnagrafica = Mockito.mock(GestoreAnagrafica.class);
    TabellaAnagrafica tabellaAnagrafica = Mockito.mock(TabellaAnagrafica.class);

    @Test
    void validaCostruttore() {
        RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno regola = new RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno();
        assertTrue(regola instanceof RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno);
    }

    @Test
    void campoDaValidareNullOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno regola = new RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno("RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno", "B42", "descrizioneErrore", null);
        Mockito.when(recordMockito.getCampo("nomeCampo")).thenReturn(null);
        List<Esito> result = regola.valida("nomeCampo", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void meseNonValidoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_DIR");
        parametri.put("separatore", ";");
        parametri.put("mese", "meseDiRiferimento");
        parametri.put("anno", "annoDiRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno regola = new RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno("RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno", "B42", "descrizioneErrore", parametriTest);
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("12345");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("13");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        List<Esito> result = regola.valida("codiceAziendaSanitariaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("B42", e.getErroriValidazione().get(0).getCodice());
        }
    }

    @Test
    void anagraficaKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_DIR");
        parametri.put("separatore", "#");
        parametri.put("mese", "meseDiRiferimento");
        parametri.put("anno", "annoDiRiferimento");
        parametri.put("listaCampiDaConcatenare", "annoRiferimento|codiceRegione|codiceAziendaSanitariaRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "2022#010#12345"));
        recordAnagraficas.add(new RecordAnagrafica(LocalDate.of(1901, 1, 1).atStartOfDay(), LocalDate.of(2100, 1, 1).atStartOfDay(), "mock#mock"));

        RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno regola = spy(new RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno("TEST", "TEST", "TEST", parametriTest));
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("12345");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("11");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("codiceAziendaSanitariaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void anagraficaKONullDate() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, MalformedRegistryException, RegistryNotFoundException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_DIR");
        parametri.put("separatore", "#");
        parametri.put("mese", "meseDiRiferimento");
        parametri.put("anno", "annoDiRiferimento");
        parametri.put("listaCampiDaConcatenare", "annoRiferimento|codiceRegione|codiceAziendaSanitariaRiferimento");
        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        List<RecordAnagrafica> recordAnagraficas = new ArrayList<>();
        recordAnagraficas.add(new RecordAnagrafica(null, null, "2022#010#12345"));
        recordAnagraficas.add(new RecordAnagrafica(null, null, "mock#mock"));

        RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno regola = spy(new RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno("TEST", "TEST", "TEST", parametriTest));
        Mockito.when(recordMockito.getCampo("codiceAziendaSanitariaErogante")).thenReturn("12345");
        Mockito.when(recordMockito.getCampo("meseDiRiferimento")).thenReturn("11");
        Mockito.when(recordMockito.getCampo("annoDiRiferimento")).thenReturn("2022");
        Mockito.when(regola.getGestoreAnagrafica()).thenReturn(gestoreAnagrafica);
        doReturn(tabellaAnagrafica).when(gestoreAnagrafica).richiediAnagrafica(any(), any(), anyBoolean());
        Mockito.when(tabellaAnagrafica.getRecordsAnagrafica()).thenReturn(recordAnagraficas);
        List<Esito> result = regola.valida("codiceAziendaSanitariaErogante", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno regola = new RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno("RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno", "B42", "descrizioneErrore", null);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("codiceAziendaSanitariaErogante");
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("codiceAziendaSanitariaErogante", recordMockito));
    }
}