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
@XmlDiscriminatorValue("regolaObbligatorietaCondizionata")
public class RegolaObbligatorietaCondizionata extends RegolaGenerica {

    public RegolaObbligatorietaCondizionata(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che il campo in input sia valorizzato se delle particolari condizioni siano soddisfatte.
     * Nel caso di AVN, il campo regioneDomicilioSanitario(campo da validare) deve essere valorizzato se almeno uno fra comuneDomicilio
     * e aslDomicilio lo sono e se regioneDomicilioSanitario è diverso da aslResidenza
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String daValidare = (String) recordDtoGenerico.getCampo(nomeCampo); //daValidare è il campo in input (es. regioneDomicilioSanitario)

            String validatore1 = this.getParametri().getParametriMap().get("validatore1");
            String validatore2 = this.getParametri().getParametriMap().get("validatore2");
            String validatore3 = this.getParametri().getParametriMap().get("validatore3");

            String valoreValidatore1 = (String) recordDtoGenerico.getCampo(validatore1);
            String valoreValidatore2 = (String) recordDtoGenerico.getCampo(validatore2);
            String valoreValidatore3 = (String) recordDtoGenerico.getCampo(validatore3);

            if (daValidare != null) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                if ((valoreValidatore1 == null || valoreValidatore2 == null) && valoreValidatore3 == null) {
                    listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                            //.withDescrizione("Il valore del campo " + nomeCampo +
                           //         " non può essere nullo se " + valoreValidatore1 + " o " + valoreValidatore2 + " sono valorizzati e se " + nomeCampo +
                             //       " è diverso da " + valoreValidatore3)

                } else {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Non è possibile validare la regola per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
