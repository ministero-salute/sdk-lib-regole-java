package it.mds.sdk.libreriaregole.regole.catalogo;

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

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaVerificaCodiceFiscale")
public class RegolaVerificaCodiceFiscale extends RegolaGenerica {


    public RegolaVerificaCodiceFiscale(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che la stringa Codice Fiscale soddisfi tutti i requisiti di controllo
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {


        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] -  BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        List<Esito> listaEsiti = new ArrayList<>();
        try {
            String codiceFiscale = (String) recordDtoGenerico.getCampo(nomeCampo);
            String parametroDataNascita = this.getParametri().getParametriMap().get("parametroDataNascita");
            String dataNascita = (String) recordDtoGenerico.getCampo(parametroDataNascita);

            if ((codiceFiscale == null) || (codiceFiscale.isBlank()) || (verificaDataNascita(dataNascita, codiceFiscale) && verificaCarattereDiControllo(codiceFiscale))) {
                listaEsiti.add(creaEsitoOk(nomeCampo));
            } else {
                listaEsiti.add(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la regola VerificaCodiceFiscale per il campo " + nomeCampo);
        }
        return listaEsiti;

    }

    // verifica la coerenza con la data di nascita
    private boolean verificaDataNascita(String dataNascita, String codiceFiscale) {
        try {
            codiceFiscale = sostituisciNumeriOmocodia(codiceFiscale.toUpperCase());
            String codiceAnno = dataNascita.substring(2, 4);
            String codiceMese = calcolaCodiceMese(Integer.parseInt(dataNascita.substring(5, 7)));
            String codiceGiornoESesso = dataNascita.substring(8);
            String codiceDataNascita = codiceAnno + codiceMese + codiceGiornoESesso;
            String subCodice = (codiceFiscale.substring(6, 9) + (Integer.parseInt(codiceFiscale.substring(9, 10)) % 4)
                    + codiceFiscale.charAt(10));
            return subCodice.equals(codiceDataNascita);
        } catch (Exception e) {
            return false;
        }
    }

    // verifica la correttezza dell'ultimo carattere
    private boolean verificaCarattereDiControllo(String codiceFiscale) {
        try {
            codiceFiscale = codiceFiscale.toUpperCase();
            char originalCD = codiceFiscale.charAt(15);
            codiceFiscale = codiceFiscale.substring(0, 15);
            String pari = getStringaPari(codiceFiscale);
            String dispari = getStringaDispari(codiceFiscale);
            int sommaDispari = conversioneCaratteriDispari(dispari);
            int sommaPari = conversioneCaratteriPari(pari);
            int somma = sommaDispari + sommaPari;
            int resto = somma % 26;
            char restoConvertito = conversioneResto(resto);
            return restoConvertito == originalCD;
        } catch (Exception e) {
            return false;
        }
    }

    private String calcolaCodiceMese(int mese) {
        String risultato;
        switch (mese) {
            case 1:
                risultato = "A";
                break;
            case 2:
                risultato = "B";
                break;
            case 3:
                risultato = "C";
                break;
            case 4:
                risultato = "D";
                break;
            case 5:
                risultato = "E";
                break;
            case 6:
                risultato = "H";
                break;
            case 7:
                risultato = "L";
                break;
            case 8:
                risultato = "M";
                break;
            case 9:
                risultato = "P";
                break;
            case 10:
                risultato = "R";
                break;
            case 11:
                risultato = "S";
                break;
            case 12:
                risultato = "T";
                break;
            default:
                risultato = "";
                break;
        }
        return risultato;
    }

    private int conversioneCaratteriDispari(String string) {
        int risultato = 0;
        for (int i = 0; i < string.length(); i++) {
            char carattere = string.charAt(i);
            switch (carattere) {
                case '0':
                case 'A':
                    risultato += 1;
                    break;
                case '1':
                case 'B':
                    risultato += 0;
                    break;
                case '2':
                case 'C':
                    risultato += 5;
                    break;
                case '3':
                case 'D':
                    risultato += 7;
                    break;
                case '4':
                case 'E':
                    risultato += 9;
                    break;
                case '5':
                case 'F':
                    risultato += 13;
                    break;
                case '6':
                case 'G':
                    risultato += 15;
                    break;
                case '7':
                case 'H':
                    risultato += 17;
                    break;
                case '8':
                case 'I':
                    risultato += 19;
                    break;
                case '9':
                case 'J':
                    risultato += 21;
                    break;
                case 'K':
                    risultato += 2;
                    break;
                case 'L':
                    risultato += 4;
                    break;
                case 'M':
                    risultato += 18;
                    break;
                case 'N':
                    risultato += 20;
                    break;
                case 'O':
                    risultato += 11;
                    break;
                case 'P':
                    risultato += 3;
                    break;
                case 'Q':
                    risultato += 6;
                    break;
                case 'R':
                    risultato += 8;
                    break;
                case 'S':
                    risultato += 12;
                    break;
                case 'T':
                    risultato += 14;
                    break;
                case 'U':
                    risultato += 16;
                    break;
                case 'V':
                    risultato += 10;
                    break;
                case 'W':
                    risultato += 22;
                    break;
                case 'X':
                    risultato += 25;
                    break;
                case 'Y':
                    risultato += 24;
                    break;
                case 'Z':
                    risultato += 23;
                    break;
                default: 
                	break;
            }
        }
        return risultato;
    }

    private int conversioneCaratteriPari(String string) {
        int risultato = 0;
        for (int i = 0; i < string.length(); i++) {
            char carattere = string.charAt(i);
            int numero = Character.getNumericValue(carattere);
            if (Character.isLetter(carattere)) {
                numero = carattere - 65;
                risultato += numero;
            } else {
                risultato += numero;
            }
        }
        return risultato;
    }

    private char conversioneResto(int resto) {
        return (char) (resto + 65);
    }

    private String getStringaPari(String string) {
        String risultato = "";
        for (int i = 0; i < string.length(); i++) {
            if ((i + 1) % 2 == 0) {
                risultato += Character.toString(string.charAt(i));
            }
        }
        return risultato;
    }

    private String getStringaDispari(String string) {
        String risultato = "";
        for (int i = 0; i < string.length(); i++) {
            if ((i + 1) % 2 == 1) {
                risultato += Character.toString(string.charAt(i));
            }
        }
        return risultato;
    }

    private String sostituisciNumeriOmocodia(String codiceFiscale) {
        List<Integer> indiciOmocodia = Arrays.asList(6, 7, 9, 10, 12, 13, 14);
        StringBuilder sb = new StringBuilder();
        char chiave;
        for (int i = 0; i < codiceFiscale.length(); i++) {
            chiave = codiceFiscale.charAt(i);
            if (indiciOmocodia.contains(i)) {
                chiave = codiceFiscale.charAt(i);
                switch (chiave) {
                    case 'L':
                        chiave = '0';
                        break;
                    case 'M':
                        chiave = '1';
                        break;
                    case 'N':
                        chiave = '2';
                        break;
                    case 'P':
                        chiave = '3';
                        break;
                    case 'Q':
                        chiave = '4';
                        break;
                    case 'R':
                        chiave = '5';
                        break;
                    case 'S':
                        chiave = '6';
                        break;
                    case 'T':
                        chiave = '7';
                        break;
                    case 'U':
                        chiave = '8';
                        break;
                    case 'V':
                        chiave = '9';
                        break;
                    default : 
                        break;
                }

            }
            sb.append(chiave);
        }
        return sb.toString();
    }

}
