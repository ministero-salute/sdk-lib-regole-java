package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaCampoCondizionatoDataSomministrazione")
public class RegolaObbligatorietaCampoCondizionatoDataSomministrazione extends RegolaGenerica {

    public RegolaObbligatorietaCampoCondizionatoDataSomministrazione(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *
     * Veritfica che il campo non sia nullo quando la data somministrazione è successiva a primo luglio
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            String dataPassata = this.getParametri().getParametriMap().get("dataPassata");
            String codiceAIC = (String) (recordDtoGenerico.getCampo(nomeCampo));
            String dataSomministrazione = (String)recordDtoGenerico.getCampo("dataSomministrazione");

            if(codiceAIC != null){
                return List.of(creaEsitoOk(nomeCampo));
            }
            if(LocalDate.parse(dataSomministrazione).isAfter(LocalDate.parse(dataPassata))){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - recordDtoGenerico[{}] ",
                    this.getClass().getName(), recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaCodiceAIC per il campo " + nomeCampo);
        }catch (NullPointerException e) {
            log.error("{}.valida - recordDtoGenerico[{}] ",
                    this.getClass().getName(), recordDtoGenerico.toString(), e);
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }

    }

}
