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
import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaResType")
public class RegolaObbligatorietaResType extends RegolaGenerica {

    public RegolaObbligatorietaResType(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    @Override
    //se RESTYPE=VAL, allora  RESVAL >= RESLOQ, se RESLOQ è valorizzato (presente e diverso da blanks)
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
        try {
            // resVal
            Double campoDaValidare = (Double) recordDtoGenerico.getCampo(nomeCampo);
            // resLoq
            Double valoreCampoCondizionante = (Double) recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionante")
            );

            //resType
            var valoreCampoCondizionanteDue = recordDtoGenerico.getCampo(
                    this.getParametri().getParametriMap().get("valoreCampoCondizionanteDue")
            );
            // val
            var parametroCampoCondizionante = this.getParametri().getParametriMap().get("parametroCampoCondizionante");

            if (Objects.equals(valoreCampoCondizionanteDue, parametroCampoCondizionante) 
            		&& valoreCampoCondizionante != null && campoDaValidare != null 
            		&& campoDaValidare < valoreCampoCondizionante){
                    return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
                }
            log.debug("{}.valida - valoreCampoCondizionante[{}]  - parametroCampoCondizionante[{}] - Non sono uguali",
                    this.getClass().getName(), valoreCampoCondizionanteDue, parametroCampoCondizionante);

            return List.of(creaEsitoOk(nomeCampo));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico.toString(), e);
            throw new ValidazioneImpossibileException("Non è possibile controllare l'obbligatorietà di " + nomeCampo);
        }
    }
}
