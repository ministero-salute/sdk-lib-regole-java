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
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoriAnagraficaCampoUgualeListaValori")
public class RegolaDominioValoriAnagraficaCampoUgualeListaValori extends RegolaGenerica {

    public RegolaDominioValoriAnagraficaCampoUgualeListaValori(
            String nome, String codErrore, String desErrore, Parametri parametri
    ) {
        super(nome, codErrore, desErrore, parametri);
    }
    /**
     Se:
     -	il valore del campo PROGID del tracciato di input è uguale alla stringa PSD and
     -	il valore del campo PROGTYPE del tracciato di input è uguale alle stringhe:

     o	K009A or
     o	K018A
     Allora:
     -	i valori dei campi SAMPMATCODE_BUILDING (costruito con le regole descritte nel paragrafo 3.4),
     PROGLEGALREF e SAMPSTRATEGY del tracciato di input devono essere presenti
     (vedi nota 1 paragrafo 3.4 per la corrispondenza del campo SAMPMATCODE_BUILDING) sullo stesso record
     e rispettivamente sulle colonne FOODEX2, PROGLEGALREF e SAMPSTRATEGY
     della query Anagrafica FOODEX_K018A_K019A (paragrafo 1.32)

     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            //sampMatCodeBuilding
            String sampMatCodeBuilding = getSampMatCodeBuilingFromDTO(recordDtoGenerico, nomeCampo);
            //progId
            String valoreCondizionante = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("valoreCondizionante"));
            //progType
            String valoreCondizionante2 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("valoreCondizionante2"));
            //progLegalRef
            String valoreCondizionante3 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("valoreCondizionante3"));
            //sampStrategy
            String valoreCondizionante4 = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("valoreCondizionante4"));

            //PNR
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante");

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

            List<String> listaValoriAmmessi = Arrays.stream(this.getParametri().getParametriMap().
                    get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if(parametroCondizionante.equals(valoreCondizionante) &&
                    listaValoriAmmessi.contains(valoreCondizionante2) &&
                    !this.isCampiDaValidarePresentiNelloStessoRecordDellaQuery(
                            sampMatCodeBuilding, valoreCondizionante3, valoreCondizionante4, listaDominio
                    )
            ){
                return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }
    }

    private boolean isCampiDaValidarePresentiNelloStessoRecordDellaQuery(
            String sampMatCodeBuilding,
            String valoreCondizionante3,
            String valoreCondizionante4,
            List<String> lista
    ) {
        log.debug("{}.valida - sampMatCodeBuilding[{}] - valoreCondizionante3[{}] - valoreCondizionante4[{}] - lista[{}]- BEGIN",
                this.getClass().getName(), sampMatCodeBuilding, valoreCondizionante3, valoreCondizionante4, String.join("|", lista));

        var element = lista.stream().filter(e -> e.matches(sampMatCodeBuilding)).findFirst();
        if(element.isPresent() &&
                element.get().matches(valoreCondizionante3) &&
                element.get().matches(valoreCondizionante4)
        ){
            return true;
        }
        return false;
    }

    private String getSampMatCodeBuilingFromDTO(RecordDtoGenerico dto, String nomeCampo) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.debug("{}.getSampMatCodeBuilingFromDTO  - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), dto.toString());

        String partialSampMatCodeBuilding = dto.getCampo(nomeCampo) != null ?
                (String) dto.getCampo(nomeCampo) : "";
        String f33 = faceletGenerator("f33", dto.getCampo("sampmatcodeLegis"));
        String f32 = faceletGenerator("f32", dto.getCampo("sampmatcodeGender"));
        String f31 = faceletGenerator("f31", dto.getCampo("sampmatcodeAnimalAgeClass"));
        String f30 = faceletGenerator("f30", dto.getCampo("sampmatcodeReproductiveLevel"));
        String f28 = faceletGenerator("f28", dto.getCampo("sampmatcodeProcess"));
        String f27 = faceletGenerator("f27", dto.getCampo("sampmatcodeSourceComodities"));
        String f26 = faceletGenerator("f26", dto.getCampo("sampmatcodeGen"));
        String f24 = faceletGenerator("f24", dto.getCampo("sampmatcodeUse"));
        String f23 = faceletGenerator("f23", dto.getCampo("sampmatcodeTargetConsumer"));
        String f22 = faceletGenerator("f22", dto.getCampo("sampmatcodePlace"));
        String f21 = faceletGenerator("f21", dto.getCampo("sampmatcodeProductionMethod"));
        String f20 = faceletGenerator("f20", dto.getCampo("sampmatcodePartConsumed"));
        String f19 = faceletGenerator("f19", dto.getCampo("sampmatcodePackmat"));
        String f18 = faceletGenerator("f18", dto.getCampo("sampmatcodePackFormat"));
        String f17 = faceletGenerator("f17", dto.getCampo("sampmatcodeCookext"));
        String f10 = faceletGenerator("f10", dto.getCampo("sampmatcodeQualitativeInfo"));
        String f04 = faceletGenerator("f04", dto.getCampo("sampmatcodeIngred"));
        String f02 = faceletGenerator("f02", dto.getCampo("sampmatcodePartNature"));
        String f01 = faceletGenerator("f01", dto.getCampo("sampmatcodeSource"));

        var out = partialSampMatCodeBuilding + "#" + f33 + f32 + f31 + f30 + f28
                + f27 + f26 + f24 + f23 + f22 + f21 + f20 +
                f19 + f18 + f17 + f10 + f04 + f02 + f01;

        // Rimuoviamo l'ultima occorrenza di $, che altrimenti sarebbe l'ultimo char e porterebbe in KO la validazione.
        int i = out.lastIndexOf("$");
        return out.substring(0, i) + out.substring(i + 1);

    }

    private String faceletGenerator(String identificator, Object sampMatCode) {

        log.debug("{}.faceletGenerator  - identificator[{}] - sampMatCode[{}] - BEGIN",
                this.getClass().getName(), identificator, sampMatCode);

        if (sampMatCode == null) {
            return "";
        }
        return identificator.toUpperCase(Locale.ROOT) + "." + sampMatCode + "$";
    }

}
