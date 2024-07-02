/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.beans;

import it.mds.sdk.gestoreesiti.modelli.ErroreValidazione;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.regole.catalogo.*;
import it.mds.sdk.libreriaregole.regole.catalogo.anagrafica.*;
import it.mds.sdk.libreriaregole.regole.catalogo.cdm.*;
import it.mds.sdk.libreriaregole.regole.catalogo.cnt.Regola3943;
import it.mds.sdk.libreriaregole.regole.catalogo.crosscampo.*;
import it.mds.sdk.libreriaregole.regole.catalogo.date.*;
import it.mds.sdk.libreriaregole.regole.catalogo.input.RegolaCheckCampiInput;
import it.mds.sdk.libreriaregole.regole.catalogo.lunghezza.*;
import it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta.*;
import it.mds.sdk.libreriaregole.regole.diretta.*;
import jakarta.xml.bind.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorNode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@XmlDiscriminatorNode("@nome")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({
        RegolaDominioValoriAnagrafica.class,
        RegolaDiversitaValoreCrossCampo.class,
        RegolaEsistenzaDisuguaglianzaCrossCampo.class,
        RegolaIncoerenzaCrossCampo.class,
        RegolaIncoerenzaCrossCampo2.class,
        RegolaMaggioranzaCrossCampo.class,
        RegolaMinoranzaCrossCampo.class,
        RegolaOmissioneCrossCampo.class,
        RegolaUguaglianzaCrossCampo.class,
        RegolaDataAntecedente.class,
        RegolaDataPosteriore.class,
        RegolaIntervalloDate.class,
        RegolaLunghezzaCampoFacoltativo.class,
        RegolaLunghezzaCampoObbligatorio.class,
        RegolaObbligatorietaCondizionataValoreCampo.class,
        RegolaIntervalloMinMax.class,
        RegolaListaValoriAmmessiCampoFacoltativo.class,
        RegolaRegex.class,
        RegolaRegexCampoFacoltativo.class,
        RegolaUguaglianzaCampoValoreString.class,
        RegolaUguaglianzaCampoValoreInteger.class,
        RegolaObbligatorieta.class,
        RegolaListaValoriAmmessi.class,
        RegolaDominioValoriAnagrafica.class,
        RegolaDominioValoriAnagraficaFacoltativo.class,
        RegolaVerificaEta.class,
        RegolaUguaglianzaCrossCampoValore.class,
        RegolaDataAntecedenteCondizionale.class,
        RegolaIntervalloLunghezza.class,
        RegolaObbligatorietaCondizioneData.class,
        RegolaMaxLunghezzaCampoObbligatorio.class,
        RegolaValoreNonAmmesso.class,
        RegolaDataAntecedenteConParametro.class,
        RegolaObbligatorietaCondizionataLista.class,
        RegolaDataPosterioreConParametro.class,
        RegolaCodiceAIC.class,
        RegolaMaxLunghezzaCampoFacoltativo.class,
        RegolaDiversitaValoreParametro.class,
        RegolaUguaglianzaCrossDueCampi.class,
        RegolaUguaglianzaCrossCampoCondizionale.class,
        RegolaUguaglianzaCrossDueCampiCondizionale.class,
        RegolaObbligatorietaCondizionata.class,
        RegolaListaValoriNonAmmessi.class,
        RegolaDiversitaValoreCrossCampoFacoltativo.class,
        RegolaCrossCampoRegioneNazione.class,
        RegolaDominioValoriAnagraficaConcatenati.class,
        RegolaDominioValoriAnagraficaConcatenatiNazione.class,
        RegolaObbligatorietaNullCondizionataValoreCampo.class,
        RegolaObbligatorietaNullCondizionataValoreDiversoCampo.class,
//        RegolaObbligatorietaListaValoriAmmessi.class,
//        RegolaObbligatorietaValoriAmmessiParametro.class,
//        RegolaObbligatorietaListaValoriAmmessiPerCampo.class,
//        RegolaObbligatorietaSuDueCampiConValoreParametroPrefissato.class,
        RegolaObbligatorietaResType.class,
        RegolaObbligatorietaCampoDiversoDaParam.class,
        RegolaObbligatorietaNullCondizionataDueCampi.class,
//        RegolaObbligatorietaPerAlmenoUnCampo.class,
        RegolaObbligatorietaCodazallorig.class,
        RegolaObbligatorietaCondizionataNCampi.class,
        RegolaDominioValoreAnagraficaResloq.class,
        RegolaListaValoriAmmessiCondizionataCampoUguale.class,
        RegolaListaValoriAmmessiCondizionataCampoDiverso.class,
        RegolaDominioValoreAnagraficaParamType.class,
        RegolaDominioValoreAnagraficaCCBeta.class,
        RegolaDominioValoreAnagraficaEvallowLimit.class,
        RegolaDominioValoreAnagraficaSampUnitAnimalID.class,
        RegolaValoreMinoreDiUnCampo.class,
        RegolaDominioValoreAnagraficaOrigfishAreaCode.class,
//        RegolaControlloValoreMaggioreZero.class,
        RegolaListaValoriAmmessiCondizionataListaValori.class,
        RegolaUguaglianzaCrossCampoSeValorizzati.class,
        RegolaDominioValoreAnagraficaTrattamentoSINO.class,
        RegolaDominioValoreAnagraficaAetate.class,
        RegolaDominioValoreAnagraficaSampMatCodePackMat.class,
        RegolaDominioValoreAnagraficaExpressFatPercRF.class,
        RegolaDominioValoreAnagraficaExpressType.class,
        RegolaCampoUgualeAPrimiQuattroCaratteri.class,
        RegolaCampoUgualeAiCaratteriDopoSplit.class,
//        RegolaObbligatorietaListaAmmessiPerSingoloCampo.class,
        RegolaCampiUgualiEValorizzatiConfronto.class,
        RegolaListaValoriAmmessiCondizionataDueCampiUguali.class,
        RegolaListaValoriAmmessiCondizionataCampoDiversoSeconda.class,
        RegolaListaValoriAmmessiCondizionataCampoMaggiore.class,
        RegolaObbligatorietaCondizionataCampo.class,
        RegolaListaValoriAmmessiCampoDiversoCampoMaggiore.class,
        RegolaListaValoriAmmessiCampoDiversoCampoUguale.class,
        RegolaListaValoriAmmessiCampoUgualeCampoDiversoDaLista.class,
        RegolaCondizionataDaDueCampi.class,
        RegolaListaValoriAmmessiCampoUgualeCampoObbligatorio.class,
        RegolaCampoCondizionatoDueValori.class,
        RegolaCampoNotNullCondizionatoUnValoreUnCampo.class,
        RegolaValoreUgualeCondizionatoDateMinori.class,
//        RegolaBR236.class,
//        RegolaListaValoriAmmessiSubStringCampoUguale.class,
        RegolaObbligatorietaCampoNullListaValoriCampoUguale.class,
        RegolaDueCampiUgualiObbligatorietaCampoDiverso.class,
        RegolaTreCampiUgualiObbligatorietaCampoDiverso.class,
        RegolaCampoCondizionatoQuattroValori.class,
        RegolaCampoCondizionatoTreValori.class,
        RegolaCampoUgualeListaValoriAmmessiCampoObbligatorio.class,
//        RegolaBR091.class,
//        RegolaBR130.class,
//        RegolaBR057.class,
//        RegolaBR220.class,
//        RegolaBR200.class,
//        RegolaBR185.class,
//        RegolaBR006.class,
//        RegolaControlloUnicitaOggetto.class,
        RegolaDominioAnagraficaSampDateValido.class,
//        RegolaBR063.class,
        RegolaDominioAnagraficaSampDateValidoSampIdoneo.class,
        RegolaDominioValoreAnagraficaCampoUguale.class,
        RegolaDominioCampoUgualeValoreAnagrafica.class,
        RegolaDominioValoriAnagraficaSplittatiCampoUguale.class,
        RegolaDominioValoriAnagraficaSplittatiCampoDiverso.class,
        RegolaDominioValoriAnagraficaCampoUgualeListaValori.class,
        RegolaCheckCampiInput.class,
//        RegolaBR131.class,
//        RegolaBR123.class,
//        RegolaDR063.class,
        RegolaDominioCampoObbligatorioValoreAnagrafica.class,
//        RegolaDR027.class,
//        RegolaDR059.class,
//        RegolaBR064.class,
//        RegolaBR069.class,
//        RegolaBR152.class,
        //RegolaControlloCampoUgualeInCampione.class,
//        RegolaBR231.class,
        RegolaVerificaEtaString.class,
        RegolaDominioValoreAnagraficaListaValoriAmmessi.class,
        RegolaDominioValoreAnagraficaCampoUgualeCampoSplittato.class,
        RegolaDominioValoreAnagraficaCCAlpha.class,
        RegolaValidazionePeriodoRiferimento.class,
        RegolaCampoSplittatoDominioValoreAnagrafica.class,
        RegolaDominioValoreAnagraficaExpressPercFatPerc.class,
        RegolaObbligatorietaCondizionataDaListaValori.class,
        RegolaDataPosterioreCondizionaleFacoltativo.class,
        RegolaEsistenzaUguaglianzaCrossCampo.class,
        RegolaDominioValoriAnagraficaConcatenatiParametro.class,
        RegolaCampoNullCondizionatoDaListaCampi.class,
        RegolaCampoNotNullCondizionatoDaListaCampi.class,
        RegolaUguaglianzaListaCampi.class,
        RegolaDisuguaglianzaListaCampi.class,
        RegolaDataAntecedenteCampoFacoltativo.class,
        RegolaVerificaCodiceFiscale.class,
        RegolaDominioValoriAnagraficaSplit.class,
        RegolaIntervalloMinMaxInteger.class,
        Regola3campiTuttiPieniOTuttiVuoti.class,
        RegolaIncoerenzaRegioneComuneASLEstero.class,
        RegolaIncoerenzaStatoEsteroConComuneASL.class,
        RegolaDominioValoriAnagraficaSplitFacoltativo.class,
        RegolaB29Diretta.class,
        RegolaMaggioranzaCrossCampoString.class,
        RegolaSoloUnCampoValorizzato.class,
        RegolaDataCorrentePosterioreAnnoMeseRif.class,
        RegolaIncoerenzaRegioneComuneASLDomicilio.class,
        RegolaListaValoriAmmessiCondizionataValoreAnagrafica.class,
        RegolaDominioCampoDiversoValoreAnagraficaSplit.class,
        RegolaAlmenoUnCampoValorizzato.class,
        RegolaDominioCampoDiversoValoreAnagrafica.class,
        RegolaCoerenzaCampiUguali.class,
        RegolaErroreQuantitaDiretta.class,
        Regola3943.class,
        RegolaB17Diretta.class,
        RegolaAnonimatoImplicito.class,
        RegolaAnonimatoEsplicito.class,
        RegolaDataNascitaAnonimato.class,
        RegolaCondizionataDa1aNCampi.class,
        RegolaDominioValoriAnagraficaSplitConParametro.class,
        RegolaObbligatorietaEtaDisponentiFiduciari.class,
        RegolaObbligatorietaTutoriSoggettiAffidatari.class,
        RegolaObbligatorietaNoImplicitoNoEsplicito.class,
        RegolaDominioValoriAnagraficaConcatenatiPeriodoAnno.class,
        RegolaDominioValoriAnagraficaConcatenatiParametroPeriodoAnno.class,
        RegolaDominioValoriAnagraficaSplitFacoltativoListaValori.class,
        RegolaObbligatorietaPeriodoDiRiferimentoDiretta.class,
        RegolaAlmenoUnoObbligatori0NoImplicitoNoEsplicito.class,
        RegolaDominioCampoUgualeValoreAnagraficaMeseAnno.class,
        RegolaUguaglianzaValoreCrossCampoFacoltativo.class,
        RegolaNoImplicitoNoEsplicitoMaggioreZero.class,
        RegolaObbligatorietaDataErogazioneDiretta.class,
        RegolaStessoSegno.class,
        RegolaCampoFacoltativoCondizionataListaValoriAmmessi.class,
        RegolaIncoerenzaRegioneComuneASLSomministrazione.class,
        RegolaIncoerenzaASLSomministrazioneComuneRegione.class,
        RegolaIncoerenzaComuneRegioneASLSomministrazione.class,
        RegolaCoerenzaRegioneCampoInput.class,
        RegolaCodiceAICNonValorizzato.class,
        RegolaObbligatorietaCampoCondizionatoDataSomministrazione.class,
        RegolaAntigeneNonValorizzatoCorrettamente.class,
        RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilioModalita.class,
        RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita.class,
        RegolaIncoerenzaSitoIncoculazione.class,
        RegolaIncoerenzaStatoEsteroConComuneASLSomministrazione.class,
        RegolaIncoerenzaStatoEsteroConComuneASLSomministrazioneDiverso.class,
        RegolaDominioValoriAnagraficaConcatenatiFacoltativo.class,
        RegolaDataAntecedenteDataTrasmissioneCampoFacoltativo.class,
        RegolaIncoerenzaRegioneSomministrazioneTrasmissioneDomicilioModalita2.class,
        RegolaB42.class,
        RegolaIncoerenzaComuneRegioneASL.class,
        RegolaDiversitaValoreCampo.class,
        RegolaIncoerenzaRegioneSomministrazioneTrasmissione.class,
        RegolaIncoerenzaRegioneSomministrazioneResidenzaDomicilio.class,
        RegolaDataPosterioreCondizionataCampoFacoltativo.class,
        RegolaDataAntecedenteCondizionataCampoFacoltativo.class,
        RegolaDifferenzaDateSuperiore130Anni.class,
        //RegolaDataNonCompresaNelPeriodoRiferimento.class, //TODO: Regola 2095
        RegolaIncoerenzaASLSomministrazioneTipoTrasmissione.class,
        Regola1970.class,
        RegolaIncoerenzaStatoEsteroConComuneASL2.class,
        RegolaDataAntecedenteOUguale.class,
        RegolaDirettaD39.class,
        RegolaDominioValoriAnagraficaSplitConParametroSingolo.class,
        RegolaDominioCampoDiversoValoreAnagraficaSplitAVN_ASL.class,
        RegolaDominioCampoUgualeValoreAnagrafica_ALL.class,
        RegolaDominioValoriAnagraficaFacoltativo2.class,
        RegolaDominioValoriAnagraficaConcatenatiValoriDaEscludere.class,
        Regola4040.class,
        RegolaSism3446.class,
        RegolaBR2080.class,
        RegolaBR2095.class,
        RegolaBR1990.class,
        RegolaDominioValoriAnagraficaCampoCondizionante.class,
        RegolaBR4020.class,
        RegolaBR2060.class,
        RegolaBR1990PerFlussiTReMV.class,
        RegolaDominioValoriAnagraficaConParametro.class,
        RegolaDominioValoriAnagraficaFacoltativo2ConParametro.class,
        RegolaDominioValoriAnagraficaSplitFacoltativoConParametro.class,
        RegolaDominioValoriAnagraficaConcatenatiFacoltativoConParametro.class,
        RegolaBR2060ConParametro.class,
        RegolaDominioValoriAnagraficaConcatenatiConParametro.class,
        RegolaDominioCampoUgualeValoreAnagraficaConParametro.class,
        RegolaListaValoriAmmessiCondizionataValoreAnagraficaConParametro.class,
        RegolaDominioCampoUgualeValoreAnagrafica_ALLConParametro.class,
        RegolaBR4020ConParametro.class,
        RegolaDominioCampoDiversoValoreAnagraficaSplitConParametro.class,
        RegolaDominioCampoDiversoValoreAnagraficaConParametro.class,
        RegolaBR1975ConParametro.class,
        RegolaBR1960ConParametro.class,
        RegolaListaValoriAmmessiCondizionataCampoUguale2.class,
//        RegolaDueCampiUgualiCampoObbligatorio.class,
        RegolaCampoCondizionatoTreValoriCampoDiverso.class,
        RegolaBR195.class,
        RegolaDominioValoriAnagraficaSplitTreCampi.class,
        RegolaCampoCondizionatoQuattroValoriAnd.class,
        RegolaDominioValoriAnagraficaCampoUgualeCampoObbligatorio.class,
        RegolaDominioValoreAnagraficaCondizionataListaValoriAmmessiCampoUguale.class,
        RegolaCheckDigitTargatura.class,
        RegolaDR072.class,
        RegolaD51.class,
        RegolaCampoCondizionatoTreValori2.class,
        RegolaDominioValoriAnagraficaSplitFacoltativoListaValoriControlloCampi.class,
        RegolaDominioValoriAnagraficaDiscriminataTipoStruttura.class,
        RegolaB43.class,
        RegolaCodiceUnitaOperativa.class,
        RegolaDataCorrentePosterioreAnnoMeseGiornoRif.class,
        RegolaVerificaFormatoData.class,
        RegolaB98ErroreDurataContratto.class,
        RegolaB99ErroreFormaNegoziazione.class,
        RegolaB100ErroreQuantitaAggContrattualizzata.class,
        RegolaB102ErroreQuantita.class,
        RegolaB103PrezzoUnitarioAgg.class,
        RegolaB104ErroreImporto.class,
        RegolaB105ErroreCIG.class,
        RegolaB106ErroreCodCIG.class,
})
public abstract class RegolaGenerica {

    @XmlAttribute(name = "codiceErrore")
    private String codErrore;
    @XmlAttribute(name = "descrizioneErrore")
    private String desErrore;
    @XmlElement
    private Parametri parametri;


    public abstract List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    protected RegolaGenerica(String nome, String codErrore, String desErrore, Parametri parametri) {
        this.codErrore = codErrore;
        this.desErrore = desErrore;
        this.parametri = parametri;
    }

    protected Esito creaEsitoOk(String nomeCampo) {
        return Esito.builder()
                .withCampo(nomeCampo)
                .withValoreEsito(true)
                .withErroriValidazione(new ArrayList<>())
                .build();
    }

    protected Esito creaEsitoKO(String nomeCampo, String codErrore, String desErrore) {
        List<ErroreValidazione> listaErrori = new ArrayList<>();
        listaErrori.add(ErroreValidazione.builder()
                .withCodice(codErrore)
                .withDescrizione(desErrore)
                .build());
        return Esito.builder()
                .withCampo(nomeCampo)
                .withValoreEsito(false)
                .withErroriValidazione(listaErrori)
                .build();
    }
}
