package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
@XmlDiscriminatorValue("regolaVerificaEta")
public class RegolaVerificaEta extends RegolaGenerica {

    public RegolaVerificaEta(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * La regola consente di effettuare il calcolo dell'età mediante
     * sottrazione di due input verificando che il risultato sia compreso tra 18 e 100 anni
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        List<Esito> listaEsiti = new ArrayList<>();
        try {
            Integer campoDaValidare = (Integer) recordDtoGenerico.getCampo(nomeCampo);
            String nomeCampoValidatore = this.getParametri().getParametriMap().get("campoValidatore");

            //Parametrizzazione intervallo età

            Integer etaMin = Integer.parseInt(this.getParametri().getParametriMap().get("etaMin"));
            Integer etaMax = Integer.parseInt(this.getParametri().getParametriMap().get("etaMax"));

            Integer comparante = (Integer) recordDtoGenerico.getCampo(nomeCampoValidatore);
            Integer eta = comparante - campoDaValidare;

            if ((eta >= etaMin) && (eta <= etaMax)) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                        //.withDescrizione("L'età deve essere compresa tra " + etaMin + " e " + etaMax)
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola VerificaEta per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola VerificaEta per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}

