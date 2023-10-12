package it.mds.sdk.libreriaregole.regole.diretta;

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
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaB42")
public class RegolaB42 extends RegolaGenerica {

    public RegolaB42(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che i valori siano in anagrafica se data validita compresa nel periodo-anno del DTO e che codice regione non finisca con 000
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

            String valoreCampoInput = (String) recordDtoGenerico.getCampo(nomeCampo);
            if (valoreCampoInput == null || valoreCampoInput.isEmpty()) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");
            String separatore = this.getParametri().getParametriMap().get("separatore");

            String mese = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("mese"));
            String anno = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("anno"));

            if (Integer.parseInt(mese) > 12 || Integer.parseInt(mese) < 1) {
                return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

            List<String> listaCampiDaConcatenare = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaCampiDaConcatenare").split("\\|")).collect(Collectors.toList());

            String campiDtoConcatenati = "";

            //per ogni campo recupero il suo valore e lo concateno con il separatore
            StringJoiner joiner = new StringJoiner(separatore);
            for (String campoDaConcatenare : listaCampiDaConcatenare) {
                joiner.add((String) recordDtoGenerico.getCampo(campoDaConcatenare));
                campiDtoConcatenati = joiner.toString();
            }

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
            List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella, false).getRecordsAnagrafica();
            LocalDateTime dataConfrontoAnagrafica = LocalDateTime.of(LocalDate.of(Integer.parseInt(anno), Month.of(Integer.parseInt(mese)), 1), LocalTime.of(00, 00, 00));
            //recupero il dominio dei valori validi
            List<String> listaDominio;

            listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(dataConfrontoAnagrafica) * dataConfrontoAnagrafica.compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            String valoreCampo3caratteri = valoreCampoInput.substring(4, 6);

            if (listaDominio.contains(campiDtoConcatenati) && !"000".equals(valoreCampo3caratteri)) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            } else {
                return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));

            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("Impossibile validare la regolaB42 per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regolaB42 per il campo " + nomeCampo);
        }

    }


}
