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
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioCampoObbligatorioValoreAnagrafica")
public class RegolaDominioCampoObbligatorioValoreAnagrafica extends RegolaGenerica {

    public RegolaDominioCampoObbligatorioValoreAnagrafica(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }
    /**
     -	se il campo EVALINFO_SAMPANASSES è presente
     allora:
     -	i due campi EVALCODE e EVALINFO_SAMPANASSES del tracciato di input
     devono essere presenti all’interno dello stesso record e rispettivamente sulle colonne EVALCODE e SAMPTKASSESS
     della query Anagrafica REL_EVALCODE_SMPASSESS (paragrafo 1.26)
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            //EVALINFO_SAMPANASSES
            String campoDaValidare = (String)recordDtoGenerico.getCampo(nomeCampo);

            //EVALCODE
            String valoreCondizionante = (String) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCondizionante")
            );

            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            GestoreAnagrafica gestoreAnagrafica = new GestoreAnagrafica();
            List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella,false).getRecordsAnagrafica();

            // listaDominio = list code
            var listaDominio = listaValori
                    .stream()
                    .filter(ra -> (ra.getValidoDa()!= null && ra.getValidoA()!=null &&
                            (ra.getValidoDa().compareTo(LocalDateTime.now()) * LocalDateTime.now().compareTo(ra.getValidoA())) >= 0)
                            || (ra.getValidoDa()==null && ra.getValidoA()==null)
                    )
                    .map(RecordAnagrafica::getDato)
                    .collect(Collectors.toList());

            if(campoDaValidare != null){
               var element = listaDominio.stream().filter(e->e.split("#")[0].equals(valoreCondizionante)).findFirst();
               if(element.isPresent() && element.get().split("#")[1].equals(campoDaValidare)){
                       return List.of(creaEsitoOk(nomeCampo));
               }
                return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }
}
