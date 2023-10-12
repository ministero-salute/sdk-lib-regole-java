package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RegolaUguaglianzaCrossCampoCondizionaleTest {

    @Mock
    RecordDtoGenerico recordMockito;

    @Test
    void validaCostruttore() {
        RegolaUguaglianzaCrossCampoCondizionale regola = new RegolaUguaglianzaCrossCampoCondizionale();
        assertTrue(regola instanceof RegolaUguaglianzaCrossCampoCondizionale);
    }

    @Test
    void validaOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("modalitaTrasmissione", "modalitaTrasmissioneParametro");
        parametri.put("parametroModalitaTrasmissione", "MV");
        parametri.put("campoValidatore", "codiceRegione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampoCondizionale regola = new RegolaUguaglianzaCrossCampoCondizionale("uguaglianzaCrossCampoCondizionale", "E4065", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("codiceRegioneSomministrazione")).thenReturn("180");
        Mockito.when(recordMockito.getCampo("modalitaTrasmissioneParametro")).thenReturn("MV");
        Mockito.when(recordMockito.getCampo("codiceRegione")).thenReturn("180");
        List<Esito> result = regola.valida("codiceRegioneSomministrazione", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void validaParametroDiversoOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("modalitaTrasmissione", "modalitaTrasmissioneParametro");
        parametri.put("parametroModalitaTrasmissione", "MV");
        parametri.put("campoValidatore", "codiceRegione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampoCondizionale regola = new RegolaUguaglianzaCrossCampoCondizionale("uguaglianzaCrossCampoCondizionale", "E4065", "descrizioneErrore",parametriTest);
        Mockito.when(recordMockito.getCampo("modalitaTrasmissioneParametro")).thenReturn("TR");
        List<Esito> result = regola.valida("nomeCampoConfronto", recordMockito);
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> parametri = new HashMap<>();
        parametri.put("modalitaTrasmissione", "modalitaTrasmissioneParametro");
        parametri.put("parametroModalitaTrasmissione", "MV");
        parametri.put("campoValidatore", "codiceRegione");

        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);

        RegolaUguaglianzaCrossCampoCondizionale regola = new RegolaUguaglianzaCrossCampoCondizionale("TEST", "TEST", "TEST", parametriTest);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo(any());
        assertThrows(ValidazioneImpossibileException.class, () -> regola.valida("campoDaValidare", recordMockito));
    }
}