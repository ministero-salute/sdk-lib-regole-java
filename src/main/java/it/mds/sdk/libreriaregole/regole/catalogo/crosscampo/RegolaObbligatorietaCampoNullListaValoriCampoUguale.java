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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaListaValoriAmmessiCondizionataDueCampiUguali")
public class RegolaObbligatorietaCampoNullListaValoriCampoUguale extends RegolaGenerica {

    public RegolaObbligatorietaCampoNullListaValoriCampoUguale(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     se PROGID = PSD e RESLOQ non è valorizzato e paramType = P004A or P005A
     allora RESINFO_NOTSUMMED= Y
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            //resInfoNotSummed
            String valoreCampoDaValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
            //progId
            String valoreCampoCondizionante = String.valueOf(recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionante"))
            ); //valore del campo1 del DTO
            //resLoq F
            Object valoreCampoCondizionante2 = recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionante2")); //valore2 del campo del DTO
            //paramType F
            Object valoreCampoCondizionante3 = recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("nomeCampoCondizionante3"));

            //PSD
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");
            //Y
            String parametroCondizionante2 = this.getParametri().getParametriMap().get("parametroCampoCondizionanteDue");

            //Lista da 1 a N dei valori ammessi per il campo da validare
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if(valoreCampoCondizionante.equals(parametroCondizionante) &&
                    valoreCampoCondizionante2 == null &&
                    valoreCampoCondizionante3 != null && listaValori.contains(String.valueOf(valoreCampoCondizionante3)) &&
                    !valoreCampoDaValidare.equals(parametroCondizionante2)
            ){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata due campi uguali " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata due campi uguali " + nomeCampo);
        }

    }
}