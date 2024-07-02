/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.gestorevalidazione;

import java.util.List;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.regole.beans.RegoleFlusso;
import it.mds.sdk.libreriaregole.validator.ValidationEngine;

public class GestoreValidazione {

    private final ValidationEngine validationEngine;

    public GestoreValidazione(ValidationEngine validationEngine) {
        this.validationEngine = validationEngine;

    }

    public BloccoValidazione gestioneValidazioneBlocco(List<RecordDtoGenerico> bloccoRecord, RegoleFlusso regoleFlusso, String idRun, int numRecordValidati) {
        return validationEngine.validaFlussoBlocco(bloccoRecord, regoleFlusso, idRun, numRecordValidati);
    }

    public List<Esito> gestioneValidazioneRecord(RecordDtoGenerico recordDto, RegoleFlusso regoleFlusso, String idRun){
        return validationEngine.validaSingoloRecord(recordDto,regoleFlusso,idRun);
    }

}
