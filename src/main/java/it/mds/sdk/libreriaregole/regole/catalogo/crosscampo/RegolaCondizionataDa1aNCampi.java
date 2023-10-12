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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCondizionataDa1aNCampi")
public class RegolaCondizionataDa1aNCampi extends RegolaGenerica {

    public RegolaCondizionataDa1aNCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * La “Modalità conclusione” non è stata valorizzata ma risulta valorizzato almeno
     * uno dei seguenti campi: “data di chiusura scheda paziente” o “diagnosi di chiusura”.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            String valoreCampoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

            List<String> listaCampi = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaCampi").split("\\|")).collect(Collectors.toList());

            if(valoreCampoDaValidare != null)
                return List.of(creaEsitoOk(nomeCampo));

            for(String campo : listaCampi) {
                Object valoreCampo = recordDtoGenerico.getCampo(campo);
                if(valoreCampo != null)
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola ObbligatorietaCondizionataDa1aNCampi per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola ObbligatorietaCondizionataDa1aNCampi per il campo " + nomeCampo);
        }
    }
}

