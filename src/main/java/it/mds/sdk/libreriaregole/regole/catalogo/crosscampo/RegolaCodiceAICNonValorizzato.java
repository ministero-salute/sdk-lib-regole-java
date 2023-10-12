package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
import org.springframework.cglib.core.Local;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCodiceAICNonValorizzato")
public class RegolaCodiceAICNonValorizzato extends RegolaGenerica {

    public RegolaCodiceAICNonValorizzato(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Il campo Codice Regione non coincide con la regione che sta trasmettendo il file.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            LocalDate primoLuglio2019 = LocalDate.of(2019, Month.JULY, 01);
            String codiceAIC = (String) (recordDtoGenerico.getCampo(nomeCampo));
            String dataSomministrazione = (String)recordDtoGenerico.getCampo("dataSomministrazione");
            String statoEsteroSomministrazione = (String) recordDtoGenerico.getCampo("statoEsteroSomministrazione");

            if(codiceAIC != null){
                return List.of(creaEsitoOk(nomeCampo));
            }

            if(LocalDate.parse(dataSomministrazione).isAfter(primoLuglio2019) &&
                    (statoEsteroSomministrazione == null || "it".equalsIgnoreCase(statoEsteroSomministrazione))
            ){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaCodiceAIC per il campo " + nomeCampo);
        }catch (NullPointerException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }

    }

}
