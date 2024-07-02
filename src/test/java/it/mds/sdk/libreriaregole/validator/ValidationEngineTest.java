/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.validator;

import it.mds.sdk.gestoreesiti.GestoreEsiti;
import it.mds.sdk.gestoreesiti.GestoreRunLog;
import it.mds.sdk.gestoreesiti.modelli.ErroreValidazione;
import it.mds.sdk.gestoreesiti.modelli.EsitiValidazione;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.gestoreesiti.modelli.InfoRun;
import it.mds.sdk.gestoreesiti.validazioneXSD.MainTester;
import it.mds.sdk.gestorefile.exception.XSDNonSupportedException;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.exception.ValidazioneImpossibileException;
import it.mds.sdk.libreriaregole.gestorevalidazione.BloccoValidazione;
import it.mds.sdk.libreriaregole.regole.beans.Campo;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import it.mds.sdk.libreriaregole.regole.beans.RegolaGenerica;
import it.mds.sdk.libreriaregole.regole.beans.RegoleFlusso;
import it.mds.sdk.libreriaregole.regole.catalogo.lunghezza.RegolaIntervalloLunghezza;
import it.mds.sdk.libreriaregole.regole.catalogo.obbligatorieta.RegolaObbligatorieta;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ValidationEngineTest {

    @InjectMocks
    @Spy
    private ValidationEngine validationEngine;
    private RegoleFlusso regoleFlusso;
    private List<RecordDtoGenerico> records = new ArrayList<>();
    private final BloccoValidazione bloccoValidazione = Mockito.mock(BloccoValidazione.class);
    private List<Esito> esitoList = new ArrayList<>();
    private Esito esito;
    private ErroreValidazione erroreValidazione = Mockito.mock(ErroreValidazione.class);

    private GestoreEsiti gestoreEsiti = Mockito.mock(GestoreEsiti.class);
    private GestoreRunLog gestoreRunLog = Mockito.mock(GestoreRunLog.class);
    private InfoRun infoRun = Mockito.mock(InfoRun.class);
    private RecordDtoGenerico r = Mockito.mock(RecordDtoGenerico.class);
    private EsitiValidazione esitiValidazione = Mockito.mock(EsitiValidazione.class);
    private FileReader fr = Mockito.mock(FileReader.class);
    private BufferedReader br = Mockito.mock(BufferedReader.class);
    private FileWriter fw = Mockito.mock(FileWriter.class);
    private BufferedWriter bw = Mockito.mock(BufferedWriter.class);
    private File file = Mockito.mock(File.class);

    @BeforeEach
    void initRegoleFlusso() {
        List<Campo> campi = new ArrayList<>();
        List<RegolaGenerica> regole = new ArrayList<>();
        RegolaGenerica regolaGenericaIdRecord = new RegolaObbligatorieta("idrecordobb", "001", "errore obbl", new Parametri());
        Parametri parLunghezza = new Parametri();
        parLunghezza.getParametriMap().put("minLunghezza", "5");
        parLunghezza.getParametriMap().put("maxLunghezza", "10");
        RegolaGenerica regolaLunghezza = new RegolaIntervalloLunghezza("idrecordlunghezza", "002", "errore lunghezza"
                , parLunghezza);
        regole.add(regolaGenericaIdRecord);
        regole.add(regolaLunghezza);
        Campo campo = new Campo();
        campo.setNomeCampo("idRecord");
        campo.setRegole(regole);
        campi.add(campo);

        regoleFlusso = RegoleFlusso.builder().withCampi(campi).build();

        RecordDtoGenerico recordDtoGenerico = Mockito.mock(RecordDtoGenerico.class);
        records.add(recordDtoGenerico);

        esito = new Esito("campo", "valoreScarto", true, Collections.EMPTY_LIST);
        esitoList.add(esito);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void filtroRegoleFlussoParam()  {
        List<RecordDtoGenerico> record = new ArrayList<>();

        RecordDtoProva r1 = new RecordDtoProva();
        RecordDtoProva r2 = new RecordDtoProva();
        RecordDtoProva r3 = new RecordDtoProva();
        RecordDtoProva r4 = new RecordDtoProva();
        RecordDtoProva r5 = new RecordDtoProva();

        r1.setIdRecord("100");
        r1.setTipoOperazioneContatto("I");
        r1.setCodiceStruttura("cod1");
        r1.setIdContatto(5L);
        r2.setIdRecord("100");
        r2.setTipoOperazioneContatto("I");
        r2.setCodiceStruttura("cod1");
        r2.setIdContatto(5L);
        r3.setIdRecord("100");
        r3.setTipoOperazioneContatto("C");
        r3.setCodiceStruttura("cod2");
        r3.setIdContatto(5L);
        record.add(r1);
        record.add(r2);
        record.add(r3);
        for (int i = 0; i < 100000; i++) {
            record.add(r1);
        }
        r4.setIdRecord("200");
        r4.setTipoOperazioneContatto("V");
        r4.setCodiceStruttura("cod2");
        r4.setIdContatto(6L);
        r5.setIdRecord("200");
        r5.setTipoOperazioneContatto("V");
        r5.setCodiceStruttura("cod2");
        r5.setIdContatto(6L);
        List<String> campi = new ArrayList<>();
        campi.add("idRecord");
        campi.add("tipoOperazioneContatto");
        campi.add("codiceStruttura");
        campi.add("idContatto");


        List<RecordDtoGenerico> listaExp = new ArrayList<>();
        listaExp.add(r3);

        List<RecordDtoGenerico> listActual =
                validationEngine.validaRegolaInteroFlussoGenerica(record, campi, "desc", "cod").getRecordAccettati();

        assertEquals(listaExp, listActual);

    }


    @Test
    void validaRecordTuttoValido() {
        RecordDtoProva record = new RecordDtoProva();
        record.setIdRecord("esiste");

        List<Esito> esitoActual = validationEngine.validaRecord(record, regoleFlusso);

        Assertions.assertTrue(esitoActual.isEmpty());
    }

    @Test
    void validaRecordCampoNonValido2Regole() {
        RecordDtoProva record = new RecordDtoProva();
        record.setIdRecord("");
        List<Esito> esitoExpected = new ArrayList<>();
        List<ErroreValidazione> erroriValidazione = new ArrayList<>();
        ErroreValidazione errore = new ErroreValidazione("001", "errore obbl");
        ErroreValidazione erroreLung = new ErroreValidazione("002", "errore lunghezza");
        erroriValidazione.add(errore);
        erroriValidazione.add(erroreLung);
        Esito esito = new Esito("idRecord", "", false, erroriValidazione);
        esitoExpected.add(esito);

        List<Esito> esitoActual = validationEngine.validaRecord(record, regoleFlusso);

        assertEquals(esitoExpected, esitoActual);

    }

    @Test
    void validaRecordCampoNonValido1RegolaSu2() {
        RecordDtoProva record = new RecordDtoProva();
        record.setIdRecord("tre");
        List<Esito> esitoExpected = new ArrayList<>();
        List<ErroreValidazione> erroriValidazione = new ArrayList<>();
        ErroreValidazione erroreLung = new ErroreValidazione("002", "errore lunghezza");
        erroriValidazione.add(erroreLung);
        Esito esito = new Esito("idRecord", "tre", false, erroriValidazione);
        esitoExpected.add(esito);

        List<Esito> esitoActual = validationEngine.validaRecord(record, regoleFlusso);

        assertEquals(esitoExpected, esitoActual);

    }

    @Test
    void validaFlussoBloccoTestTrue(){

       doReturn(bloccoValidazione).when(validationEngine).getBloccoValidazione();
        doReturn(esitoList).when(validationEngine).validaRecord(any(), any());
//        given(validationEngine.getBloccoValidazione()).willReturn(bloccoValidazione);
//        given(validationEngine.validaRecord(any(), any())).willReturn(esitoList);
        BloccoValidazione result = validationEngine.validaFlussoBlocco(
                records,
                regoleFlusso,
                "1",
                1
        );
        assertEquals(0, result.getNumeroRecord());
        assertEquals(0, result.getScartati());
        assertEquals(null, result.getIdRun());

        System.out.println("d");
    }

    @Test
    void validaFlussoBloccoTestFalse(){

        Esito esito = new Esito("campo", "valoreScarto", false, Collections.EMPTY_LIST);
        List<Esito> esitoList = List.of(esito);
        doReturn(bloccoValidazione).when(validationEngine).getBloccoValidazione();
        doReturn(esitoList).when(validationEngine).validaRecord(any(), any());
//        given(validationEngine.getBloccoValidazione()).willReturn(bloccoValidazione);
//        given(validationEngine.validaRecord(any(), any())).willReturn(esitoList);
        BloccoValidazione result = validationEngine.validaFlussoBlocco(
                records,
                regoleFlusso,
                "1",
                1
        );
        assertEquals(0, result.getNumeroRecord());
        assertEquals(0, result.getScartati());
        assertEquals(null, result.getIdRun());

        System.out.println("d");
    }

    @Test
    void validaFlussoBloccoConRegoleInteroFlussoTestTrue(){
        doReturn(bloccoValidazione).when(validationEngine).getBloccoValidazione();
        doReturn(esitoList).when(validationEngine).validaRecord(any(), any());
        doReturn(erroreValidazione).when(validationEngine).getErroreValidazioneFromCodiceEDescrizione(any(), any());
        doReturn(esito).when(validationEngine).createEsito(any(), any(), anyBoolean(), anyList());

        validationEngine.validaFlussoBloccoConRegoleInteroFlusso(
                records,
                regoleFlusso,
                "1",
                Collections.emptyList(),
                1
        );
        System.out.println("");
        Assertions.assertTrue(true);
    }

    @Test
    void validaFlussoBloccoConRegoleInteroFlussoTestFalse(){
        esito = new Esito("campo", "valoreScarto", false, Collections.EMPTY_LIST);
        esitoList.clear();
        esitoList.add(esito);
        doReturn(bloccoValidazione).when(validationEngine).getBloccoValidazione();
        doReturn(esitoList).when(validationEngine).validaRecord(any(), any());
        doReturn(erroreValidazione).when(validationEngine).getErroreValidazioneFromCodiceEDescrizione(any(), any());
        doReturn(esito).when(validationEngine).createEsito(any(), any(), anyBoolean(), anyList());

        validationEngine.validaFlussoBloccoConRegoleInteroFlusso(
                records,
                regoleFlusso,
                "1",
                Collections.emptyList(),
                1
        );
        System.out.println("");
        Assertions.assertTrue(true);
    }

    @Test
    void creaFileEsitiTest(){
        doReturn(gestoreEsiti).when(validationEngine).createGestoreEsitiFromGestoreFile(any());
        doNothing().when(gestoreEsiti).creaValidazioneFlusso(any(), anyList());
        validationEngine.creaFileEsiti(bloccoValidazione);
        Assertions.assertTrue(true);
    }

    @Test
    void validaSingoloRecordTest(){

        doReturn(esitoList).when(validationEngine).validaRecord(any(), any());
        doReturn(gestoreEsiti).when(validationEngine).createGestoreEsitiFromGestoreFile(any());
        doReturn(esitiValidazione).when(gestoreEsiti).creaEsitiValidazione(any(), any());
        doReturn(gestoreRunLog).when(validationEngine).createGestoreRunLog(any(), any());
        doReturn(infoRun).when(gestoreRunLog).getRun(any());
        doReturn(infoRun).when(gestoreRunLog).updateRun(any());

        validationEngine.validaSingoloRecord(r, regoleFlusso, "1");
        Assertions.assertTrue(true);
    }

    @Test
    void startValidaFlussoBloccoTestOk(){
        doReturn(bloccoValidazione).when(validationEngine).validaFlussoBlocco(anyList(), any(), any(), anyInt());
        validationEngine.startValidaFlussoBlocco(records, regoleFlusso, "1",gestoreRunLog,1 );
        Assertions.assertTrue(true);
    }

    @Test
    void startValidaFlussoBloccoTestValidazioneImpossibile(){

        doThrow(ValidazioneImpossibileException.class).when(validationEngine).validaFlussoBlocco(anyList(), any(), any(), anyInt());
        doReturn(infoRun).when(gestoreRunLog).cambiaStatoRun(any(), any());
        doReturn(infoRun).when(gestoreRunLog).updateRun(any());
        //doNothing().when(infoRun.setDescrizioneStatoEsecuzione(any()));
        var res = validationEngine.startValidaFlussoBlocco(records, regoleFlusso, "1",gestoreRunLog,1 );
        Assertions.assertTrue(true);
    }

    @Test
    void puliziaFileDatarootTestOk() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());

        validationEngine.puliziaFileDataroot("a", "b", "c");
        
        Assertions.assertTrue(true);
    }

    @Test
    void puliziaFileSismTestOk() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());

        validationEngine.puliziaFileSism("a", "b", "c");
        Assertions.assertTrue(true);
    }

    @Test
    void puliziaFileAvnTestOk() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());

        validationEngine.puliziaFileAvn("a", "b", "c");
        Assertions.assertTrue(true);
    }

    @Test
    void puliziaFileEmurPsTestOk() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());

        validationEngine.puliziaFileEmurPs("a", "b", "c");
        Assertions.assertTrue(true);
    }

    @Test
    void puliziaFileDatarootTest_XSDException() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(false).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());

        Assertions.assertThrows(
                XSDNonSupportedException.class,
                ()->validationEngine.puliziaFileDataroot("a", "b", "c")
        );
    }

    @Test
    void puliziaFileDatarootTest_IOExc() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());
        doThrow(IOException.class).when(br).readLine();

        Assertions.assertThrows(
                RuntimeException.class,
                ()->validationEngine.puliziaFileDataroot("a", "b", "c")
        );
    }

    @Test
    void puliziaFileSismTest_IOExc() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());
        doThrow(IOException.class).when(br).readLine();

        Assertions.assertThrows(
                RuntimeException.class,
                ()->validationEngine.puliziaFileSism("a", "b", "c")
        );
    }

    @Test
    void puliziaFileAvnTest_IOExc() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());
        doThrow(IOException.class).when(br).readLine();

        Assertions.assertThrows(
                RuntimeException.class,
                ()->validationEngine.puliziaFileAvn("a", "b", "c")
        );
    }

    @Test
    void puliziaFileEmurPsTest_IOExc() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());
        doThrow(IOException.class).when(br).readLine();

        Assertions.assertThrows(
                RuntimeException.class,
                ()->validationEngine.puliziaFileEmurPs("a", "b", "c")
        );
    }

    @Test
    void puliziaFileDatarootTest_FNFExc() throws IOException
    {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());
        doThrow(FileNotFoundException.class).when(br).readLine();

        Assertions.assertThrows(
                RuntimeException.class,
                ()->validationEngine.puliziaFileDataroot("a", "b", "c")
        );
    }

    @Test
    void formatJsonEsitiOk() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(file).when(validationEngine).getFileFromPath(any());
        doReturn(true).when(file).exists();
        doReturn(true).when(file).delete();
        doReturn(true).when(file).renameTo(any());
        doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());

        validationEngine.formatJsonEsiti("a", "b");
        Assertions.assertTrue(true);
    }

    @Test
    void formatJsonEsitiKO() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(file).when(validationEngine).getFileFromPath(any());
        doReturn(false).when(file).exists();

        Assertions.assertThrows(
                RuntimeException.class,
                ()->validationEngine.formatJsonEsiti("a", "b")
        );
    }

    @Test
    void formatXmlOutputOk() throws IOException {

        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(file).when(validationEngine).getFileFromPath(any());
        doReturn(true).when(validationEngine).validateXML(any(), any());
        doReturn(true).when(file).exists();
        doReturn(true).when(file).delete();
        doReturn(true).when(file).renameTo(any());
       // doReturn(true).when(validationEngine).formatXmlOutput(any(), any(), any());
        doNothing().when(validationEngine).fileDelete(any());

        assertTrue(
                validationEngine.formatXmlOutput("a", "b", "c")
        );
    }

    @Test
    void formatXmlOutputKO() throws IOException {


        doReturn(fr).when(validationEngine).getFileReaderFromNomeFile(any());
        doReturn(br).when(validationEngine).getBufferedReaderFromFileReader(any());
        doReturn(fw).when(validationEngine).getFileWriterFromNomeFile(any());
        doReturn(bw).when(validationEngine).getBufferedWriterFromFileWriter(any());
        doReturn(file).when(validationEngine).getFileFromPath(any());
        doReturn(true).when(validationEngine).validateXML(any(), any());
        doReturn(false).when(file).exists();

        assertThrows(
                RuntimeException.class,
                ()->validationEngine.formatXmlOutput("a", "b", "c")
        );
    }

    @Test
    void validateXmlTestOk()  {
        InputStream is = Mockito.mock(InputStream.class);
        MainTester mt = Mockito.mock(MainTester.class);
        doReturn(mt).when(validationEngine).getMainTester();
        doReturn(is).when(validationEngine).getInputStreamFromNomeFile(any());
        doReturn(file).when(validationEngine).getFileFromPath(any());
        doReturn(true).when(file).exists();
        doReturn(true).when(mt).xmlValidationAgainstXSD(any(), any());

        assertTrue(
            validationEngine.validateXML("a", "b")
        );
    }

    @Test
    void validateXmlTestKO()  {
        InputStream is = Mockito.mock(InputStream.class);
        MainTester mt = Mockito.mock(MainTester.class);
        doReturn(mt).when(validationEngine).getMainTester();
        doReturn(is).when(validationEngine).getInputStreamFromNomeFile(any());
        doReturn(file).when(validationEngine).getFileFromPath(any());
        doReturn(false).when(file).exists();
//        doReturn(true).when(mt).xmlValidationAgainstXSD(any(), any());

        assertThrows(
                RuntimeException.class,
                ()->validationEngine.validateXML("a", "b")
        );
    }

    @Test
    void validateXmlTestKO2()  {
        InputStream is = Mockito.mock(InputStream.class);
        MainTester mt = Mockito.mock(MainTester.class);
        doReturn(mt).when(validationEngine).getMainTester();
        doReturn(null).when(validationEngine).getInputStreamFromNomeFile(any());

        assertThrows(
                RuntimeException.class,
                ()->validationEngine.validateXML("a", "b")
        );
    }

    @Data
    public class RecordDtoProva extends RecordDtoGenerico{
        public String idRecord;
        public String tipoOperazioneContatto;
        public String codiceStruttura;
        public Long idContatto;
    }
}
