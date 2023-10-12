package it.mds.sdk.libreriaregole.regole.catalogo.lunghezza;

import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaLunghezzaCampoObbligatorio")
public class RegolaLunghezzaCampoObbligatorio extends RegolaGenerica {

	public RegolaLunghezzaCampoObbligatorio(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * Verifica che la lunghezza del campo in ingresso sia quella definita nel
	 * parametro size
	 * 
	 * @param nomeCampo         è il nome del campo da validare
	 * @param recordDtoGenerico corrisponde al DTO del record di un flusso
	 * @return ritorna una lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
	 */

	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
		List<Esito> listaEsiti = new ArrayList<>();
		try {
			String daValidare = String.valueOf(recordDtoGenerico.getCampo(nomeCampo));
			Integer lunghezza = Integer.valueOf(this.getParametri().getParametriMap().get("size"));

			if (StringUtils.isBlank(daValidare) || "null".equals(daValidare) || !lunghezza.equals(daValidare.length())) {
				listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
			} else {
				listaEsiti.add(creaEsitoOk(nomeCampo));
			}

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("Non è possibile validare la regola lunghezza del campo obbligatorio " + nomeCampo, e);
			throw new ValidazioneImpossibileException(
					"Non è possibile validare la regola lunghezza del campo obbligatorio " + nomeCampo);
		}
		return listaEsiti;
	}
}
