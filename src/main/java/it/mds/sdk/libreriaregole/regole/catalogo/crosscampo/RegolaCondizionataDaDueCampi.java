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
@XmlDiscriminatorValue("regolaCondizionataDaDueCampi")
public class RegolaCondizionataDaDueCampi extends RegolaGenerica {

    public RegolaCondizionataDaDueCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il campo in input "nomeCampo" assuma un valore prefissato(parametro valoreCampo)
     * se il campo2 del DTO = parametro1 e il campo3 del DTO != parametro2
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        try {
            //restype
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String parametroCampoValidare = this.getParametri().getParametriMap().get("parametroCampoValidare");

            //anmethtype
            String campoDipendente1 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoDipendente1"));
            String parametroCampoDipendente1 = this.getParametri().getParametriMap().get("parametroCampoDipendente1");

            //progid
            String campoDipendente2 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoDipendente2"));
            String parametroCampoDipendente2 = this.getParametri().getParametriMap().get("parametroCampoDipendente2");

            if (parametroCampoValidare.equals(campoDaValidare) && !parametroCampoDipendente1.equals(campoDipendente1) &&
                    !parametroCampoDipendente2.equals(campoDipendente2)) {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola CondizionataDaDueCampi per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola CondizionataDaDueCampi per il campo " + nomeCampo);
        }
        return listaEsiti;
    }

}
