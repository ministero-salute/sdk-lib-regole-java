package it.mds.sdk.libreriaregole.regole.diretta;

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
import java.util.Collections;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaAnonimatoImplicito")
public class RegolaAnonimatoImplicito extends RegolaGenerica {

	public RegolaAnonimatoImplicito(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 * Verifica se il record rispetta anonimato implicito . Diremo che il record
	 * rispetta l'anonimato implicito se: tipo_er == "04" OR tipo_er=="05" OR ( tipo_er=="02" AND tipo_str!="04")
	 *
	 * @param nomeCampo         nome campo da validare
	 * @param recordDtoGenerico DTO
	 * @return lista Esiti
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

		log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), nomeCampo,
				recordDtoGenerico.toString());

		try {
			String tipo_erog = (String) recordDtoGenerico.getCampo("tipoErogazione");
			String tipo_er = (String) recordDtoGenerico.getCampo("tipoErogatore");
			String tipo_str = (String) recordDtoGenerico.getCampo("tipoStrutturaErogante");


			// tipo_erog == "04" OR tipo_er=="05" OR ( tipo_er=="02" AND tipo_str!="04" )

			if ("04".equals(tipo_erog) || "05".equals(tipo_er) || ( "02".equals(tipo_er) && !"04".equals(tipo_str) )) {
				return Collections.singletonList(creaEsitoOk(nomeCampo));
			} else {
				return Collections.singletonList(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
			}

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]", this.getClass().getName(), nomeCampo,
					recordDtoGenerico, e);
			throw new ValidazioneImpossibileException("Impossibile validare la RegolaAnonimatoImplicito ");
		}
	}

}
