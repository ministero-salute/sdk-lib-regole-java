/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro")
public class RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro extends RegolaGenerica {

    public RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *
     Se: listaValoriAmmessi contiene il campoCondizionante
        allora la lista dominio v.a. deve contenere il valore campoDaValidare.
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

            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
            String campoConcatenato;


            if(campoDaValidare == null) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            campoConcatenato = campoDaValidare.substring(0, 3) + "#" + campoDaValidare.substring(3, 6);

            String campoCondizionante = (String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("campoCondizionante")
            );

            List<String> listaValoriAmmessi = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            var listaDominio = richiediAnagrafica(nomeTabella, campoConcatenato, recordDtoGenerico);

            if(listaValoriAmmessi.contains(campoCondizionante)&& listaDominio.isEmpty()){
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }

    }


    private List<String> richiediAnagrafica(String nomeTabella, String campiConcatenati, RecordDtoGenerico recordDtoGenerico) throws MalformedRegistryException, RegistryNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        List<RecordAnagrafica> listaValori = getGestoreAnagrafica().richiediAnagrafica(nomeTabella, campiConcatenati, false).getRecordsAnagrafica();
        String modalitaRegola = this.getParametri().getParametriMap().get("modalitaRegola");

        String MODALITA_OPERATIVA_REGOLA_ANNO = "Y";
        String MODALITA_OPERATIVA_REGOLA_DATA = "D";

        if(Objects.equals(modalitaRegola, MODALITA_OPERATIVA_REGOLA_ANNO)){ // Year

            log.debug("modalità operativa regola: " + modalitaRegola);

            int annoRiferimento = Integer.parseInt(recordDtoGenerico.getCampiInput().getAnnoRiferimentoInput());

            return listaValori
                    .stream()
                    .filter(ra->(ra.getValidoDa()!= null && ra.getValidoDa().getYear() <= annoRiferimento &&
                                    ra.getValidoA()!=null &&  ra.getValidoA().getYear() >= annoRiferimento
                            )
                    )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

        } else if(Objects.equals(modalitaRegola, MODALITA_OPERATIVA_REGOLA_DATA)) { // Date

            log.debug("modalità operativa regola: " + modalitaRegola);

            LocalDateTime dataRiferimento = LocalDate.parse(String.valueOf(recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("dataRiferimento")))).atStartOfDay();
            return listaValori
                    .stream()
                    .filter(ra->(ra.getValidoDa()!= null && ra.getValidoDa().isBefore(dataRiferimento) &&
                                    ra.getValidoA()!=null &&  ra.getValidoA().isAfter(dataRiferimento)
                            )
                    )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());
        }

        log.error("modalità operativa regola NON RICONOSCIUTA: " + modalitaRegola);
        throw new ValidazioneImpossibileException();

    }

    public GestoreAnagrafica getGestoreAnagrafica(){
        return new GestoreAnagrafica();
    }


}