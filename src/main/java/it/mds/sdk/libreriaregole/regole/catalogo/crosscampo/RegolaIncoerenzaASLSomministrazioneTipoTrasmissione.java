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
import java.util.Objects;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaIncoerenzaASLSomministrazioneTipoTrasmissione")
public class RegolaIncoerenzaASLSomministrazioneTipoTrasmissione extends RegolaGenerica {

    public RegolaIncoerenzaASLSomministrazioneTipoTrasmissione(String nome, String codErrore, String desErrore, Parametri parametri) {
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
            Object codRegioneResidenza = recordDtoGenerico.getCampo(nomeCampo);
            Object tipoTrasmissione = recordDtoGenerico.getCampo("tipoTrasmissione");

            log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

            if (codRegioneResidenza == null || tipoTrasmissione == null ||
                    (Objects.equals("999", codRegioneResidenza) && !Objects.equals("RE", tipoTrasmissione))) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }


}
