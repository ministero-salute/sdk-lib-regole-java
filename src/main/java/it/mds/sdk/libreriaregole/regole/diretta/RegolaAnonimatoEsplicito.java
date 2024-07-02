/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.util.Collections;
import java.util.List;


@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaAnonimatoEsplicito")
public class RegolaAnonimatoEsplicito extends RegolaGenerica {

    public RegolaAnonimatoEsplicito(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    private final String ID_ANON_ESPLICITO="TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+Dms+s2mz2P3x90Y=";

    /**
     * Verifica se il record rispetta anonimato esplicito
     * Diremo che il record rispetta l'anonimato esplicito se:
     *  id_ass =TGIN+o+fB0m+hf4iyd2fYF6kjRqEpa6+TPiiNfAvE0M=9wmFXLxmSeuWHV0HIMjYtoyCAOT+Dms+s2mz2P3x90Y=
     *
     * @param nomeCampo
     * @param recordDtoGenerico
     * @return
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            String id_ass = (String) recordDtoGenerico.getCampo("identificativoAssistito");

            if(id_ass == null || id_ass.isEmpty()) {
                return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }


                if (ID_ANON_ESPLICITO.equals(id_ass)) {
                    return Collections.singletonList(creaEsitoOk(nomeCampo));
                } else {
                    return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }



        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaAnonimatoEsplicito ");
        }
    }

}
