package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

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
@XmlDiscriminatorValue("regolaBR1990PerFlussiTReMV")
public class RegolaBR1990PerFlussiTReMV extends RegolaGenerica{

	public RegolaBR1990PerFlussiTReMV(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 *
	 * Il codice è valorizzato e diverso da 999 e la modalità è uguale a TR
	 * e la regione di residenza è uguale dal codice regione che sta trasmettendo il dato
	 * oppure la modalità è uguale a MV e la regione di residenza è uguale al codice regione
	 * che sta trasmettendo il dato
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		try {

			String codRegioneResidenza = (String)recordDtoGenerico.getCampo(nomeCampo);
			String modalita = (String) recordDtoGenerico.getCampo("modalita");


			if((Objects.nonNull(codRegioneResidenza) &&
				!Objects.equals("999", codRegioneResidenza) &&
					Objects.equals("TR", modalita) &&
					Objects.equals(codRegioneResidenza, recordDtoGenerico.getCampiInput().getCodiceRegioneInput())) ||
					(Objects.equals("MV", modalita) && Objects.equals(codRegioneResidenza, recordDtoGenerico.getCampiInput().getCodiceRegioneInput()))
			){
				return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
			}

			return List.of(creaEsitoOk(nomeCampo));

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}
	}


}
