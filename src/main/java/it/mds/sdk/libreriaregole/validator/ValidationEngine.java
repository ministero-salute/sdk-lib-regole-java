/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.validator;

import it.mds.sdk.gestoreesiti.GestoreEsiti;
import it.mds.sdk.gestoreesiti.GestoreRunLog;
import it.mds.sdk.gestoreesiti.Progressivo;
import it.mds.sdk.gestoreesiti.modelli.*;
import it.mds.sdk.gestoreesiti.validazioneXSD.MainTester;
import it.mds.sdk.gestorefile.GestoreFile;
import it.mds.sdk.gestorefile.exception.XSDNonSupportedException;
import it.mds.sdk.gestorefile.factory.GestoreFileFactory;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.gestorevalidazione.BloccoValidazione;
import it.mds.sdk.libreriaregole.regole.beans.Campo;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import it.mds.sdk.libreriaregole.regole.beans.RegoleFlusso;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("validationEngine")
public class ValidationEngine {

    private static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
     * Restitiusce il risultato della validazione dei record contenuti nel blocco {bloccoRecord} seguendo le regole
     * di {regoleFlusso} per la run {idRun}.
     * </p>
     * Il BloccoValidazione contiene le informazioni del risultato della validazione, in numero di record esaminati e
     * scartati.
     *
     * @param bloccoRecord blocco di record da validare
     * @param regoleFlusso regole su cui validare i record
     * @param idRun        id della run
     * @return BloccoValidazione che contiene i risultati della validazione e altre informazioni utili sul blocco
     */
    public BloccoValidazione validaFlussoBlocco(List<RecordDtoGenerico> bloccoRecord, RegoleFlusso regoleFlusso, String idRun, int numRecordValidati) {

        log.debug("Inizio validazione blocco idRun {}", idRun);
        BloccoValidazione bloccoValidazione = getBloccoValidazione();
        ValidazioneFlusso validazioneFlusso;
        List<ValidazioneFlusso> results = new ArrayList<>();
        List<RecordDtoGenerico> listaAccettati = new ArrayList<>();
        List<RecordDtoGenerico> listaScartati = new ArrayList<>();
        //id record che andrà settato per ogni record validato
        int numeroRecord = numRecordValidati;
        // numero totale record del blocco validato
        int numRecordBlocco = 0;
        int scartati = 0;

        //Validazione blocco di record
        for (RecordDtoGenerico recordDtoGenerico : bloccoRecord) {
            numeroRecord++;
            numRecordBlocco++;
            validazioneFlusso = getValidazioneFlusso();
            validazioneFlusso.setRecordProcessato(recordDtoGenerico);
            List<Esito> esitiRecord = this.validaRecord(recordDtoGenerico, regoleFlusso);

            if (!esitiRecord.stream().filter(p -> !p.isValoreEsito()).collect(Collectors.toList()).isEmpty()) {
                scartati++;
                listaScartati.add(recordDtoGenerico);
                validazioneFlusso.setNumeroRecord(String.valueOf(numeroRecord));
                validazioneFlusso.getListEsiti().addAll(esitiRecord);
                results.add(validazioneFlusso);
                log.debug("Record {} non validato, risultato validazione : {}", recordDtoGenerico, validazioneFlusso);
            } else {
                listaAccettati.add(recordDtoGenerico);
            }
        }
        bloccoValidazione.setValidazioneFlussoList(results);
        bloccoValidazione.setScartati(scartati);
        bloccoValidazione.setNumeroRecord(numRecordBlocco);
        bloccoValidazione.setIdRun(idRun);
        bloccoValidazione.setRecordList(listaAccettati);
        log.info("Validazione completata del blocco per idRun {}", bloccoValidazione.getIdRun());

        return bloccoValidazione;
    }

