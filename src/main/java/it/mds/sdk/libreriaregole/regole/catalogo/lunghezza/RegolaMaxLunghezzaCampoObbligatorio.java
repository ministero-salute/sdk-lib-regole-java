package it.mds.sdk.libreriaregole.regole.catalogo.lunghezza;

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
@XmlDiscriminatorValue("regolaMaxLunghezzaCampoObbligatorio")
public class RegolaMaxLunghezzaCampoObbligatorio extends RegolaGenerica {

    public RegolaMaxLunghezzaCampoObbligatorio(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che la lunghezza del campo sia minore di un parametro
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String daValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            Integer maxLunghezza = Integer.parseInt(this.getParametri().getParametriMap().get("maxLunghezza"));

            if ((daValidare != null) && (daValidare.length() <= maxLunghezza)) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                      //  .withDescrizione("La lunghezza del campo " + nomeCampo +" è maggiore rispetto al valore consentito "+ maxLunghezza +" o il campo obbligatario non è presente")

            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NumberFormatException e) {
            log.error("Non è possibile validare la regola lunghezza del campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola lunghezza del campo " + nomeCampo);
        }
        return listaEsiti;
    }

}
