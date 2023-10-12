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
@XmlDiscriminatorValue("regolaEsistenzaDisuguaglianzaCrossCampo")
public class RegolaEsistenzaDisuguaglianzaCrossCampo extends RegolaGenerica{

	public RegolaEsistenzaDisuguaglianzaCrossCampo(String nome, String codErrore, String desErrore, Parametri parametri) {
		super(nome, codErrore, desErrore, parametri);
	}

	/**
	 * verifica la coerenza tra il valore di due campi del DTO in questo modo :
	 * Se il campo in input (nomeCampo) é valorizzato verifica che un altro campo dello stesso DTO (parametro campoDipendente ) abbia un valore diverso da una lista  prefissata(parametro listaValoriIncoerenti)
	 *
	 * Esempio: se dataTrasferimento é valorizzata allora la modalitaTrasmissione deve essere diversa da MV o RE
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
				List<String> listaValoriConfronto = Arrays.stream(this.getParametri().getParametriMap().get("listaValoriIncoerenti").split("\\|")).collect(Collectors.toList());

                if(valoreCampoDipendente != null && !listaValoriConfronto.contains(valoreCampoDipendente)){
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
