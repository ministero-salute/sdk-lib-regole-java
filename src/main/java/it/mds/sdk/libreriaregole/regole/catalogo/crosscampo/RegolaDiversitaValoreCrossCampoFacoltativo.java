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
@XmlDiscriminatorValue("regolaDiversitaValoreCrossCampoFacoltativo")
public class RegolaDiversitaValoreCrossCampoFacoltativo extends RegolaGenerica {

    public RegolaDiversitaValoreCrossCampoFacoltativo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla Se il campo in input "nomeCampo" assume un valore prefissato o null(parametro valoreCampo)
     * verifica che un altro campo dello stesso DTO (parametro campoDipendente )
     * abbia un valore diverso da uno prefissato(parametro valoreDipendente)
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();

        try {
            String valoreCampo = this.getParametri().getParametriMap().get("valoreCampo");

            String nomeCampoConfronto = this.getParametri().getParametriMap().get("campoDipendente");
            String valoreCampoDipendente = String.valueOf(recordDtoGenerico.getCampo(nomeCampoConfronto));
            String valoreConfronto = this.getParametri().getParametriMap().get("valoreDipendente");

            if (recordDtoGenerico.getCampo(nomeCampo) != null) {
                if (String.valueOf(recordDtoGenerico.getCampo(nomeCampo)).equals(valoreCampo)
                        && valoreCampoDipendente != null
                        && !valoreCampoDipendente.equals(valoreConfronto)) {

                    listaEsiti.add(creaEsitoOk(nomeCampo));

                } else {
                    listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                            //.withDescrizione("Se il campo : " + nomeCampo + " vale " + valoreCampo + " il campo  " + nomeCampoConfronto + " deve essere nullo o deve essere diverso da  " + valoreConfronto)
                }
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }


        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola diversitaValoreCrossCampoFacoltativo per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola diversitaValoreCrossCampoFacoltativo per il campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
