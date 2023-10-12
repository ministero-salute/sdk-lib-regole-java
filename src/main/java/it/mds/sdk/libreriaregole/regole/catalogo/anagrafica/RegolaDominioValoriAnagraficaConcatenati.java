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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaConcatenati")
public class RegolaDominioValoriAnagraficaConcatenati extends RegolaGenerica {

    private static final String IMPOSSIBILE_VALIDARE_LA_REGOLA_DOMINIO_VALORI_ANAGRAFICA = "Impossibile validare la regola dominio valori anagrafica per il campo ";

	public RegolaDominioValoriAnagraficaConcatenati(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che i valori
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        try {
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            String separatore = this.getParametri().getParametriMap().get("separatore");

            List<String> listaCampiDaConcatenare = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaCampiDaConcatenare").split("\\|")).collect(Collectors.toList());

            String campiDtoConcatenati = "";

            if(Objects.isNull(separatore) || Objects.isNull(nomeTabella)){
                log.error("separatore in input o nome tabella nullo.");
                throw new ValidazioneImpossibileException(IMPOSSIBILE_VALIDARE_LA_REGOLA_DOMINIO_VALORI_ANAGRAFICA + nomeCampo);
            }

            //per ogni campo recupero il suo valore e lo concateno con il separatore
            StringJoiner joiner = new StringJoiner(separatore);
            for (String campoDaConcatenare : listaCampiDaConcatenare) {
                joiner.add((String) recordDtoGenerico.getCampo(campoDaConcatenare));
                campiDtoConcatenati = joiner.toString();
            }

            List<RecordAnagrafica> listaValori = getGestoreAnagrafica().richiediAnagrafica(nomeTabella, campiDtoConcatenati, false).getRecordsAnagrafica();
            //recupero il dominio dei valori validi
            List<String> listaDominio;

            listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            if ( listaDominio.isEmpty() ) {
                return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error(IMPOSSIBILE_VALIDARE_LA_REGOLA_DOMINIO_VALORI_ANAGRAFICA + nomeCampo, e);
            throw new ValidazioneImpossibileException(IMPOSSIBILE_VALIDARE_LA_REGOLA_DOMINIO_VALORI_ANAGRAFICA + nomeCampo);
        }

    }

    public GestoreAnagrafica getGestoreAnagrafica() {
        return new GestoreAnagrafica();
    }

}
