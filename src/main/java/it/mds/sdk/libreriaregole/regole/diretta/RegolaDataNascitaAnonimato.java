package it.mds.sdk.libreriaregole.regole.diretta;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDataNascitaAnonimato")
public class RegolaDataNascitaAnonimato extends RegolaGenerica {

    private final String DATA_ANONIMATO = "9999-12-31";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public RegolaDataNascitaAnonimato(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *La data nasciat puo essere 31/12/9999 solo se il record rispetta anonimato
     * altrimenti deve stare in un range con la data di erogazione
     * @param nomeCampo
     * @param recordDtoGenerico
     * @return
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            String valoreCampo = (String) recordDtoGenerico.getCampo(nomeCampo);
            String valoreCampoCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("nomeCampoCondizionante"));

            int valoreMinimo = Integer.parseInt(this.getParametri().getParametriMap().get("valoreMinimo"));
            int valoreMassimo = Integer.parseInt(this.getParametri().getParametriMap().get("valoreMassimo"));

            RegolaAnonimatoImplicito regolaAnonimatoImplicito = new RegolaAnonimatoImplicito("", "", "", new Parametri());
            List<Esito> esiti = regolaAnonimatoImplicito.valida(nomeCampo, recordDtoGenerico);
            boolean esitoAnonimatoImplicito = esiti.get(0).isValoreEsito();

            RegolaAnonimatoEsplicito regolaAnonimatoEsplicito = new RegolaAnonimatoEsplicito("", "", "", new Parametri());
            List<Esito> esitiEs = regolaAnonimatoEsplicito.valida(nomeCampo, recordDtoGenerico);
            boolean esitoAnonimatoEsplicito = esitiEs.get(0).isValoreEsito();

            log.debug("esitoAnonimatoImplicito: {}, esitoAnonimatoEsplicito: {}, dataNascita :{} ",esitoAnonimatoImplicito,esitoAnonimatoEsplicito,valoreCampo);

            if(valoreCampo == null){
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }
            if (DATA_ANONIMATO.equals(valoreCampo)) {
                if(esitoAnonimatoImplicito || esitoAnonimatoEsplicito){
                    return Collections.singletonList(creaEsitoOk(nomeCampo));
                }else{
                    return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            }
            if(!DATA_ANONIMATO.equals(valoreCampo) || (!esitoAnonimatoImplicito && !esitoAnonimatoEsplicito)){ //NOSONAR
            //   0<=dataerog anno - data nascita anno <=150
                LocalDate  dataEr = LocalDate.parse(valoreCampoCondizionante, formatter);
                LocalDate dataNascita = LocalDate.parse(valoreCampo,formatter);
                int differenzaAnno = dataEr.getYear() - dataNascita.getYear();
                if (differenzaAnno>=valoreMinimo &&  differenzaAnno<= valoreMassimo){
                    return Collections.singletonList(creaEsitoOk(nomeCampo));
                }else{
                    return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            }
            return Collections.singletonList(creaEsitoOk(nomeCampo));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaDataNascitaAnonimato per il campo " + nomeCampo);

        } catch (DateTimeParseException dataE) {
            return Collections.singletonList(creaEsitoKO(nomeCampo, "999", "Errore formatazione date"));
        }

    }
}
