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
@XmlDiscriminatorValue("regolaCampoFacoltativoCondizionataListaValoriAmmessi")
public class RegolaCampoFacoltativoCondizionataListaValoriAmmessi extends RegolaGenerica {

    public RegolaCampoFacoltativoCondizionataListaValoriAmmessi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Il valore è nullo e il campo Tipologia erogatore è diverso da “6” e da “99”’
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            String valoreCampoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);

            String nomeCampoCondizionante = this.getParametri().getParametriMap().get("nomeCampoCondizionante");//campo del DTO
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(nomeCampoCondizionante)); //valore del campo del DTO

            //Lista da 1 a N dei valori ammessi per il campo da validare
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if(valoreCampoDaValidare == null && !listaValori.contains(valoreCampoCondizionante)){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo);
        }

    }
}