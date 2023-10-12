package it.mds.sdk.libreriaregole.regole.catalogo.date;

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
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaBR2095")
public class RegolaBR2095 extends RegolaGenerica {

    public RegolaBR2095(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
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

            String dataDecessoString = (String)recordDtoGenerico.getCampo("dataDecesso");
            LocalDate dataDecesso = null;
            if (dataDecessoString != null) {
                dataDecesso = LocalDate.parse((String)recordDtoGenerico.getCampo("dataDecesso"));
            }

            String periodoRiferimento = recordDtoGenerico.getCampiInput().getPeriodoRiferimentoInput();
            List<String> months;

            log.debug("{}.valida - nomeCampo {} - periodoRiferimento {}", this.getClass().getName(), dataDecesso, periodoRiferimento);

            if(dataDecesso != null){
                if("Q1".equalsIgnoreCase(periodoRiferimento)){
                    months = List.of("1","2","3");
                    if(!months.contains(String.valueOf(dataDecesso.getDayOfMonth()))){
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                }else if("Q2".equalsIgnoreCase(periodoRiferimento)){
                    months = List.of("4","5","6");
                    if(!months.contains(String.valueOf(dataDecesso.getDayOfMonth()))){
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                }else if("Q3".equalsIgnoreCase(periodoRiferimento)){
                    months = List.of("7","8","9");
                    if(!months.contains(String.valueOf(dataDecesso.getDayOfMonth()))){
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                }else if("Q4".equalsIgnoreCase(periodoRiferimento)){
                    months = List.of("10","11","12");
                    if(!months.contains(String.valueOf(dataDecesso.getDayOfMonth()))){
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                }
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola2080 del campo " + nomeCampo);
        } catch (DateTimeParseException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }
    }
}

