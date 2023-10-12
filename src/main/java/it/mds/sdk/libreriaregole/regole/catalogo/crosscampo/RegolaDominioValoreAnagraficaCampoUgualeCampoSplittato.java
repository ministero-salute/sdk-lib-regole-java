package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaCampoUgualeCampoSplittato")
public class RegolaDominioValoreAnagraficaCampoUgualeCampoSplittato extends RegolaGenerica {

    public RegolaDominioValoreAnagraficaCampoUgualeCampoSplittato(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     *
     * Se il valore del campo PROGLEGALREF del tracciato di input composto da tante stringhe
     * concatenate e separate dal carattere $ contiene la stringa N028A allora:
     * - il valore del campo SAMPMATCODE_BULDING (costruito con le regole descritte
     * nel paragrafo 3.4) del tracciato di input deve essere presente (vedi nota 1 paragrafo 3.4)
     * nella colonna TERM_CODE della query Anagrafica BABY_FOOD (paragrafo 1.2)
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
            // SAMPMATCODE_BULDING
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            // PROGLEGALREF
            String campoCondizionante = (String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("campoCondizionante")
            );
            // N028A
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante");

            List<RecordAnagrafica> listaValori = getGestoreAnagrafica().richiediAnagrafica("BABY_FOOD", campoDaValidare, false).getRecordsAnagrafica();
            // listaDominio = list termCode
            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());
            if (!campoCondizionante.contains(parametroCondizionante) || listaDominio.contains(campoDaValidare)){
                return List.of(creaEsitoOk(nomeCampo));
            }
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }

    }

    public GestoreAnagrafica getGestoreAnagrafica() {
        return new GestoreAnagrafica();
    }

}