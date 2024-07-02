/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.diretta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegolaAnonimatoImplicitoTest {

	RegolaAnonimatoImplicito sut ;
   
	
	@BeforeEach
	void init() {
		sut = new RegolaAnonimatoImplicito("testAnonimatoImplicitoOK", "", "descrizioneErrore", null);
	}
	

    @Test
    
    void validaCostruttore() {
        RegolaAnonimatoImplicito regola = new RegolaAnonimatoImplicito();
        assertTrue(regola instanceof RegolaAnonimatoImplicito);
    }

    @Test
    void testAnonimatoImplicitoOK() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	//preparazione
    	RecordDtoGenerico recordMockito=Mockito.mock(RecordDtoGenerico.class);
        
        Mockito.when(recordMockito.getCampo("tipoErogazione")).thenReturn("04");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("06");
        //esecuzione
        List<Esito> result = sut.valida("tipoErogatore", recordMockito);
        //verifica
        for (Esito e : result) {
            assertTrue(e.isValoreEsito());
        }
    }

    @Test
    void testAnonimatoImplicitoKO() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    	RecordDtoGenerico recordMockito=Mockito.mock(RecordDtoGenerico.class);
    	
    	Mockito.when(recordMockito.getCampo("tipoErogatore")).thenReturn("07");
        Mockito.when(recordMockito.getCampo("tipoStrutturaErogante")).thenReturn("04");
        List<Esito> result = sut.valida("tipoErogatore", recordMockito);
        for (Esito e : result) {
            assertFalse(e.isValoreEsito());
            assertEquals("", e.getErroriValidazione().get(0).getCodice());
        }
    }
    @Test
    void testException() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	RecordDtoGenerico recordMockito=Mockito.mock(RecordDtoGenerico.class);
        BDDMockito.willThrow(new IllegalAccessException()).given(recordMockito).getCampo("tipoErogatore");
        assertThrows(ValidazioneImpossibileException.class,() -> sut.valida("tipoErogatore", recordMockito));
    }
}