/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaTutoriSoggettiAffidatari")
public class RegolaObbligatorietaTutoriSoggettiAffidatari extends RegolaGenerica {

    private RecordDtoGenerico dto;
    private final String TUTORE_SOGGETTI_AFFIDATARI = "TUTORE_SOGGETTI_AFFIDATARI";
    private final String GENITORE = "GENITORE";

    public RegolaObbligatorietaTutoriSoggettiAffidatari(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Se è maggiorenne, controllo data di nascita controllo che non ci deve essere donatore.
     * <p>
     * Se donatore è minorenne e disponente 1 è nullo vado KO.
     * <p>
     * - Se non è genitore nè tutore ed è presente il campo minorenne:
     * - deve essere valorizzato disponente 1
     * -  data nascita disponente deve essere maggiorenne
     * <p>
     * - Se non è genitore ma è tutore ed è presente campo minorenne:
     * - disponente 1 deve essere valorizzato.
     * - data nascita minore deve essere minorenne.
     * <p>
     * - Se è genitore ed è presente campo minorenne:
     * - disponente 1 deve essere valorizzato.
     * - controllo data nascita minore deve essere minorenne.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {

            dto = recordDtoGenerico;

            String tipoDisponente = (String) recordDtoGenerico.getCampo(nomeCampo);
            String donatoreMinorenne = (String) recordDtoGenerico.getCampo("donatoreMinorenne");
            String donatoreDataNascita = (String) recordDtoGenerico.getCampo("donatoreDataNascita");
            String disponenti1DataNascita = (String) recordDtoGenerico.getCampo("disponenti1DataNascita");
            String disponenti2DataNascita = (String) recordDtoGenerico.getCampo("disponenti2DataNascita");

            List<String> listaCampiDonatore = Arrays.stream(this.getParametri().getParametriMap().get("listaCampiDonatore").split("\\|")).collect(Collectors.toList());
            List<String> listaCampiDisponenti2 = Arrays.stream(this.getParametri().getParametriMap().get("listaCampiDisponenti2").split("\\|")).collect(Collectors.toList());

            // Se è maggiorenne, controllo data di nascita (deve essere maggiorenne), controllo che non ci deve essere donatore.
            if ("no".equalsIgnoreCase(donatoreMinorenne)) {
                // se disponenti 1 è minorenne o donatore è pieno -> ko
                if (Period.between(LocalDate.parse(disponenti1DataNascita), LocalDate.now()).getYears() < 18 ||
                        isDisponenteDonatoreFiduciarioNonNull(listaCampiDonatore)) {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
                // se è genitore o tutore -> ko perchè maggiorenne
                if (TUTORE_SOGGETTI_AFFIDATARI.equals(tipoDisponente) || GENITORE.equals(tipoDisponente)) {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
                // se dataDisponenti1 è minore 18 -> ko perchè disponenti1 è egli stesso il donatore maggiorenne.
                // se disponenti2 è non null -> ko perchè disponenti1 cioè il donatore è maggiorenne.
                if (Period.between(LocalDate.parse(disponenti1DataNascita), LocalDate.now()).getYears() < 18 ||
                        isDisponenteDonatoreFiduciarioNonNull(listaCampiDisponenti2)
                ) {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }

            } else { // se minorenne
                // disponente 1 è sempre valorizzato, rimuovo if di seguito.
                // se non è genitore nè tutore
                if (!TUTORE_SOGGETTI_AFFIDATARI.equals(tipoDisponente) && !GENITORE.equals(tipoDisponente)) {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
                // se tutore
                else if (TUTORE_SOGGETTI_AFFIDATARI.equals(tipoDisponente)) {
                    if(isDisponenteDonatoreFiduciarioNonNull(listaCampiDisponenti2)){
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                    if (Period.between(LocalDate.parse(donatoreDataNascita), LocalDate.now()).getYears() > 17) {
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                    if (Period.between(LocalDate.parse(disponenti1DataNascita), LocalDate.now()).getYears() < 18) {
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                }
                // se genitore
                else {
                    if (Period.between(LocalDate.parse(donatoreDataNascita), LocalDate.now()).getYears() > 17) {
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                    if (Period.between(LocalDate.parse(disponenti1DataNascita), LocalDate.now()).getYears() < 18) {
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                    if(isDisponenteDonatoreFiduciarioNonNull(listaCampiDisponenti2) &&
                            Period.between(LocalDate.parse(disponenti2DataNascita), LocalDate.now()).getYears() < 18)
                    {
                        return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                    }
                }
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola per il campo " + nomeCampo);
        }catch(NullPointerException | DateTimeParseException x){
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), x);
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }
    }

    private boolean isDisponenteDonatoreFiduciarioNonNull(List<String> listaCampi) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for (String campo : listaCampi) {
            if (dto.getCampo(campo) != null) {
                return true;
            }
        }
        return false;
    }

}
