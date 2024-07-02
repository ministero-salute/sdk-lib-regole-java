/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import it.mds.sdk.libreriaregole.regole.catalogo.anagrafica.RegolaDominioValoriAnagraficaConcatenati;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaIncoerenzaComuneRegioneASL")
public class RegolaIncoerenzaComuneRegioneASL extends RegolaDominioValoriAnagraficaConcatenati {

	public RegolaIncoerenzaComuneRegioneASL(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 * Verifica la coerenza di 3 campi così : se sono valorizzati e diversi da estero (999999 o 999) allora devono essere in anagrafica e coerenti
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());
		try {

			String campo1 =(String) recordDtoGenerico.getCampo(nomeCampo);
			String campo2 = (String)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCoerente2"));
			String campo3 = (String)recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("campoCoerente3"));

			String valoreCoerente1 = this.getParametri().getParametriMap().get("valoreCoerente1");
			String valoreCoerente2 = this.getParametri().getParametriMap().get("valoreCoerente2");
			String valoreCoerente3 = this.getParametri().getParametriMap().get("valoreCoerente3");
			log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());

			if(!valoreCoerente1.equals(campo1)){
				if(valoreCoerente2.equals(campo2) || valoreCoerente3.equals(campo3)){
					return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
				}else {
					return super.valida(nomeCampo,recordDtoGenerico);
				}
			}else {
				if(valoreCoerente2.equals(campo2) && valoreCoerente3.equals(campo3)){
					return List.of(creaEsitoOk(nomeCampo));
				}
				return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
			}

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}
	}


}
