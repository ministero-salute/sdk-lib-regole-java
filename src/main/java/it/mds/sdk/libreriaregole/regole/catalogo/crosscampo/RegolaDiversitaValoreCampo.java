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
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDiversitaValoreCampo")
public class RegolaDiversitaValoreCampo extends RegolaGenerica {

    public RegolaDiversitaValoreCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Se confrontando il campoDaValidare con un parametro non sono uguali -> ko
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
            String valoreCampoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String parametroValidatore = this.getParametri().getParametriMap().get("parametroValidatore");
            String nomeCampoConfronto = this.getParametri().getParametriMap().get("nomeCampoCoerente");


            if (Objects.equals(valoreCampoDaValidare, parametroValidatore)) {
                String valoreCampoDipendente = String.valueOf(recordDtoGenerico.getCampo(nomeCampoConfronto));
                List<String> listaValoriConfronto = Arrays.stream(this.getParametri().getParametriMap().get("listaValoriIncoerenti")
                        .split("\\|")).collect(Collectors.toList());
                if (!listaValoriConfronto.contains(valoreCampoDipendente)) {
                    return List.of(creaEsitoOk(nomeCampo));
                } else {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}]", this.getClass().getName(), nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola diversitaValoreCrossCampo per il campo " + nomeCampo);
        }
    }

}
