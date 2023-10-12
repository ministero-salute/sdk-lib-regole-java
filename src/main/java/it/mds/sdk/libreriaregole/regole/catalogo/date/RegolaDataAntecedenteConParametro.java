package it.mds.sdk.libreriaregole.regole.catalogo.date;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDataAntecedenteConParametro")
public class RegolaDataAntecedenteConParametro extends RegolaDataAntecedente {

    public RegolaDataAntecedenteConParametro(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * In caso di flusso Residenti uguale ad un parametro prefissato, Controlla che il valore del campo passato in ingresso(una data)
     * sia antecedente a un'altra data (se valorizzata) all'interno dello stesso DTO.
     * <p>
     * nomeCampoValidatore é il parametro che contiene il nome del record del DTO con cui voglio comparare il dato in ingresso
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        String parametroTipoFlusso = this.getParametri().getParametriMap().get("parametroTipoFlusso");

        if (parametroTipoFlusso.equals("RE")) {
            listaEsiti = super.valida(nomeCampo, recordDtoGenerico);
        } else {
            listaEsiti.add(creaEsitoOk(nomeCampo));
        }
        return listaEsiti;
    }
}