    public BloccoValidazione validaFlussoBloccoConRegoleInteroFlusso(
            List<RecordDtoGenerico> bloccoRecord, RegoleFlusso regoleFlusso, String idRun, List<String> listaIdRecordsScartatiBR3060, int numRecordValidati
    ) {
        log.debug("Inizio validazione blocco idRun {}", idRun);
        BloccoValidazione bloccoValidazione = getBloccoValidazione();
        ValidazioneFlusso validazioneFlusso;
        List<ValidazioneFlusso> results = new ArrayList<>();
        List<RecordDtoGenerico> listaAccettati = new ArrayList<>();
        List<RecordDtoGenerico> listaScartati = new ArrayList<>();
        int numeroRecord = numRecordValidati;
        int scartati = 0;
        int numRecordBlocco = 0;
        //Validazione blocco di record

        ErroreValidazione error = getErroreValidazioneFromCodiceEDescrizione("3060", "Il tipo di formulazione non è coerente con il numero degli antigenti indicati");
        Esito e = createEsito("codTipoFormulazione", null, false, List.of(error));

        for (RecordDtoGenerico recordDtoGenerico : bloccoRecord) {
            numeroRecord++;
            numRecordBlocco++;
            validazioneFlusso = getValidazioneFlusso();
            validazioneFlusso.setRecordProcessato(recordDtoGenerico);
            List<Esito> esitiRecord = this.validaRecord(recordDtoGenerico, regoleFlusso);
            String numeroRecordString = String.valueOf(numeroRecord);

            var scartoSenzaBR3060 = listaIdRecordsScartatiBR3060.stream().filter(es -> Objects.equals(numeroRecordString, es)).findFirst();

            if (scartoSenzaBR3060.isPresent()) {
                esitiRecord.add(e);
            }

            if (!esitiRecord.stream().filter(p -> !p.isValoreEsito()).collect(Collectors.toList()).isEmpty()) {
                scartati++;
                listaScartati.add(recordDtoGenerico);
                validazioneFlusso.setNumeroRecord(String.valueOf(numeroRecord));
                validazioneFlusso.getListEsiti().addAll(esitiRecord);
                results.add(validazioneFlusso);
                log.debug("Record {} non validato, risultato validazione : {}", recordDtoGenerico, validazioneFlusso);
            } else {
                listaAccettati.add(recordDtoGenerico);
            }
        }
        bloccoValidazione.setValidazioneFlussoList(results);
        bloccoValidazione.setScartati(scartati);
        bloccoValidazione.setNumeroRecord(numRecordBlocco);
        bloccoValidazione.setIdRun(idRun);
        bloccoValidazione.setRecordList(listaAccettati);
        log.info("Validazione completata del blocco per idRun {}", bloccoValidazione.getIdRun());
        return bloccoValidazione;
    }

    public BloccoValidazione validaFlussoBloccoConRegoleInteroFlussoCNS(
            List<RecordDtoGenerico> bloccoRecord, RegoleFlusso regoleFlusso, String idRun, List<String> listaIdRecordsScartatiCNS, int numRecordValidati
    ) {
        log.debug("Inizio validazione blocco idRun {}", idRun);
        BloccoValidazione bloccoValidazione = getBloccoValidazione();
        ValidazioneFlusso validazioneFlusso;
        List<ValidazioneFlusso> results = new ArrayList<>();
        List<RecordDtoGenerico> listaAccettati = new ArrayList<>();
        List<RecordDtoGenerico> listaScartati = new ArrayList<>();
        int numeroRecord = numRecordValidati;
        int scartati = 0;
        int numRecordBlocco = 0;
        //Validazione blocco di record

        ErroreValidazione error = getErroreValidazioneFromCodiceEDescrizione("B80", "Errore formato dati comunicati");
        Esito e = createEsito("codiceAziendaSanitaria", null, false, List.of(error));

        for (RecordDtoGenerico recordDtoGenerico : bloccoRecord) {
            numeroRecord++;
            numRecordBlocco++;
            validazioneFlusso = getValidazioneFlusso();
            validazioneFlusso.setRecordProcessato(recordDtoGenerico);
            List<Esito> esitiRecord = this.validaRecord(recordDtoGenerico, regoleFlusso);
            String numeroRecordString = String.valueOf(numeroRecord);

            var scartoSenzaB80 = listaIdRecordsScartatiCNS.stream().filter(es -> Objects.equals(numeroRecordString, es)).findFirst();

            if (scartoSenzaB80.isPresent()) {
                esitiRecord.add(e);
            }

            if (!esitiRecord.stream().filter(p -> !p.isValoreEsito()).collect(Collectors.toList()).isEmpty()) {
                scartati++;
                listaScartati.add(recordDtoGenerico);
                validazioneFlusso.setNumeroRecord(String.valueOf(numeroRecord));
                validazioneFlusso.getListEsiti().addAll(esitiRecord);
                results.add(validazioneFlusso);
                log.debug("Record {} non validato, risultato validazione : {}", recordDtoGenerico, validazioneFlusso);
            } else {
                listaAccettati.add(recordDtoGenerico);
            }
        }
        bloccoValidazione.setValidazioneFlussoList(results);
        bloccoValidazione.setScartati(scartati);
        bloccoValidazione.setNumeroRecord(numRecordBlocco);
        bloccoValidazione.setIdRun(idRun);
        bloccoValidazione.setRecordList(listaAccettati);
        log.info("Validazione completata del blocco per idRun {}", bloccoValidazione.getIdRun());
        return bloccoValidazione;
    }

