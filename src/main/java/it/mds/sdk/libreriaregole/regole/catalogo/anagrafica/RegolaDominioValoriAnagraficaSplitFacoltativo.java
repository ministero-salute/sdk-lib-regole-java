package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaSplitFacoltativo")
public class RegolaDominioValoriAnagraficaSplitFacoltativo extends RegolaGenerica {

    public RegolaDominioValoriAnagraficaSplitFacoltativo(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo in input "nomeCampo"
     * sia contenuto in un dominio di valori di anagrafica splittato per #
     * E' PASSATO UN PARAMETRO parametroSplitter CHE INDICA QUALE PARTE DELLO SPLIT CONFRONTARE
     * CON IL VALORE IN INGRESSO
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
            String campoDaValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
            String campoDaConcatenare = String.valueOf(recordDtoGenerico.getCampo("codiceRegioneDomicilio"));
            String valoreConcatenato = campoDaConcatenare + "#" + campoDaValidare;

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
            List<RecordAnagrafica> listaValori =
                    gestoreAnagrafica.richiediAnagrafica(nomeTabella, valoreConcatenato, false).getRecordsAnagrafica();
            //recupero il dominio dei valori validi
            List<String> listaDominio;
            log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
            listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null) || ((ra.getValidoDa() != null && ra.getValidoA() == null) && (ra.getValidoDa().compareTo(LocalDateTime.now()) <= 0)))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());
            log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());
            if (recordDtoGenerico.getCampo(nomeCampo) == null || listaDominio.contains(valoreConcatenato)) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ", this.getClass().getName(), nomeCampo, recordDtoGenerico);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }

    }


}
