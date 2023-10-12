package it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaCondizionataDaListaValori")
public class RegolaObbligatorietaCondizionataDaListaValori extends RegolaGenerica {

    public RegolaObbligatorietaCondizionataDaListaValori(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * verifica che il campo in input sia presente(obbligatorio) se un altro campo del DTO assume un certo valore
     * contenuto in una lista di valori ammessi
     *
     * @param nomeCampo il nome del campo da validare
     * @param recordDtoGenerico DTO del record di un flusso
     * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
            String campoCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCondizionante"));
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if(listaValori.contains(campoCondizionante) && campoDaValidare==null) {
                listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
            }else{
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException  e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare regolaObbligatorietaCondizionataDaListaValori del campo " + nomeCampo );
        }
        return listaEsiti;
    }
}