    public BloccoValidazione validaFlussoBloccoConRegoleInteroFlussoCT2(
            List<RecordDtoGenerico> bloccoRecord, RegoleFlusso regoleFlusso, String idRun, List<String> listaIdRecordsScartatiCT2, int numRecordValidati
    ) {
        log.debug("Inizio validazione blocco idRun {}", idRun);
        BloccoValidazione bloccoValidazione = getBloccoValidazione();
        ValidazioneFlusso validazioneFlusso;
        List<ValidazioneFlusso> results = new ArrayList<>();
        List<RecordDtoGenerico> listaAccettati = new ArrayList<>();
        List<RecordDtoGenerico> listaScartati = new ArrayList<>();
        int numeroRecord = numRecordValidati;
        int scartati = 0;
        int numRecordBlocco = 0;
        //Validazione blocco di record

        ErroreValidazione error = getErroreValidazioneFromCodiceEDescrizione("B95", "Errore formato dati comunicati");
        Esito e = createEsito("codiceAziendaSanitaria", null, false, List.of(error));

        for (RecordDtoGenerico recordDtoGenerico : bloccoRecord) {
            numeroRecord++;
            numRecordBlocco++;
            validazioneFlusso = getValidazioneFlusso();
            validazioneFlusso.setRecordProcessato(recordDtoGenerico);
            List<Esito> esitiRecord = this.validaRecord(recordDtoGenerico, regoleFlusso);
            String numeroRecordString = String.valueOf(numeroRecord);

            var scartoSenzaB95 = listaIdRecordsScartatiCT2.stream().filter(es -> Objects.equals(numeroRecordString, es)).findFirst();

            if (scartoSenzaB95.isPresent()) {
                esitiRecord.add(e);
            }

            if (!esitiRecord.stream().filter(p -> !p.isValoreEsito()).collect(Collectors.toList()).isEmpty()) {
                scartati++;
                listaScartati.add(recordDtoGenerico);
                validazioneFlusso.setNumeroRecord(String.valueOf(numeroRecord));
                validazioneFlusso.getListEsiti().addAll(esitiRecord);
                results.add(validazioneFlusso);
                log.debug("Record {} non validato, risultato validazione : {}", recordDtoGenerico, validazioneFlusso);
            } else {
                listaAccettati.add(recordDtoGenerico);
            }
        }
        bloccoValidazione.setValidazioneFlussoList(results);
        bloccoValidazione.setScartati(scartati);
        bloccoValidazione.setNumeroRecord(numRecordBlocco);
        bloccoValidazione.setIdRun(idRun);
        bloccoValidazione.setRecordList(listaAccettati);
        log.info("Validazione completata del blocco per idRun {}", bloccoValidazione.getIdRun());
        return bloccoValidazione;
    }

    public void creaFileEsiti(BloccoValidazione bloccoValidazione) {
        GestoreFile gestoreFile = GestoreFileFactory.getGestoreFile("CSV");
        GestoreEsiti gestoreEsiti = createGestoreEsitiFromGestoreFile(gestoreFile);
        gestoreEsiti.creaValidazioneFlusso(bloccoValidazione.getIdRun(), bloccoValidazione.getValidazioneFlussoList());
        log.debug("Creato file esiti per id {}", bloccoValidazione.getIdRun());
    }

    public List<Esito> validaRecord(RecordDtoGenerico recordDto, RegoleFlusso regoleFlusso) {
        log.debug("{}.validaRecord - recordDto[{}] - regoleFlusso[{}] - BEGIN",
                this.getClass().getName(), recordDto.toString(), regoleFlusso.toString());

        List<Esito> esiti = new ArrayList<>();
        for (Campo c : regoleFlusso.getCampi()) {
            String nomeCampo = c.getNomeCampo();
            Esito esito = Esito.builder().withCampo(c.getNomeCampo()).withValoreEsito(true).withErroriValidazione(new ArrayList<>()).build();
            for (RegolaGenerica r : c.getRegole()) {
                try {
                    log.trace("nome campo " + c.getNomeCampo());
                    log.trace("regola " + r.getCodErrore() + " " + r.getClass());
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    Esito e = r.valida(c.getNomeCampo(), recordDto).get(0);
                    stopWatch.stop();
                    Stats.addTempo(r.getClass().toString(), stopWatch.getTotalTimeNanos());
                    if (!e.isValoreEsito()) {
                        esito.getErroriValidazione().addAll(e.getErroriValidazione());
                        esito.setValoreEsito(e.isValoreEsito());
                        Object valoreCampo = recordDto.getCampo(c.getNomeCampo());
                        String scarto = valoreCampo == null ? null : valoreCampo.toString();
                        esito.setValoreScarto(scarto);
                    }
                } catch (SecurityException | IllegalArgumentException | IllegalAccessException |
                         NoSuchMethodException | InvocationTargetException e) {
                    log.error("Non è possibile validare il record per campo {}", nomeCampo, e);
                    esito.getErroriValidazione().add(new ErroreValidazione("000", "Errore generico"));
                    esito.setValoreEsito(false);
                }

            }
            //Esito va messo in lista solo se non validato
            if (!esito.isValoreEsito()) {
                esiti.add(esito);
                log.debug("Esito {} non validato, viene aggiunto alla lista", esito);
            }
        }
        log.debug("Esito validazione per record: {} : {}", recordDto, esiti);
        return esiti;
    }

