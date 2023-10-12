package it.mds.sdk.libreriaregole.regole.catalogo.input;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCheckCampiInput")
public class RegolaCheckCampiInput extends RegolaGenerica {

    private static final String ANNO_RIF = "annoRiferimentoInput";
    private static final String PERIODO_RIF = "periodoRiferimentoInput";
    private static final String CODICE_REGIONE = "codiceRegioneInput";

    public RegolaCheckCampiInput(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il valore del campo in input a SDK sia uguale al valore del campo dto nome campo
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {

            String daValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
            String parametroInput = this.getParametri().getParametriMap().get("parametroInput");

            String valoreConfronto = null;

            switch (parametroInput) {
                case ANNO_RIF :  valoreConfronto = recordDtoGenerico.getCampiInput().getAnnoRiferimentoInput();
                    break;
                case PERIODO_RIF:  valoreConfronto = recordDtoGenerico.getCampiInput().getPeriodoRiferimentoInput();
                    break;
                case CODICE_REGIONE:  valoreConfronto = recordDtoGenerico.getCampiInput().getCodiceRegioneInput();
                    break;
                default: valoreConfronto = "Invalid Value";
                    break;
            }

            if (valoreConfronto.equals(daValidare)) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));

            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola RegolaCheckCampiInput per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaCheckCampiInput per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
