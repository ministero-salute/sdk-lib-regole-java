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
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regola4040")
public class Regola4040 extends RegolaGenerica {

    public Regola4040(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {

            Object codiceAslSomministrazione = recordDtoGenerico.getCampo(nomeCampo);
            Object codiceComuneSomministrazione = recordDtoGenerico.getCampo("codiceComuneSomministrazione");
            Object codiceRegioneSomministrazione = recordDtoGenerico.getCampo("codiceRegioneSomministrazione");

            log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

            if (("999".equals(String.valueOf(codiceAslSomministrazione)) && "999999".equals(String.valueOf(codiceComuneSomministrazione)) && "999".equals(String.valueOf(codiceRegioneSomministrazione)))
                    ||
                    (!"999".equals(String.valueOf(codiceAslSomministrazione)) && !"999999".equals(String.valueOf(codiceComuneSomministrazione)) && !"999".equals(String.valueOf(codiceRegioneSomministrazione))
                    )) {
                return List.of(creaEsitoOk(nomeCampo));
            } else {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }


}