    public List<Esito> validaSingoloRecord(RecordDtoGenerico recordDto, RegoleFlusso regoleFlusso, String idRun) {
        List<Esito> result = this.validaRecord(recordDto, regoleFlusso);
        GestoreFile gestoreFile = GestoreFileFactory.getGestoreFile("CSV");
        GestoreEsiti gestoreEsiti = createGestoreEsitiFromGestoreFile(gestoreFile);
        gestoreEsiti.creaEsitiValidazione(idRun, result);
        GestoreRunLog gestoreRunLog = createGestoreRunLog(gestoreFile, Progressivo.creaProgressivo(Progressivo.Fonte.FILE));
        InfoRun infoRun = gestoreRunLog.getRun(idRun);
        Boolean isOk = result.stream().filter(e -> !e.isValoreEsito()).collect(Collectors.toList()).isEmpty();
        infoRun.setNumeroRecordScartati(isOk ? 0 : 1);
        infoRun.setNumeroRecordAccettati(isOk ? 1 : 0);
        infoRun.setDataFineEsecuzione(new Timestamp(System.currentTimeMillis()));
        gestoreRunLog.updateRun(infoRun);
        return result;
    }

    /**
     * Metodo che data una lista di RecordDtoGenerico, che rappresenta l'intero tracciato da analizzare, controlla i
     * duplicati dei campi specificati nel parametro campi e li elimina dal tracciato.
     * Il risultato è un oggetto di tipo InteroFlussoEsito che contiene la lista dei RecordDtoGenerico filtrato e
     * l'oggetto che rappresenta gli esiti.
     * Vengono scartati solo i record che hanno i valori di tutti i campi indicati identici.
     *
     * @param listaDaFiltrare lista dei record da filtrare, l'intero tracciato
     * @param campi           i campi su cui controllare i duplicati
     * @param codice          codice errore che va negli esiti
     * @param descrizione     descrizione errore che va negli esiti
     * @return
     */
    public InteroFlussoEsito validaRegolaInteroFlussoGenerica(List<RecordDtoGenerico> listaDaFiltrare,
                                                              List<String> campi, String codice, String descrizione) {
        Map<String, Long> concatMap =
                listaDaFiltrare.stream().collect(Collectors.groupingBy(r -> {
                            StringBuilder sb = new StringBuilder();
                            String concatenazione;
                            campi.forEach(c -> {
                                try {
                                    sb.append(r.getCampo(c).toString().concat("#"));
                                } catch (IllegalAccessException | NoSuchMethodException |
                                         InvocationTargetException e) {
                                    throw new ValidazioneImpossibileException("impossibile filtrare record intero flusso " + c, e);
                                }
                            });
                            concatenazione = sb.deleteCharAt(sb.length() - 1).toString();
                            return concatenazione;
                        },
                        Collectors.counting()));
        List<String> campiConcatentatiDaEliminare = new ArrayList<>();
        for (String con : concatMap.keySet()) {
            if (concatMap.get(con) > 1) {
                campiConcatentatiDaEliminare.add(con);
            }
        }
        log.debug("Campi da eliminare concatenati {}", campiConcatentatiDaEliminare);
        List<RecordDtoGenerico> daCancellare = new ArrayList<>();
        List<ValidazioneFlusso> esitiCancellati = new ArrayList<>();
        for (int i = 0; i < listaDaFiltrare.size(); i++) {
            int numero = i + 1;
            RecordDtoGenerico s = listaDaFiltrare.get(i);
            campiConcatentatiDaEliminare.forEach(c -> {
                String concatS;
                StringBuilder sb = new StringBuilder();
                campi.forEach(ca -> {
                    try {
                        sb.append(s.getCampo(ca).toString().concat("#"));
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        throw new ValidazioneImpossibileException("impossibile filtrare record intero flusso " + s, e);
                    }
                });
                concatS = sb.deleteCharAt(sb.length() - 1).toString();
                if (concatS.equals(c)) {
                    daCancellare.add(s);
                    List<ErroreValidazione> errVal = new ArrayList<>();
                    errVal.add(new ErroreValidazione(codice, descrizione));
                    List<Esito> esiti = new ArrayList<>();
                    esiti.add(new Esito("record duplicato", "campo", false, errVal));
                    ValidazioneFlusso vl = new ValidazioneFlusso();
                    vl.setListEsiti(esiti);
                    vl.setNumeroRecord(String.valueOf(numero));
                    esitiCancellati.add(vl);
                }
            });
        }
        log.trace("Elementi da cancellare dalla lista : {}", daCancellare);
        listaDaFiltrare.removeAll(daCancellare);


        return new InteroFlussoEsito(listaDaFiltrare, esitiCancellati);
    }


    private static class Stats {

        private static Map<String, Long> tempoRegole = new HashMap<>();
        private static int esecuzioni = 0;

