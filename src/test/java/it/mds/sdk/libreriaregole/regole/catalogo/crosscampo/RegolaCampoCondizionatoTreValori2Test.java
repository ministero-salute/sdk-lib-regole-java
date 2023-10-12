package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.catalogo.anagrafica.RegolaDominioValoriAnagraficaSplitFacoltativoListaValoriControlloCampi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegolaCampoCondizionatoTreValori2Test {

    @Mock
    RecordDtoGenerico recordDtoGenerico;
    Parametri parametriTest;
    RegolaCampoCondizionatoTreValori2 regola;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> parametri = new HashMap<>();
        parametri.put("nomeTabella", "ANAG_SISM_DSM");
        parametri.put("parametroCondizionante1", "parametroCondizionante1");
        parametri.put("listaValoriAmmessi", "01|02");
        parametri.put("parametroCondizionante2", "parametroCondizionante2");
        parametri.put("campoCondizionante1", "campoCondizionante1");
        parametri.put("campoCondizionante2", "campoCondizionante2");

        parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        regola = spy(new RegolaCampoCondizionatoTreValori2("TEST", "TEST", "TEST", parametriTest));
    }

    @Test
    void valida() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn("campo").when(recordDtoGenerico).getCampo(any());
        var result = regola.valida("nomeCampo", recordDtoGenerico);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }
    @Test
    void validaKO() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn("01").when(recordDtoGenerico).getCampo("nomeCampo");
        doReturn("parametroCondizionante1").when(recordDtoGenerico).getCampo("campoCondizionante1");
        doReturn("campoCondizionante2_no").when(recordDtoGenerico).getCampo("campoCondizionante2");

        var result = regola.valida("nomeCampo", recordDtoGenerico);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
        }
    }

    @Test
    void validaKO2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doThrow(IllegalAccessException.class).when(recordDtoGenerico).getCampo("nomeCampo");
        doReturn("parametroCondizionante1").when(recordDtoGenerico).getCampo("campoCondizionante1");
        doReturn("campoCondizionante2_no").when(recordDtoGenerico).getCampo("campoCondizionante2");

        Assertions.assertThrows(
                ValidazioneImpossibileException.class,
                ()->regola.valida("nomeCampo", recordDtoGenerico)
        );
    }

}
