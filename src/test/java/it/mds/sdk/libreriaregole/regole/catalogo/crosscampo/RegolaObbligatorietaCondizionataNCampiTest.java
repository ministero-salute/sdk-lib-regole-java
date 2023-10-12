package it.mds.sdk.libreriaregole.regole.catalogo.crosscampo;

import it.mds.sdk.gestoreesiti.modelli.ErroreValidazione;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.regole.beans.Parametri;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegolaObbligatorietaCondizionataNCampiTest {

    private static final String CAMPO_DA_VALIDARE = "campo";

    private static final String CAMPO_CONDIZIONANTE1 = "cond1";
    private static final String CAMPO_CONDIZIONANTE2 = "cond2";
    private static final String CAMPO_CONDIZIONANTE3 = "cond3";

    private RegolaObbligatorietaCondizionataNCampi sut;

    private RecordDtoGenerico rec;

    @BeforeEach
    void init() {
        rec = mock(RecordDtoGenerico.class);
        sut = new RegolaObbligatorietaCondizionataNCampi();
    }

    @Test
    void ifNoParamsNullValueIsOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn(null).when(rec).getCampo(CAMPO_DA_VALIDARE);
        List<Esito> esito = sut.valida(CAMPO_DA_VALIDARE, rec);

        List<ErroreValidazione> errori = Optional.of(esito).stream().flatMap(Collection::stream)
                .filter(Objects::nonNull).findFirst().map(Esito::getErroriValidazione).orElse(null);
        assertTrue(errori==null || errori.size()==0);
    }

    @Test
    void ifNoParamsBlankValueIsOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn("").when(rec).getCampo(CAMPO_DA_VALIDARE);
        List<Esito> esito = sut.valida(CAMPO_DA_VALIDARE, rec);

        List<ErroreValidazione> errori = Optional.of(esito).stream().flatMap(Collection::stream)
                .filter(Objects::nonNull).findFirst().map(Esito::getErroriValidazione).orElse(null);
        assertTrue(errori==null || errori.size()==0);
    }

    @Test
    void ifNoParamsAnyValueIsOK() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn(RandomStringUtils.random(4)).when(rec).getCampo(CAMPO_DA_VALIDARE);
        List<Esito> esito = sut.valida(CAMPO_DA_VALIDARE, rec);

        List<ErroreValidazione> errori = Optional.of(esito).stream().flatMap(Collection::stream)
                .filter(Objects::nonNull).findFirst().map(Esito::getErroriValidazione).orElse(null);
        assertTrue(errori==null || errori.size()==0);
    }

    @Test
    void ifOneOfParamIsValuedMainFieldMustBeValued() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn("").when(rec).getCampo(CAMPO_DA_VALIDARE);
        doReturn(null).when(rec).getCampo(CAMPO_CONDIZIONANTE1);
        doReturn(RandomStringUtils.random(4)).when(rec).getCampo(CAMPO_CONDIZIONANTE2);
        doReturn("").when(rec).getCampo(CAMPO_CONDIZIONANTE3);
        HashMap<String, String> param = new HashMap<>();
        param.put(RegolaObbligatorietaCondizionataNCampi.LISTA_CAMPI_PARAM, StringUtils.joinWith("|",CAMPO_CONDIZIONANTE1, CAMPO_CONDIZIONANTE2, CAMPO_CONDIZIONANTE3));
        sut.setParametri(new Parametri());
        sut.getParametri().setParametriMap(param);

        List<Esito> esito = sut.valida(CAMPO_DA_VALIDARE, rec);

        List<ErroreValidazione> errori = Optional.of(esito).stream().flatMap(Collection::stream)
                .filter(Objects::nonNull).findFirst().map(Esito::getErroriValidazione).orElse(null);
        assertTrue(errori!=null && !errori.isEmpty());
    }

    @Test
    void ifAllOfParamIsEmptyMainFieldCanBeEmpty() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn("").when(rec).getCampo(CAMPO_DA_VALIDARE);
        doReturn(null).when(rec).getCampo(CAMPO_CONDIZIONANTE1);
        doReturn("    ").when(rec).getCampo(CAMPO_CONDIZIONANTE2);
        doReturn("").when(rec).getCampo(CAMPO_CONDIZIONANTE3);
        HashMap<String, String> param = new HashMap<>();
        param.put(RegolaObbligatorietaCondizionataNCampi.LISTA_CAMPI_PARAM, StringUtils.joinWith("|",CAMPO_CONDIZIONANTE1, CAMPO_CONDIZIONANTE2, CAMPO_CONDIZIONANTE3));
        sut.setParametri(new Parametri());
        sut.getParametri().setParametriMap(param);

        List<Esito> esito = sut.valida(CAMPO_DA_VALIDARE, rec);

        List<ErroreValidazione> errori = Optional.of(esito).stream().flatMap(Collection::stream)
                .filter(Objects::nonNull).findFirst().map(Esito::getErroriValidazione).orElse(null);
        assertTrue(errori==null || errori.isEmpty());
    }

    @Test
    void ifAllOfParamIsEmptyMainFieldCanBeNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn(null).when(rec).getCampo(CAMPO_DA_VALIDARE);
        doReturn(null).when(rec).getCampo(CAMPO_CONDIZIONANTE1);
        doReturn("    ").when(rec).getCampo(CAMPO_CONDIZIONANTE2);
        doReturn("").when(rec).getCampo(CAMPO_CONDIZIONANTE3);
        HashMap<String, String> param = new HashMap<>();
        param.put(RegolaObbligatorietaCondizionataNCampi.LISTA_CAMPI_PARAM, StringUtils.joinWith("|",CAMPO_CONDIZIONANTE1, CAMPO_CONDIZIONANTE2, CAMPO_CONDIZIONANTE3));
        sut.setParametri(new Parametri());
        sut.getParametri().setParametriMap(param);

        List<Esito> esito = sut.valida(CAMPO_DA_VALIDARE, rec);

        List<ErroreValidazione> errori = Optional.of(esito).stream().flatMap(Collection::stream)
                .filter(Objects::nonNull).findFirst().map(Esito::getErroriValidazione).orElse(null);
        assertTrue(errori==null || errori.isEmpty());
    }

    @Test
    void ifAllOfParamIsEmptyMainFieldCanBeAnyValue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        doReturn(RandomStringUtils.random(4)).when(rec).getCampo(CAMPO_DA_VALIDARE);
        doReturn(null).when(rec).getCampo(CAMPO_CONDIZIONANTE1);
        doReturn("    ").when(rec).getCampo(CAMPO_CONDIZIONANTE2);
        doReturn("").when(rec).getCampo(CAMPO_CONDIZIONANTE3);
        HashMap<String, String> param = new HashMap<>();
        param.put(RegolaObbligatorietaCondizionataNCampi.LISTA_CAMPI_PARAM, StringUtils.joinWith("|",CAMPO_CONDIZIONANTE1, CAMPO_CONDIZIONANTE2, CAMPO_CONDIZIONANTE3));
        sut.setParametri(new Parametri());
        sut.getParametri().setParametriMap(param);

        List<Esito> esito = sut.valida(CAMPO_DA_VALIDARE, rec);

        List<ErroreValidazione> errori = Optional.of(esito).stream().flatMap(Collection::stream)
                .filter(Objects::nonNull).findFirst().map(Esito::getErroriValidazione).orElse(null);
        assertTrue(errori==null || errori.isEmpty());
    }
}