        static void addTempo(String regola, long tempoNano) {
            Long tempo = tempoRegole.get(regola);
            esecuzioni++;
            if (tempo == null) {
                tempoRegole.put(regola, tempoNano);
            } else {
                tempoRegole.put(regola, tempo + tempoNano);
            }
            if (esecuzioni % 5000000 == 0) {
                log.info("esecuzione {}, tempistiche \n{}", esecuzioni, scriviTempi());
            }
        }

        static String scriviTempi() {
            StringBuilder sb = new StringBuilder();
            for (String clazz : tempoRegole.keySet()) { 
                long tempo = tempoRegole.get(clazz);
                sb.append("Classe : " + clazz + " tempo ns: " + tempo + " in sec: " + tempo / 1000000000 + "\n");
            }
            return sb.toString();
        }
    }

    public BloccoValidazione startValidaFlussoBlocco(
            List<RecordDtoGenerico> records, RegoleFlusso regoleFlusso, String idRun, GestoreRunLog gestoreRunLog, Integer numRecordValidati
    ) {
        log.debug("Inizio validazione flusso ");
        InfoRun infoRun;
        BloccoValidazione bloccoValidazione;
        try {
            bloccoValidazione = this.validaFlussoBlocco(records, regoleFlusso, idRun, numRecordValidati);
        } catch (ValidazioneImpossibileException vie) {
            infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_GESTIONE_VALIDAZIONE);
            log.error("Errore validazione idrun {}", idRun, vie);
            infoRun.setDescrizioneStatoEsecuzione(vie.getMessage());
            gestoreRunLog.updateRun(infoRun);
            return null;
        } catch (Throwable t) {
            infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_GENERICO);
            log.error("Errore generico idRun {}", idRun, t);
            infoRun.setDescrizioneStatoEsecuzione(t.getMessage());
            gestoreRunLog.updateRun(infoRun);
            return null;
        }
        return bloccoValidazione;
    }

    public String puliziaFileDataroot(String nomeFileTmp, String nomeFile, String nomeFileXSD) {
        try (FileReader fr = getFileReaderFromNomeFile(nomeFileTmp);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(nomeFile);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {
            bw.write(XML_VERSION_1_0_ENCODING_UTF_8);
            bw.newLine();
            bw.flush();
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.equals("</dataroot><dataroot>")) {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            br.close();
            fr.close();
            bw.close();
            fw.close();
            fileDelete(Paths.get(nomeFileTmp));
            boolean isFileXMLOutputCleaned = this.formatXmlOutput(nomeFile, nomeFileTmp, nomeFileXSD);

            if (!isFileXMLOutputCleaned) {
                log.warn("ATTENZIONE: Un'operazione tra copia, rename o eliminazione del file temp XML OUTPUT non è andata a buon fine.");
                throw new XSDNonSupportedException();
            }
            return nomeFile;

        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String puliziaFileSism(String nomeFileTmp, String nomeFile, String xmlTag) {
        try (FileReader fr = getFileReaderFromNomeFile(nomeFileTmp);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(nomeFile);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {
            bw.write(XML_VERSION_1_0_ENCODING_UTF_8);
            bw.newLine();
            bw.flush();
            String linea;
            boolean annoRiferimentoCheck = false;
            boolean periodoRiferimentoCheck = false;
            boolean codRegioneCheck = false;

            while ((linea = br.readLine()) != null) {
                if (linea.equals(xmlTag)) {

                } else if (linea.contains("<AnnoRiferimento>")) {
                    if (!annoRiferimentoCheck) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        annoRiferimentoCheck = true;
                    }
                } else if (linea.contains("<PeriodoRiferimento>")) {
                    if (!periodoRiferimentoCheck) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        periodoRiferimentoCheck = true;
                    }
                } else if (linea.contains("<CodiceRegione>")) {
                    if (!codRegioneCheck) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        codRegioneCheck = true;
                    }
                } else {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            br.close();
            fr.close();
            fileDelete(Paths.get(nomeFileTmp));

            return nomeFile;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }



    public String puliziaFileAvn(String nomeFileTmp, String nomeFile, String tagFlussoXML) {
        try (FileReader fr = getFileReaderFromNomeFile(nomeFileTmp);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(nomeFile);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {
            bw.write(XML_VERSION_1_0_ENCODING_UTF_8);
            bw.newLine();
            bw.flush();
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.contains(tagFlussoXML)){
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            br.close();
            fr.close();
            fileDelete(Paths.get(nomeFileTmp));

            return nomeFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String puliziaFileEmurPs(String nomeFileTmp, String nomeFile, String xmlTag) {
        try (FileReader fr = getFileReaderFromNomeFile(nomeFileTmp);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(nomeFile);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {
            bw.write(XML_VERSION_1_0_ENCODING_UTF_8);
            bw.newLine();
            bw.flush();
            String linea;
            boolean rootElement = false;


            while ((linea = br.readLine()) != null) {
                if (linea.equals(xmlTag)) {


                } else if (linea.contains("<ns0:flsProSoc xmlns:ns0=\"http://flussi.mds.it/flsProSoc\">")) {
                    if (!rootElement) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        rootElement = true;
                    }
                } else {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            br.close();
            fr.close();
            fileDelete(Paths.get(nomeFileTmp));


            return nomeFile;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String puliziaFileDispovig(String nomeFileTmp, String nomeFile, String xmlTag) {
        try (FileReader fr = getFileReaderFromNomeFile(nomeFileTmp);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(nomeFile);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {
            bw.write(XML_VERSION_1_0_ENCODING_UTF_8);
            bw.newLine();
            bw.flush();
            String linea;
            boolean rootElement = false;


            while ((linea = br.readLine()) != null) {
                if (linea.equals(xmlTag)) {


                } else if (linea.contains("<flsDispovig>")) {
                    if (!rootElement) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        rootElement = true;
                    }
                } else {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            br.close();
            fr.close();
            fileDelete(Paths.get(nomeFileTmp));


            return nomeFile;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String puliziaFileDatarootCns(String nomeFileTmp, String nomeFile, String nomeFileXSD) {
        try (FileReader fr = getFileReaderFromNomeFile(nomeFileTmp);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(nomeFile);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.flush();
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.equals("</ns0:dataroot><ns0:dataroot>")) {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            br.close();
            fr.close();
            bw.close();
            fw.close();
            fileDelete(Paths.get(nomeFileTmp));
            boolean isFileXMLOutputCleaned = this.formatXmlOutputCns(nomeFile, nomeFileTmp, nomeFileXSD);

            if (!isFileXMLOutputCleaned) {
                log.warn("ATTENZIONE: Un'operazione tra copia, rename o eliminazione del file temp XML OUTPUT non è andata a buon fine.");
                throw new XSDNonSupportedException();
            }
            return nomeFile;

        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String puliziaFileCt2(String nomeFileTmp, String nomeFile, String nomeFileXSD) {
        try (FileReader fr = getFileReaderFromNomeFile(nomeFileTmp);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(nomeFile);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.flush();
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.equals("</<ns0:dataroot></ns0:dataroot>")) {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            br.close();
            fr.close();
            bw.close();
            fw.close();
            fileDelete(Paths.get(nomeFileTmp));
            boolean isFileXMLOutputCleaned = this.formatXmlOutputCt2(nomeFile, nomeFileTmp, nomeFileXSD);

            if (!isFileXMLOutputCleaned) {
                log.warn("ATTENZIONE: Un'operazione tra copia, rename o eliminazione del file temp XML OUTPUT non è andata a buon fine.");
                throw new XSDNonSupportedException();
            }
            return nomeFile;

        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public boolean formatJsonEsiti(String nomeFile, String percorsoTemp) {

        log.info("*** Inizio Refactor file Esiti ***");
        log.debug("{}.formatJsonEsiti - nomeFile{} - nomeFileTemp{}", this.getClass().getName(), nomeFile, percorsoTemp);

        List<Boolean> resultList = new ArrayList<>();
        String linea;

        try (FileReader fr = getFileReaderFromNomeFile(nomeFile);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(percorsoTemp);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {
            bw.write("[");
            bw.newLine();
            bw.flush();


            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
                bw.flush();
            }
            bw.write("]");

            br.close();
            fr.close();
            bw.close();
            fr.close();

            File fileFinale = getFileFromPath(nomeFile);
            File fileTemp = getFileFromPath(percorsoTemp);

            if (!fileFinale.exists()) {
                log.warn("ATTENZIONE: Il file {} non esiste", Path.of(nomeFile));
                throw new IOException();
            }

            boolean eliminazioneFile = fileFinale.delete();
            boolean renamingFile = fileTemp.renameTo(Path.of(nomeFile).toFile());

            if (!eliminazioneFile) {
                log.warn("Errore durante l'eliminazione del file");
            }
            if (!renamingFile) {
                log.warn("Errore durante il renaming del file temporaneo");
            }

            resultList.add(eliminazioneFile);
            resultList.add(renamingFile);

            boolean result = !resultList.contains(Boolean.FALSE);
            log.info("*** Fine Refactor file Esiti - ESITO {} ***", (result ? "OK" : "KO"));

            return result;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public boolean formatXmlOutput(String nomeFile, String percorsoTemp, String nomeFileXSD) {

        log.info("*** Inizio Refactor XML OUTPUT ***");
        log.debug("{}.formatXmlOutput - nomeFile{} - nomeFileTemp{}", this.getClass().getName(), nomeFile, percorsoTemp);

        List<Boolean> resultList = new ArrayList<>();
        String linea;
        boolean occurrencesRegione = false;
        boolean occurrencesPeriodo = false;

        try (FileReader fr = getFileReaderFromNomeFile(nomeFile);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(percorsoTemp);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {

            while ((linea = br.readLine()) != null) {
                if (linea.contains("<REGIONE")) {
                    if (!occurrencesRegione) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        occurrencesRegione = true;
                    }
                } else if (linea.contains("<PERIODO")) {
                    if (!occurrencesPeriodo) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        occurrencesPeriodo = true;
                    }
                } else if (linea.contains("</REGIONE")) {

                } else if (linea.contains("</PERIODO")) {

                } else if (linea.contains("</dataroot")) {

                } else {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            bw.write("</PERIODO>");
            bw.newLine();
            bw.flush();
            bw.write("</REGIONE>");
            bw.newLine();
            bw.flush();
            bw.write("</dataroot>");
            bw.newLine();
            bw.flush();

            br.close();
            fr.close();
            bw.close();
            fr.close();

            File fileFinale = getFileFromPath(nomeFile);
            File fileTemp = getFileFromPath(percorsoTemp);

            if (!fileFinale.exists()) {
                log.warn("ATTENZIONE: Il file {} non esiste", Path.of(nomeFile));
                throw new IOException();
            }

            boolean eliminazioneFile = fileFinale.delete();
            boolean renamingFile = fileTemp.renameTo(Path.of(nomeFile).toFile());

            if (!eliminazioneFile) {
                log.warn("Errore durante l'eliminazione del file");
            }
            if (!renamingFile) {
                log.warn("Errore durante il renaming del file temporaneo");
            }

            resultList.add(eliminazioneFile);
            resultList.add(renamingFile);

            boolean validationResult = validateXML(nomeFile, nomeFileXSD);

            resultList.add(validationResult);

            boolean result = !resultList.contains(Boolean.FALSE);
            log.info("*** Fine Refactor XML OUTPUT - ESITO {} ***", (result ? "OK" : "KO"));
            return result;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }


    public boolean formatXmlOutputCns(String nomeFile, String percorsoTemp, String nomeFileXSD) {

        log.info("*** Inizio Refactor XML OUTPUT ***");
        log.debug("{}.formatXmlOutput - nomeFile{} - nomeFileTemp{}", this.getClass().getName(), nomeFile, percorsoTemp);

        List<Boolean> resultList = new ArrayList<>();
        String linea;
        boolean occurrencesRegione = false;
        boolean occurrencesPeriodo = false;

        try (FileReader fr = getFileReaderFromNomeFile(nomeFile);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(percorsoTemp);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {

            while ((linea = br.readLine()) != null) {
                if (linea.contains("<ns0:REGIONE")) {
                    if (!occurrencesRegione) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        occurrencesRegione = true;
                    }
                } else if (linea.contains("<ns0:PERIODO")) {
                    if (!occurrencesPeriodo) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        occurrencesPeriodo = true;
                    }
                } else if (linea.contains("</ns0:REGIONE")) {

                } else if (linea.contains("</ns0:PERIODO")) {

                } else if (linea.contains("</ns0:dataroot")) {

                } else {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            bw.write("      </ns0:PERIODO>");
            bw.newLine();
            bw.flush();
            bw.write("   </ns0:REGIONE>");
            bw.newLine();
            bw.flush();
            bw.write("</ns0:dataroot>");
            bw.newLine();
            bw.flush();

            br.close();
            fr.close();
            bw.close();
            fr.close();

            File fileFinale = getFileFromPath(nomeFile);
            File fileTemp = getFileFromPath(percorsoTemp);

            if (!fileFinale.exists()) {
                log.warn("ATTENZIONE: Il file {} non esiste", Path.of(nomeFile));
                throw new IOException();
            }

            boolean eliminazioneFile = fileFinale.delete();
            boolean renamingFile = fileTemp.renameTo(Path.of(nomeFile).toFile());

            if (!eliminazioneFile) {
                log.warn("Errore durante l'eliminazione del file");
            }
            if (!renamingFile) {
                log.warn("Errore durante il renaming del file temporaneo");
            }

            resultList.add(eliminazioneFile);
            resultList.add(renamingFile);

            boolean validationResult = validateXML(nomeFile, nomeFileXSD);

            resultList.add(validationResult);

            boolean result = !resultList.contains(Boolean.FALSE);
            log.info("*** Fine Refactor XML OUTPUT - ESITO {} ***", (result ? "OK" : "KO"));
            return result;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    public boolean formatXmlOutputCt2(String nomeFile, String percorsoTemp, String nomeFileXSD) {

        log.info("*** Inizio Refactor XML OUTPUT ***");
        log.debug("{}.formatXmlOutput - nomeFile{} - nomeFileTemp{}", this.getClass().getName(), nomeFile, percorsoTemp);

        List<Boolean> resultList = new ArrayList<>();
        String linea;
        boolean occurrencesRegione = false;

        try (FileReader fr = getFileReaderFromNomeFile(nomeFile);
             BufferedReader br = getBufferedReaderFromFileReader(fr);
             FileWriter fw = getFileWriterFromNomeFile(percorsoTemp);
             BufferedWriter bw = getBufferedWriterFromFileWriter(fw)
        ) {

            while ((linea = br.readLine()) != null) {
                if (linea.contains("<ns0:REGIONE")) {
                    if (!occurrencesRegione) {
                        bw.write(linea);
                        bw.newLine();
                        bw.flush();
                        occurrencesRegione = true;
                    }
                } else if (linea.contains("</ns0:REGIONE")) {

                } else if (linea.contains("</ns0:dataroot>")) {

                } else {
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                }
            }
            bw.write("</ns0:REGIONE>");
            bw.newLine();
            bw.flush();
            bw.write("</ns0:dataroot>");
            bw.newLine();
            bw.flush();

            br.close();
            fr.close();
            bw.close();
            fr.close();

            File fileFinale = getFileFromPath(nomeFile);
            File fileTemp = getFileFromPath(percorsoTemp);

            if (!fileFinale.exists()) {
                log.warn("ATTENZIONE: Il file {} non esiste", Path.of(nomeFile));
                throw new IOException();
            }

            boolean eliminazioneFile = fileFinale.delete();
            boolean renamingFile = fileTemp.renameTo(Path.of(nomeFile).toFile());

            if (!eliminazioneFile) {
                log.warn("Errore durante l'eliminazione del file");
            }
            if (!renamingFile) {
                log.warn("Errore durante il renaming del file temporaneo");
            }

            resultList.add(eliminazioneFile);
            resultList.add(renamingFile);

            boolean validationResult = validateXML(nomeFile, nomeFileXSD);

            resultList.add(validationResult);

            boolean result = !resultList.contains(Boolean.FALSE);
            log.info("*** Fine Refactor XML OUTPUT - ESITO {} ***", (result ? "OK" : "KO"));
            return result;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    public boolean validateXML(String nomeFile, String NOME_FILE_XSD) {
        MainTester mt = getMainTester();

        InputStream avnXSD = getInputStreamFromNomeFile(NOME_FILE_XSD);

        if (Objects.isNull(avnXSD)) {
            log.error("InputStream del file {}", NOME_FILE_XSD);
            throw new RuntimeException();
        }

        File xmlFile = getFileFromPath(nomeFile);

        if (!xmlFile.exists()) {
            log.error("File {} non trovato", xmlFile);
            throw new RuntimeException();
        }
        boolean validationResult = mt.xmlValidationAgainstXSD(xmlFile, avnXSD);

        if (validationResult) {
            log.info("Validazione XMLOutput {} vs XSD {} - OK", nomeFile, NOME_FILE_XSD);
        } else {
            log.info("Validazione XMLOutput {} vs XSD {} - KO", nomeFile, NOME_FILE_XSD);
        }
        return validationResult;
    }

    public InputStream getInputStreamFromNomeFile(String NOME_FILE_XSD) {
        return getClass().getClassLoader().getResourceAsStream(NOME_FILE_XSD);
    }

    public MainTester getMainTester() {
        return new MainTester();
    }

    public BloccoValidazione getBloccoValidazione() {
        return new BloccoValidazione();
    }

    public File getFileFromPath(String nomeFile) {
        return Path.of(nomeFile).toFile();
    }

    public ValidazioneFlusso getValidazioneFlusso() {
        return new ValidazioneFlusso();
    }

    public ErroreValidazione getErroreValidazioneFromCodiceEDescrizione(String codice, String descrizione) {
        return new ErroreValidazione(codice, descrizione);
    }

    public Esito createEsito(String campo, String scarto, boolean esito, List<ErroreValidazione> errorList) {
        return new Esito(campo, scarto, esito, errorList);
    }

    public GestoreEsiti createGestoreEsitiFromGestoreFile(GestoreFile gestoreFile) {
        return new GestoreEsiti(gestoreFile);
    }

    public GestoreRunLog createGestoreRunLog(GestoreFile gestoreFile, Progressivo creaProgressivo) {
        return new GestoreRunLog(gestoreFile, creaProgressivo);
    }

    public void fileDelete(Path path) throws IOException {
        Files.delete(path);
    }

    public BufferedWriter getBufferedWriterFromFileWriter(FileWriter fw) {
        return new BufferedWriter(fw);
    }

    public FileWriter getFileWriterFromNomeFile(String nomeFile) throws IOException {
        return new FileWriter(nomeFile);
    }

    public BufferedReader getBufferedReaderFromFileReader(FileReader fr) {
        return new BufferedReader(fr);
    }

    public FileReader getFileReaderFromNomeFile(String nomeFileTmp) throws FileNotFoundException {
        return new FileReader(nomeFileTmp);
    }
}

