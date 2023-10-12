package it.mds.sdk.libreriaregole.regole.diretta;

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
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaB17Diretta")
public class RegolaB17Diretta extends RegolaGenerica {

    public RegolaB17Diretta(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * se il valore del campo id_ass del tracciato di input è uguale alla stringa TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+
     * Dms+s2mz2P3x90Y= allora il campo tip_id_ass del tracciato di input deve essere valorizzato con 98 and il campo vld_id_ass del tracciato di input deve essere valorizzato con 0
     *
     * @param nomeCampo         il nome del campo da validare
     * @param recordDtoGenerico DTO del record di un flusso
     * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        List<Esito> listaEsiti = new ArrayList<>();
        try {
            //campo su cui applicare la regola
            String identificativoAssistito = (String) recordDtoGenerico.getCampo(nomeCampo);
            String valoreIdentificativoAssistito = "TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+Dms+s2mz2P3x90Y=";

            Integer tipologiaCodIdentAssistito = (Integer) recordDtoGenerico.getCampo("tipologiaCodIdentAssistito");
            Integer validitaCodIdentAssistito = (Integer) recordDtoGenerico.getCampo("validitaCodIdentAssistito");

            if (valoreIdentificativoAssistito.equalsIgnoreCase(identificativoAssistito)) {
                if (tipologiaCodIdentAssistito == 98 && validitaCodIdentAssistito == 0) {
                    listaEsiti.add(creaEsitoOk(nomeCampo));
                } else {
                    listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            } else {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaB17Diretta del campo " + nomeCampo);
        }
        return listaEsiti;
    }
}
