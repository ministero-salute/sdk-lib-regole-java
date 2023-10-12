package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaSampMatCodePackMat")
public class RegolaDominioValoreAnagraficaSampMatCodePackMat extends RegolaGenerica {

    public RegolaDominioValoreAnagraficaSampMatCodePackMat(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Controlla che il valore del campo in input "nomeCampo"  sia obbligatorio
     * se la lista dei Master#termCode estratta dalla tabella Anagrafica PARAM PARENT nel campo termcode contiene paramCode
     * e se tale record nella parte Master contiene uno dei valori RF
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso.
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            String paramCode = (String) recordDtoGenerico.getCampo("paramcode");

            if(Objects.isNull(paramCode)){
                log.trace("VALIDAZIONE OK: {}.valida() - il campo paramcode is null", this.getClass().getName());
                return List.of(creaEsitoOk(nomeCampo));
            }

            String sampMatCodePackMat = (String)recordDtoGenerico.getCampo(nomeCampo);

            List<String> RF_PARAM_LIST = Arrays.stream(this.getParametri().getParametriMap().get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
            List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(
                    "ANAG_SALM_PARAM_PARENT", false
            ).getRecordsAnagrafica();

            // listaDominio = list Master#termCode
            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa() == null && ra.getValidoA() == null))
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());


            Stream<String> rigaConTermCodeCercato = listaDominio.stream().filter(el -> el.split("#")[1].contains(paramCode));

            var x = rigaConTermCodeCercato.findFirst();

            if (x.isPresent() && RF_PARAM_LIST.contains(x.get().split("#")[0]) && sampMatCodePackMat == null) {
                    return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }

}
