/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.validator;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.gestoreesiti.modelli.ValidazioneFlusso;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import lombok.Getter;

import java.util.List;

@Getter
public class InteroFlussoEsito {

    private List<RecordDtoGenerico> recordAccettati;
    private List<ValidazioneFlusso> esitoValidazione;

    public InteroFlussoEsito(List<RecordDtoGenerico> recordAccettati, List<ValidazioneFlusso> esitoValidazione) {
        this.recordAccettati = recordAccettati;
        this.esitoValidazione = esitoValidazione;
    }
}
