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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioCampoDiversoValoreAnagraficaSplitConParametro")
public class RegolaDominioCampoDiversoValoreAnagraficaSplitConParametro extends RegolaGenerica {

    public RegolaDominioCampoDiversoValoreAnagraficaSplitConParametro(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se un campo dto è diverso da un parametro e un altro campo non è contenuto nella lista Anagrafiche vai in ko
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

            String valoreCondizionante = (String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCondizionante")
            );

            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante");

            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            String separatore = this.getParametri().getParametriMap().get("separatore");

            List<String> listaCampiDaConcatenare = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaCampiDaConcatenare").split("\\|")).collect(Collectors.toList());

            String campiDtoConcatenati = "";

            //per ogni campo recupero il suo valore e lo concateno con il separatore
            StringJoiner joiner = new StringJoiner(separatore);
            for (String campoDaConcatenare : listaCampiDaConcatenare) {
                joiner.add((String) recordDtoGenerico.getCampo(campoDaConcatenare));
                campiDtoConcatenati = joiner.toString();
            }

            var listaDominio = richiediAnagrafica(nomeTabella, campiDtoConcatenati, recordDtoGenerico);

            if (!parametroCondizionante.equals(valoreCondizionante) && listaDominio.size() == 0 ) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }

    private List<String> richiediAnagrafica(String nomeTabella, String campiConcatenati, RecordDtoGenerico recordDtoGenerico) throws MalformedRegistryException, RegistryNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
        List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella, campiConcatenati, false).getRecordsAnagrafica();
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
}
