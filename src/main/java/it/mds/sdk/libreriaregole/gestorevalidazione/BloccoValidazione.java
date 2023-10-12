package it.mds.sdk.libreriaregole.gestorevalidazione;

import it.mds.sdk.gestoreesiti.modelli.ValidazioneFlusso;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import lombok.Data;

import java.util.List;

/**
 * Classe che rappresenta la validazione di un blocco.
 */
@Data
public class BloccoValidazione {

    private List<ValidazioneFlusso> validazioneFlussoList;
    private List<RecordDtoGenerico> recordList;
    private int scartati;
    private int numeroRecord;
    private String idRun;

}
