package it.mds.sdk.libreriaregole.regole.catalogo;

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
@XmlDiscriminatorValue("regolaListaValoriNonAmmessi")
public class RegolaListaValoriNonAmmessi extends RegolaGenerica {

    public RegolaListaValoriNonAmmessi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che il campo di ingresso, non sia contenuto in un dominio predefinito (listaValoriNonAmmessi) e
     * che un altro campo del DTO(ad esempio via di Somministrazione) risulti diverso da 2 volori prefissati
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String campoDaValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
            String viaSomministrazione = this.getParametri().getParametriMap().get("viaSomministrazione");

            String valoreViaSomministrazione = (String) recordDtoGenerico.getCampo(viaSomministrazione);
            List<String> listaValori1 = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriNonAmmessi1").split("\\|")).collect(Collectors.toList());
            List<String> listaValori2 = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriNonAmmessi2").split("\\|")).collect(Collectors.toList());

            if (!listaValori1.contains(campoDaValidare)) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                if (listaValori2.contains(valoreViaSomministrazione)) {
                    listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                            //.withDescrizione("Valore del campo  " + nomeCampo + " non ammesso - i valori ammessi sono" + listaValori2)

                } else {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                }
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori non ammessi per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori non ammessi per il campo " + nomeCampo);
        }
        return listaEsiti;

    }
}
