package it.mds.sdk.libreriaregole.testutil;

import it.mds.sdk.gestoreesiti.modelli.Esito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class TestUtils {

    private TestUtils() {
    }

    public static void assertValidationError(String campo, String errorCode, String descriptionErrorCode, List<Esito> esitoList) {
        assertEquals(1, esitoList.size());
        var esito = esitoList.get(0);
        assertFalse(esito.isValoreEsito());
        assertEquals(campo, esito.getCampo());
        assertEquals(1, esito.getErroriValidazione().size());
        var err = esito.getErroriValidazione().get(0);
        assertEquals(errorCode, err.getCodice());
        assertEquals(descriptionErrorCode, err.getDescrizione());

    }
}
