package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class RegolaDominioValoreAnagraficaResloqTest { //NOSONAR
    @Mock
    RecordDtoGenerico recordMockito;
    Parametri parametri;
    @Mock
    GestoreAnagrafica gestoreAnagrafica;

    @BeforeEach
    void getMockedMap() {
        Map<String, String> parametri = new HashMap<>();
        parametri.put("progId", "progId");
        parametri.put("paramCode", "paramCode");
        parametri.put("resInfoNotSummed", "resInfoNotSummed");
        parametri.put("accredproc", "accredproc");

        parametri.put("y", "valoreParametroY");
        parametri.put("V999A", "valoreParametroV999A");
        parametri.put("MCG", "valoreParametroMCG");
        parametri.put("termCode", "valoreParametroTermCode");
        parametri.put("nomeTabella", "valoreNomeTabella");


        Parametri parametriTest = new Parametri();
        parametriTest.setParametriMap(parametri);
        this.parametri = parametriTest;
        Assertions.assertTrue(true);
    }
//    @Test //TODO: Test in sospeso, poichè è da decidere con il team come trattare il Gestore anagrafiche.
    void valida() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        var list = List.of(
                new RecordAnagrafica(
                        LocalDateTime.parse("1920-11-01T10:15:30"),
                        LocalDateTime.parse("2920-11-01T10:15:30" ),
                        "valoreParametroTermCode"
                ));

        Mockito.when(recordMockito.getCampo("progid")).thenReturn("valoreParametroMCG");
        Mockito.when(recordMockito.getCampo("paramcode")).thenReturn("valoreParametroMCG");
        Mockito.when(recordMockito.getCampo("resInfoNotsummed")).thenReturn("valoreParametroY");
        Mockito.when(recordMockito.getCampo("accredproc")).thenReturn("valoreParametroV999A");
        Mockito.when(recordMockito.getCampo("resloq")).thenReturn("valoreResLoq");
        try {
//            try (MockedStatic<GestoreAnagrafica> utilities = Mockito.mockStatic(GestoreAnagrafica.class)) {
//                utilities.when(() -> GestoreAnagrafica.richiediAnagrafica("ANAG_DPM_REGIONI", false).getRecordsAnagrafica()).thenReturn(list);

            Mockito.when(gestoreAnagrafica.richiediAnagrafica("valoreNomeTabella",false).getRecordsAnagrafica()).thenReturn(list);
            RegolaDominioValoreAnagraficaResloq regola = new RegolaDominioValoreAnagraficaResloq("regolaObbligatorietaCodazallorig", "BR153", "Codice BR153", parametri);
            var listaEsiti = regola.valida("resloq", recordMockito );

            listaEsiti.forEach(e -> assertFalse(e.isValoreEsito()));

        } catch (MalformedRegistryException | RegistryNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

}
