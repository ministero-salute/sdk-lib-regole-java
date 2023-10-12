package it.mds.sdk.libreriaregole.regole.catalogo.anagrafica;

import it.mds.sdk.anagrafiche.client.exceptions.MalformedRegistryException;
import it.mds.sdk.anagrafiche.client.exceptions.RegistryNotFoundException;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.GestoreAnagrafica;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordAnagrafica;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaD51")
public class RegolaD51 extends RegolaGenerica{

	public RegolaD51(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 *
	 * "Se tipo_str = 01 (strutture di ricovero), allora il campo cod_un_op (se valorizzato) può essere uguale ""0000""
	 * OR i primi due caratteri di cod_un_op devono essere presenti in anagrafica Codici Unità Operative
	 * AND gli ultimi due caratteri di cod_un_op trasformati in intero devono valere tra 0 e 99.
	 *
	 * Se tipo_str diverso da 01 non bisogna effettuare controlli, nemmeno di campo non vuoto
	 *
	 * Le anagrafiche devono essere filtrate sulla condizione:
	 * il periodo (campi ""anno"" e ""mese"") di riferimento del tracciato di input deve essere compreso tra le date di validità riportate nelle colonne DAT_INI_VLD, DAT_END_VLD_EFT della query. "
	 *
	 * @param nomeCampo campo da validare
	 * @param recordDtoGenerico DTO del record del flusso
	 * @return lista di {@link Esito}
	 */
	@Override
	public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

		try {
			log.debug("{}.valida - nomeCampo{} - BEGIN", this.getClass().getName(), nomeCampo);

			String campoDaValidare = (String) recordDtoGenerico.getCampo(nomeCampo);
			String campoCondizionante = this.getParametri().getParametriMap().get("campoCondizionante");
			String valoreCampoCondizionante = (String) recordDtoGenerico.getCampo(campoCondizionante);
			String mese = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("mese"));
			String anno = (String) recordDtoGenerico.getCampo(this.getParametri().getParametriMap().get("anno"));
			String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

			LocalDateTime dataConfrontoAnagrafica = LocalDateTime.of(LocalDate.of(Integer.parseInt(anno), Month.of(Integer.parseInt(mese)), 1), LocalTime.of(00, 00, 00));

			Pattern isIntegerPattern = Pattern.compile("^\\d+$");

			GestoreAnagrafica gestoreAnagrafica = getGestoreAnagrafica();
			List<RecordAnagrafica> listaValori = gestoreAnagrafica.richiediAnagrafica(nomeTabella, campoDaValidare, false).getRecordsAnagrafica();

			var listaDominio = listaValori
					.stream()
					.filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null &&
							(ra.getValidoDa().compareTo(dataConfrontoAnagrafica) * dataConfrontoAnagrafica.compareTo(ra.getValidoA())) >= 0)
							|| (ra.getValidoDa() == null && ra.getValidoA() == null)
					)
					.map(RecordAnagrafica::getDato)
					.collect(Collectors.toList());

			if(Objects.equals(valoreCampoCondizionante, "01"))
			{
				log.debug("campoCondizionante{} is 01", campoCondizionante);

				if("0000".equals(campoDaValidare)){
					return List.of(creaEsitoOk(nomeCampo));
				}

				if(campoDaValidare.length() != 4){
					log.warn("campoDaValidare{} length != 4", nomeCampo);
					return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
				}

				String codiceUnitaOperativa = campoDaValidare.substring(0, 2);
				String checkLastDigit = campoDaValidare.substring(2, 4);

				log.debug("Inizio validazione checkPerStrutturaRicovero01 - campoDaValidare{}", nomeCampo);

				if(!checkPerStrutturaRicovero01(codiceUnitaOperativa, listaDominio, checkLastDigit, isIntegerPattern)){
					log.warn("KO validazione - {}.checkPerStrutturaRicovero01 - campoDaValidare{}", this.getClass().getName(), nomeCampo);
					return List.of(creaEsitoKO(nomeCampo,this.getCodErrore(),this.getDesErrore()));
				}
				log.debug("OK validazione - {}.checkPerStrutturaRicovero01 - campoDaValidare{}", this.getClass().getName(), nomeCampo);
			}
			return List.of(creaEsitoOk(nomeCampo));

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedRegistryException | RegistryNotFoundException e) {
			log.error("{}.valida - Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo, this.getClass().getName(), e );
			throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo );
		}

	}

	public GestoreAnagrafica getGestoreAnagrafica() {
		return new GestoreAnagrafica();
	}

	private boolean checkPerStrutturaRicovero01(String codiceUnitaOperativa, List<String> listaDominio, String checkLastDigit, Pattern isIntegerPattern)
	{
		log.debug("Check {}.checkPerStrutturaRicovero01() - codiceUnitaOperativa{} - checkLastDigit{}", this.getClass().getName(), codiceUnitaOperativa, checkLastDigit);
		List<Boolean> esitiCheck = new ArrayList<>();
		esitiCheck.add(listaDominio.contains(codiceUnitaOperativa));
		esitiCheck.add(isInteger(checkLastDigit, isIntegerPattern));

		return !esitiCheck.contains(Boolean.FALSE);
	}

	public boolean isInteger(String str, Pattern pattern) {
		log.debug("Check {}.isInteger() - campoDaValidare{}", this.getClass().getName(), str);
		return pattern.matcher(str).matches();
	}



}
