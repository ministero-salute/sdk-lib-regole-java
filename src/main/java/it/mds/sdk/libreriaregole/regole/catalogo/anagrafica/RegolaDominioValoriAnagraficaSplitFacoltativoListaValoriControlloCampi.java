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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaSplitFacoltativoListaValoriControlloCampi")
public class RegolaDominioValoriAnagraficaSplitFacoltativoListaValoriControlloCampi extends RegolaGenerica {

    public RegolaDominioValoriAnagraficaSplitFacoltativoListaValoriControlloCampi(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che,se un paramentroCondizionanate è in una listaValoriAmmessi, il valore del campo in input "nomeCampo"
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

            String codiceStrutturaErogante = (String) (recordDtoGenerico.getCampo(nomeCampo));
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            if (codiceStrutturaErogante == null || codiceStrutturaErogante.isEmpty()) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }

            String codAsl = (String) recordDtoGenerico.getCampo("codiceAziendaSanitaria");
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante");
            String valoreParametroCondizionante = (String)recordDtoGenerico.getCampo(parametroCondizionante);
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if (!listaValori.contains(valoreParametroCondizionante)) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }

            String annoDiRiferimento = (String) recordDtoGenerico.getCampo("annoConsegna");
            String meseDiRiferimento = (String) recordDtoGenerico.getCampo("meseConsegna");
            LocalDateTime annoMeseDiRiferimento = getLocalDateTimeFromMeseAnnoRiferimento(annoDiRiferimento, meseDiRiferimento);

            if ("02".equals(valoreParametroCondizionante)) {
                //concatenazione dei 3 campi
                String valoreDaValidareConcatenato = valoreParametroCondizionante + "#" + codAsl + "#" + codiceStrutturaErogante;
                return validaCampiSplittatiInAnagrafica(nomeCampo, nomeTabella, valoreDaValidareConcatenato, annoMeseDiRiferimento);

            }
            // nel caso di tipo_str="01"
            // se cod_str termina con "00" e ha in totale 8 caratteri
            // allora bisogna eliminare i due caratteri finali, dunque confrontare il dato dell'anagrafica con i primi sei caratteri di cod_str.
            if ("01".equals(valoreParametroCondizionante) && codiceStrutturaErogante.length() == 8 && Objects.equals(codiceStrutturaErogante.substring(6), "00")) {

                    String valoreDaValidareConcatenato = valoreParametroCondizionante + "#" + codiceStrutturaErogante.substring(0, 6);
                    return validaCampiSplittatiInAnagrafica(nomeCampo, nomeTabella, valoreDaValidareConcatenato, annoMeseDiRiferimento);
            }
            //concatenazione dei 2 campi
            String valoreDaValidareConcatenato = valoreParametroCondizionante + "#" + codiceStrutturaErogante;
            return validaCampiSplittatiInAnagrafica(nomeCampo, nomeTabella, valoreDaValidareConcatenato, annoMeseDiRiferimento);

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ", this.getClass().getName(), nomeCampo, recordDtoGenerico);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }

    }

    public LocalDateTime getLocalDateTimeFromMeseAnnoRiferimento(String annoDiRiferimento, String meseDiRiferimento) {
        return LocalDateTime.of(LocalDate.of(Integer.parseInt(annoDiRiferimento), Month.of(Integer.parseInt(meseDiRiferimento)), 1), LocalTime.of(00, 00, 00));
    }

    private List<Esito> validaCampiSplittatiInAnagrafica(String nomeCampo,  String nomeTabella, String valoreDaValidareConcatenato, LocalDateTime annoMeseDiRiferimento) throws MalformedRegistryException, RegistryNotFoundException
    {
        GestoreAnagrafica gestoreAnagrafica = getGestoreAnagrafica();
        List<RecordAnagrafica> valoriTabella = gestoreAnagrafica.richiediAnagrafica(nomeTabella, valoreDaValidareConcatenato, false).getRecordsAnagrafica();

        //recupero il dominio dei valori validi
        List<String> listaDominio;
        log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
        listaDominio = valoriTabella
                .stream()
                .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(annoMeseDiRiferimento) * annoMeseDiRiferimento.compareTo(ra.getValidoA())) >= 0)
                        || (ra.getValidoDa() == null && ra.getValidoA() == null) || ((ra.getValidoDa() != null && ra.getValidoA() == null) && (ra.getValidoDa().compareTo(annoMeseDiRiferimento) <= 0)))
                .map(RecordAnagrafica::getDato)
                .collect(Collectors.toList());
        log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

        if (!listaDominio.contains(valoreDaValidareConcatenato)) {
            return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
        }
        return List.of(creaEsitoOk(nomeCampo));
    }

    public GestoreAnagrafica getGestoreAnagrafica() {
        return new GestoreAnagrafica();
    }
}
