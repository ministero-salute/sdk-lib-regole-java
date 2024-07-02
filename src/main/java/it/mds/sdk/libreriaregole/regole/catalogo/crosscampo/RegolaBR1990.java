/* SPDX-License-Identifier: BSD-3-Clause */

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
@XmlDiscriminatorValue("regola1990")
public class RegolaBR1990 extends RegolaGenerica{

	public RegolaBR1990(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		try {

			String codRegioneResidenza = (String)recordDtoGenerico.getCampo(nomeCampo);
			String modalita = (String) recordDtoGenerico.getCampo("modalita");

			if(Objects.nonNull(codRegioneResidenza) &&
					!Objects.equals("999", codRegioneResidenza) &&
					Objects.equals("RE", modalita) &&
					!Objects.equals(codRegioneResidenza, recordDtoGenerico.getCampiInput().getCodiceRegioneInput())
			){
				log.debug("{} KO: codRegioneResidenza {} - modalita {} - regioneInput {} ",
						this.getClass().getName(),
						codRegioneResidenza,
						modalita,
						recordDtoGenerico.getCampiInput().getCodiceRegioneInput()
				);
				return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
			}

			return List.of(creaEsitoOk(nomeCampo));

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}
	}


}
