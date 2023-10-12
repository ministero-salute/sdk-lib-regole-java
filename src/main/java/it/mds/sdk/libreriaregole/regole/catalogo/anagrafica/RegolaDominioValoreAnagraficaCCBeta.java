package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaDominioValoreAnagraficaCCBeta")
public class RegolaDominioValoreAnagraficaCCBeta extends RegolaGenerica {

    public RegolaDominioValoreAnagraficaCCBeta(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }


    /**
     * Controlla che il valore del campo in input "nomeCampo"  sia obbligatorio
     * se progId=PNR e se anMethType=AT06A e se paramCode = farmaciVeterinari (param non è nè contaminante nè pesticida
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */
    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        try {
            String progId = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("progId"));
            String paramCode = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("paramCode"));
            String anmethtype = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("anmethtype"));

            String paramPNR = this.getParametri().getParametriMap().get("paramPNR");
            String paramAT06A = this.getParametri().getParametriMap().get("AT06A");
            String paramPesticida = this.getParametri().getParametriMap().get("paramPesticida");
            String paramContaminante = this.getParametri().getParametriMap().get("paramContaminante");

            String ccBeta = (String)recordDtoGenerico.getCampo(nomeCampo);

            if( progId.equals(paramPNR) && anmethtype.equals(paramAT06A) &&
                    !paramCode.equals(paramPesticida) && !paramCode.equals(paramContaminante) && ccBeta == null
            ){
                    return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
            }
            return List.of(creaEsitoOk(nomeCampo));


        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }

    }
}
