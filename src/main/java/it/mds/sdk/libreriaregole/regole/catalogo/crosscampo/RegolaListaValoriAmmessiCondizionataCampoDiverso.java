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
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaListaValoriAmmessiCondizionataCampoDiverso")
public class RegolaListaValoriAmmessiCondizionataCampoDiverso extends RegolaGenerica {

    public RegolaListaValoriAmmessiCondizionataCampoDiverso(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che la stringa o l'intero in ingresso, sia contenuto in un dominio predefinito (parametro listaValoriAmmessi)
     * se un altro campo del DTO è diverso ad un parametro
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String valoreCampoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);

            String nomeCampoCondizionante = this.getParametri().getParametriMap().get("nomeCampoCondizionante");//campo del DTO
            String valoreCampoCondizionante = (String) recordDtoGenerico.getCampo(nomeCampoCondizionante); //valore del campo del DTO

            // parametro con cui devo confrontare il valoreCampoCondizionante
            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");

            //Lista da 1 a N dei valori ammessi per il campo da validare
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());
            if(!Objects.equals(valoreCampoCondizionante,parametroCampoCondizionante) &&
                    !listaValori.contains(valoreCampoDaValidare)
            ){
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo);
        }
        return listaEsiti;

    }
}