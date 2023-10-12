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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCodiceUnitaOperativa")
public class RegolaCodiceUnitaOperativa extends RegolaGenerica {

    public RegolaCodiceUnitaOperativa(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Il cod_un_op può essere uguale a "0000" OR
     * [i primi due caratteri di cod_un_op devono essere presenti in anagraficaCodici Unità Operative
     * AND gli ultimi due caratteri di cod_un_op trasformati in intero devono valere tra 0 e 99]
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
            //cod_un_op
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            if (campoDaValidare == null || "0000".equalsIgnoreCase(campoDaValidare)) {
                return List.of(creaEsitoOk(nomeCampo));
            }

            Integer lunghezzeStringa = campoDaValidare.length();
            String primiDueCaratteri = campoDaValidare.substring(0, 2);
            String ultimiDueCaratteri = campoDaValidare.substring(lunghezzeStringa - 2, lunghezzeStringa);

            // Verifico che gli ultimi due caratteri siano dei numeri
            Pattern pattern = Pattern.compile("[0-9]{2}");
            Matcher matcher = pattern.matcher(ultimiDueCaratteri);

            List<RecordAnagrafica> listaValori = getGestoreAnagrafica().richiediAnagrafica(nomeTabella, primiDueCaratteri, false).getRecordsAnagrafica();
            //recupero il dominio dei valori validi

            log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
            List<String> listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null &&
                            (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0) ||
                            (ra.getValidoDa() == null && ra.getValidoA() == null)
                    )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());
            log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

            if (matcher.matches() && listaDominio.contains(primiDueCaratteri)) {
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
