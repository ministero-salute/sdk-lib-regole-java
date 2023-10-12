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
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaAntigeneNonValorizzatoCorrettamente")
public class RegolaAntigeneNonValorizzatoCorrettamente extends RegolaGenerica {

    public RegolaAntigeneNonValorizzatoCorrettamente(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se antigene è valorizzato con 08 o 09 e la dataSomministrazione è successiva al 01.01.2019 -> ko
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
            LocalDate primoGennaio2019 = LocalDate.of(2019, Month.JANUARY, 1);
            String antigene = (String) (recordDtoGenerico.getCampo(nomeCampo));
            String dataSomministrazione = (String)recordDtoGenerico.getCampo("dataSomministrazione");
            List<String> listaValoriAntigene = List.of("08", "09");

            if(listaValoriAntigene.contains(antigene) && LocalDate.parse(dataSomministrazione).isAfter(primoGennaio2019)){
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola RegolaCodiceAIC per il campo " + nomeCampo);
        }catch(NullPointerException x){
            log.error("{}.valida - recordDtoGenerico[{}] ",
                    this.getClass().getName(), recordDtoGenerico.toString(), x);
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }

    }

}
