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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaEsistenzaUguaglianzaCrossCampo")
public class RegolaEsistenzaUguaglianzaCrossCampo extends RegolaGenerica{

	public RegolaEsistenzaUguaglianzaCrossCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 * verifica la coerenza tra il valore di due campi del DTO in questo modo :
	 * Se il campo in input (nomeCampo) é valorizzato verifica che un altro campo dello stesso DTO (parametro campoDipendente ) abbia un valore uguale ad una lista  prefissata(parametro listaValoriCoerenti)
	 *
	 * Esempio: se Aslresidenza é valorizzata allora la stato Estero deve essere nullo o uguale ad IT
	 *
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {
			if (recordDtoGenerico.getCampo(nomeCampo) != null) {
				String nomeCampoConfronto = this.getParametri().getParametriMap().get("nomeCampoCoerente");
				String valoreCampoDipendente = String.valueOf(recordDtoGenerico.getCampo(nomeCampoConfronto));
				List<String> listaValoriConfronto = Arrays.stream(this.getParametri().getParametriMap().get("listaValoriCoerenti").split("\\|")).collect(Collectors.toList());

                if(valoreCampoDipendente != null && (listaValoriConfronto.contains(valoreCampoDipendente) || valoreCampoDipendente==null)){ //NOSONAR
					listaEsiti.add(creaEsitoOk(nomeCampo));
				}else{
					listaEsiti.add(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
				}
			}else{
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}

		} catch (InvocationTargetException | NoSuchMethodException |IllegalAccessException  e) {
			log.error("Impossibile validare la regola diversitaValoreCrossCampo per il campo " + nomeCampo, e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola diversitaValoreCrossCampo per il campo "  + nomeCampo );
		}
		return listaEsiti;
	}

}
