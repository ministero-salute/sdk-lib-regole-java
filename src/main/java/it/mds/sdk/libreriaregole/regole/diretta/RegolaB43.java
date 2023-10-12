package it.mds.sdk.libreriaregole.regole.diretta;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaB43")
public class RegolaB43 extends RegolaGenerica {

    public RegolaB43(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }


    /**
     * Se il valore del campo tipo_canale del tracciato di input è uguale al carattere R allora
     * se tipo_er = 02 allora tipo_er # cod_asl # id_er  deve esistere in anagrafica;
     * se tipo_er = 01, 03, 06 allora tipo_er # id_er  deve esistere in anagrafica;
     * Le anagrafiche devono essere filtrate sulla condizione:
     * il periodo (campi ""anno"" e ""mese"") di riferimento del tracciato di input deve essere compreso tra le date di validità riportate nelle colonne DAT_INI_VLD, DAT_END_VLD_EFT della query.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {
        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] - BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            String codiceStrutturaErogante = (String) (recordDtoGenerico.getCampo(nomeCampo));
            String tipoCanale = (String) recordDtoGenerico.getCampo("tipoCanale");
            String nomeTabella = this.getParametri().getParametriMap().get("nomeTabella");

            if (!"R".equalsIgnoreCase(tipoCanale)) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }

            if (codiceStrutturaErogante == null || codiceStrutturaErogante.isEmpty()) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }

            String cod_asl = (String) recordDtoGenerico.getCampo("codiceAziendaSanitariaErogante");
            //tipoErogatore
            String parametroCondizionante = this.getParametri().getParametriMap().get("parametroCondizionante");
            String valoreParametroCondizionante = String.valueOf(recordDtoGenerico.getCampo(parametroCondizionante));
            List<String> listaValori = Arrays.stream(this.getParametri().getParametriMap().get("listaValoriAmmessi").split("\\|")).collect(Collectors.toList());

            if (!listaValori.contains(valoreParametroCondizionante)) {
                return Collections.singletonList(creaEsitoOk(nomeCampo));
            }

            String annoDiRiferimento = (String) recordDtoGenerico.getCampo("annoDiRiferimento");
            String meseDiRiferimento = (String) recordDtoGenerico.getCampo("meseDiRiferimento");
            LocalDateTime annoMeseDiRiferimento = LocalDateTime.of(LocalDate.of(Integer.parseInt(annoDiRiferimento), Month.of(Integer.parseInt(meseDiRiferimento)), 1), LocalTime.of(00, 00, 00));

            GestoreAnagrafica gestoreAnagrafica = getGestoreAnagrafica();

            if ("02".equals(valoreParametroCondizionante)) {
                //concatenazione dei 3 campi
                String valoreDaValidareConcatenato = valoreParametroCondizionante + "#" + cod_asl + "#" + codiceStrutturaErogante;
                List<RecordAnagrafica> valoriTabella = gestoreAnagrafica.richiediAnagrafica(nomeTabella, valoreDaValidareConcatenato, false).getRecordsAnagrafica();

                //recupero il dominio dei valori validi
                List<String> listaDominio;
                log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
                listaDominio = valoriTabella
                        .stream()
                        .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(annoMeseDiRiferimento) * annoMeseDiRiferimento.compareTo(ra.getValidoA())) >= 0)
                                || (ra.getValidoDa() == null && ra.getValidoA() == null) || ((ra.getValidoDa() != null && ra.getValidoA() == null) && (ra.getValidoDa().compareTo(annoMeseDiRiferimento) <= 0)))
                        .map(RecordAnagrafica::getDato)
                        .collect(Collectors.toList());
                log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());
                if (listaDominio.contains(valoreDaValidareConcatenato)) {
                    return List.of(creaEsitoOk(nomeCampo));
                } else {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            } else {
                return concatenazioneRecuperoDomini(nomeCampo, codiceStrutturaErogante, nomeTabella,
						valoreParametroCondizionante, annoMeseDiRiferimento, gestoreAnagrafica);
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 MalformedRegistryException | RegistryNotFoundException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] ", this.getClass().getName(), nomeCampo, recordDtoGenerico);
            throw new ValidazioneImpossibileException("Impossibile validare la regola dominio valori anagrafica per il campo " + nomeCampo);
        }

    }


	private List<Esito> concatenazioneRecuperoDomini(String nomeCampo, String codiceStrutturaErogante,
			String nomeTabella, String valoreParametroCondizionante, LocalDateTime annoMeseDiRiferimento,
			GestoreAnagrafica gestoreAnagrafica) throws MalformedRegistryException, RegistryNotFoundException {
		//concatenazione dei 2 campi
		String valoreDaValidareConcatenato = valoreParametroCondizionante + "#" + codiceStrutturaErogante;
		List<RecordAnagrafica> valoriTabella = gestoreAnagrafica.richiediAnagrafica(nomeTabella, valoreDaValidareConcatenato, false).getRecordsAnagrafica();

		//recupero il dominio dei valori validi
		List<String> listaDominio;
		log.trace("regola domino valori anagrafica inizio stream filter : " + System.nanoTime());
		listaDominio = valoriTabella
		        .stream()
		        .filter(ra -> (ra.getValidoDa() != null && ra.getValidoA() != null && (ra.getValidoDa().compareTo(annoMeseDiRiferimento) * annoMeseDiRiferimento.compareTo(ra.getValidoA())) >= 0)
		                || (ra.getValidoDa() == null && ra.getValidoA() == null) || ((ra.getValidoDa() != null && ra.getValidoA() == null) && (ra.getValidoDa().compareTo(annoMeseDiRiferimento) <= 0)))
		        .map(RecordAnagrafica::getDato)
		        .collect(Collectors.toList());
		log.trace("regola domino valori anagrafica fine stream filter : " + System.nanoTime());
		if (listaDominio.contains(valoreDaValidareConcatenato)) {
		    return List.of(creaEsitoOk(nomeCampo));
		} else {
		    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
		}
	}

    public GestoreAnagrafica getGestoreAnagrafica() {
        return new GestoreAnagrafica();
    }
}
