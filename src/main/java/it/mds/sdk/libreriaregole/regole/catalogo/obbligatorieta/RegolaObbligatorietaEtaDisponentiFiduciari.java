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
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaObbligatorietaEtaDisponentiFiduciari")
public class RegolaObbligatorietaEtaDisponentiFiduciari extends RegolaGenerica {

	public RegolaObbligatorietaEtaDisponentiFiduciari(String nome, String codErrore, String desErrore,
			Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 * "Se il donatore è minorenne controllare la data di sottoscrizione che siano
	 * <= 17 anni Gli altri (disponenti e fiduciari) sempre MAGGIORENNI"
	 *
	 * @param nomeCampo         campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

		log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN", this.getClass().getName(), nomeCampo,
				recordDtoGenerico.toString());
		try {
			String dataNascitaDonatore = (String) recordDtoGenerico.getCampo("donatoreDataNascita");
			String disponenti1DataNascita = (String) recordDtoGenerico.getCampo("disponenti1DataNascita");
			String disponenti2DataNascita = (String) recordDtoGenerico.getCampo("disponenti2DataNascita");
			String fiduciari1DataNascita = (String) recordDtoGenerico.getCampo("fiduciari1DataNascita");
			String fiduciari2DataNascita = (String) recordDtoGenerico.getCampo("fiduciari2DataNascita");
			String donatoreMinorenne = (String) recordDtoGenerico.getCampo("donatoreMinorenne");

			if (dataNascitaDonatore == null || dataNascitaDonatore.isBlank()) {
				return checkDataNascitaDonatore(nomeCampo, donatoreMinorenne);
			}

			if (isMinorenne(LocalDate.parse(dataNascitaDonatore))) {
				log.debug("Il donatore è minorenne.");
				if ((disponenti1DataNascita != null && isMinorenne(LocalDate.parse(disponenti1DataNascita)))
						|| (disponenti2DataNascita != null && isMinorenne(LocalDate.parse(disponenti2DataNascita)))
						|| (fiduciari1DataNascita != null && isMinorenne(LocalDate.parse(fiduciari1DataNascita)))
						|| (fiduciari2DataNascita != null && isMinorenne(LocalDate.parse(fiduciari2DataNascita)))) {
					log.debug("Attenzione! Si è generato uno scarto: Un donatore/Fiduciario è minorenne.");
					return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
				}
			}
			return List.of(creaEsitoOk(nomeCampo));

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]", this.getClass().getName(), nomeCampo,
					recordDtoGenerico, e);
			throw new ValidazioneImpossibileException("Impossibile validare la regola per il campo " + nomeCampo);
		} catch (DateTimeParseException e) {
			log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]", this.getClass().getName(), nomeCampo,
					recordDtoGenerico, e);
			return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
		}

	}

	private List<Esito> checkDataNascitaDonatore(String nomeCampo, String donatoreMinorenne) {
		log.debug("La data nascita donatore è nulla");
		if ("NO".equalsIgnoreCase(donatoreMinorenne)) {
			return List.of(creaEsitoOk(nomeCampo));
		} else {
			return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
		}
	}

	private boolean isMinorenne(LocalDate birthDate) {
		return Period.between(birthDate, LocalDate.now()).getYears() < 18;
	}

}
