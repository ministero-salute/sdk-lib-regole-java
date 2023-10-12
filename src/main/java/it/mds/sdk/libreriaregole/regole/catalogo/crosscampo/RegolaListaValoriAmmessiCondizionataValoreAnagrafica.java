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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaListaValoriAmmessiCondizionataValoreAnagrafica")
public class RegolaListaValoriAmmessiCondizionataValoreAnagrafica extends RegolaGenerica {

    public RegolaListaValoriAmmessiCondizionataValoreAnagrafica(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Se: listaValoriAmmessi contiene il campoCondizionante
     * allora la lista dominio v.a. deve contenere il valore campoDaValidare.
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
            String campoConcatenato = null;

            if (campoDaValidare == null) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            String primaParte = campoDaValidare.substring(0, 3);
            String secondaParte = campoDaValidare.substring(3, 6);
            campoConcatenato = primaParte + "#" + secondaParte;


            String campoCondizionante = (String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("campoCondizionante")
            );

            List<String> listaValoriAmmessi = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            List<RecordAnagrafica> listaValori = getGestoreAnagrafica().richiediAnagrafica(nomeTabella, campoConcatenato, false).getRecordsAnagrafica();

            // listaDominio = list termCode
            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            if (listaValoriAmmessi.contains(campoCondizionante) && !listaDominio.contains(campoConcatenato)) {
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

    public GestoreAnagrafica getGestoreAnagrafica() {
        return new GestoreAnagrafica();
    }


}