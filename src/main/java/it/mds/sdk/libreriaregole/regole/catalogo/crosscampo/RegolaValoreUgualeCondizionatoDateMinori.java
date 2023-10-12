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
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaValoreUgualeCondizionatoDateMinori")
public class RegolaValoreUgualeCondizionatoDateMinori extends RegolaGenerica {


    public RegolaValoreUgualeCondizionatoDateMinori(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se SAMPIDONEO =Y,
     * verificare che SAMP_DATE <= SAMPACC_DATE <= ANALYSIS_START_DATE <= ANALYSIS_END_DATE <= RAPPROVA_DATE <= DATA ESECUZIONE SDK
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            //sampIdoneo
            String valoreCampoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);

            // sampDate
            LocalDate valoreCampoCondizionante = LocalDate.parse((String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante"))
            );

            //sampAccDate
            LocalDate valoreCampoCondizionante2 = LocalDate.parse((String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante2")
            ));

            //analysisStartDate Fac.
            String campoCondizionante3 = (String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante3")
            );
            //analysisEndDate Fac.
            String campoCondizionante4 = (String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante4")
            );
            //rapprovaDate Fac.
            String campoCondizionante5 = (String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante5")
            );

            //data esecuzione sdk
            LocalDate dataEsecuzioneSDK = LocalDate.now();

            // Y
            String parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");

            if (!valoreCampoDaValidare.equals(parametroCampoCondizionante) ||
                    campoCondizionante3 == null ||
                    campoCondizionante4 == null ||
                    campoCondizionante5 == null ) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            LocalDate valoreCampoCondizionante3 = LocalDate.parse(campoCondizionante3);
            LocalDate valoreCampoCondizionante4 = LocalDate.parse(campoCondizionante4);
            LocalDate valoreCampoCondizionante5 =  LocalDate.parse(campoCondizionante5);

            if (!valoreCampoCondizionante.isAfter(valoreCampoCondizionante2) &&
                    !valoreCampoCondizionante2.isAfter(valoreCampoCondizionante3) &&
                    !valoreCampoCondizionante3.isAfter(valoreCampoCondizionante4) &&
                    !valoreCampoCondizionante4.isAfter(valoreCampoCondizionante5) &&
                    !valoreCampoCondizionante5.isAfter(dataEsecuzioneSDK)
            ) {
                return List.of(creaEsitoOk(nomeCampo));
            } else {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }


        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola Lista valori ammessi condizionata per il campo " + nomeCampo);
        }
    }

}


