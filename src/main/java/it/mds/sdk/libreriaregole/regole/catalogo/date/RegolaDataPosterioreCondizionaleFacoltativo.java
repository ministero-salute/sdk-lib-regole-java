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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDataPosterioreCondizionaleFacoltativo")
public class RegolaDataPosterioreCondizionaleFacoltativo extends RegolaGenerica {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RegolaDataPosterioreCondizionaleFacoltativo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo passato in ingresso(data, se valorizzata) sia posteriore a un'altra data all'interno dello stesso DTO.
     * nomeCampoValidatore é il parametro che contiene il nome del record del DTO con cui voglio comparare il dato in ingresso (ad esempio data di nascita)
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String dataDaComparareString = (String) recordDtoGenerico.getCampo(nomeCampo);//data in ingresso es dataTrasferimentoResidenza
            String nomeCampoValidatore = this.getParametri().getParametriMap().get("nomeCampoValidatore");//nome del campo con cui confrontare la data in ingresso es "datadiNascita"
            String dataValidatoreString = (String) recordDtoGenerico.getCampo(nomeCampoValidatore);// valore del campo dataNascita  cioè la data con cui confrontare quella in ingresso

            if (dataDaComparareString != null /*|| dataValidatoreString != null*/) {
                LocalDate dataDaComparare;
                dataDaComparare = LocalDate.parse(dataDaComparareString, formatter);
                LocalDate dataValidatore = LocalDate.parse(dataValidatoreString, formatter);
                if (dataDaComparare.isAfter(dataValidatore) || dataDaComparare.isEqual(dataValidatore)) {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                } else {
                    listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));

            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Non è possibile validare la regola di data antecedente del campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Non è possibile validare la regola di data antecedente del campo " + nomeCampo);
        } catch (DateTimeParseException dataE) {
            listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }
        return listaEsiti;
    }
}

