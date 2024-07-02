/* SPDX-License-Identifier: BSD-3-Clause */

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor
@XmlDiscriminatorValue("regolaCheckDigitTargatura")
public class RegolaCheckDigitTargatura extends RegolaGenerica {

    public RegolaCheckDigitTargatura(String nome, String codErrore, String desErrore, Parametri parametri) {
        super(nome, codErrore, desErrore, parametri);
    }

    /**
     * Verifica che la stringa Targatura soddisfi tutti i requisiti di controllo check digit
     * <p>
     * Se targatura è composto da dieci caratteri numerici (e quindi non negli altri casi definiti nella regola D100), allora:
     * a) sia t i primi nove caratteri (numerici) del valore del campo targatura a partire da sinistra;
     * b) iniziando dall'ultima cifra signicativa di t (quindi da destra), assegnare a ciascuna cifra, procedendo verso sinistra, alternativamente i valori 3,1,3,1,...;
     * c) sia s la somma dei prodotti tra ciascuna cifra ed il valore rispettivamente assegnato;
     * d) il check-digit è uguale a 10 - (s mod 10);
     * e) il record è valido se il campo targatura ha come numero più a destra (quindi il decimo carattere) il valore del check-digit calcolato al punto precedente.
     *
     * @param nomeCampo         campo da validare
     * @param recordDtoGenerico DTO del record del flusso
     * @return lista di {@link it.mds.sdk.gestoreesiti.modelli.Esito}
     */

    @Override
    public List<Esito> valida(String nomeCampo, RecordDtoGenerico recordDtoGenerico) {

        log.debug("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}] -  BEGIN",
                this.getClass().getName(), nomeCampo, recordDtoGenerico.toString());

        try {
            String targatura = (String) recordDtoGenerico.getCampo(nomeCampo);

            // Verifico che la targatura sia non nulla, di lunghezza 10 e che l'ultima cifra sia un numerico
            if (targatura != null && targatura.length() == 10 && isNumerico(targatura)) {

                int somma = sommaTargaturaPerParametri(targatura);
                int checkDigit = 10 - (somma % 10);
                int decimoCarattere = Integer.parseInt(targatura.substring(9, 10));

                if ((checkDigit % 10) == decimoCarattere) {
                    return List.of(creaEsitoOk(nomeCampo));
                } else {
                    return List.of(creaEsitoKO(nomeCampo, this.getCodErrore(), this.getDesErrore()));
                }
            } else {
                return List.of(creaEsitoOk(nomeCampo));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("{}.valida - nomeCampo[{}] - recordDtoGenerico[{}]",
                    this.getClass().getName(), nomeCampo, recordDtoGenerico, e);
            throw new ValidazioneImpossibileException("Impossibile validare la RegolaCheckDigitTargatura per il campo " + nomeCampo);
        }
    }

    // Verifica che l'ultimo carattere di targatura sia un numerico
    private boolean isNumerico(String targatura) {
        try {
            String decimoCarattere = targatura.substring(9, 10);
            Pattern pattern = Pattern.compile("\\b[0-9]\\b");
            Matcher matcher = pattern.matcher(decimoCarattere);
            if (matcher.matches()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Somma tutti i valori di targatura moltiplicandoli prima con dei parametri
    private int sommaTargaturaPerParametri(String targatura) {
        try {
            int valore9 = Integer.parseInt(targatura.substring(8, 9)) * 3;
            int valore8 = Integer.parseInt(targatura.substring(7, 8));
            int valore7 = Integer.parseInt(targatura.substring(6, 7)) * 3;
            int valore6 = Integer.parseInt(targatura.substring(5, 6));
            int valore5 = Integer.parseInt(targatura.substring(4, 5)) * 3;
            int valore4 = Integer.parseInt(targatura.substring(3, 4));
            int valore3 = Integer.parseInt(targatura.substring(2, 3)) * 3;
            int valore2 = Integer.parseInt(targatura.substring(1, 2));
            int valore1 = Integer.parseInt(targatura.substring(0, 1)) * 3;

            int somma = valore9 + valore8 + valore7 + valore6 + valore5 + valore4 + valore3 + valore2 + valore1;

            return somma;

        } catch (Exception e) {
            log.error("{} : valore di targatura non numerico: {}",
                    this.getClass().getName(), targatura, e);
            return 0;
        }
    }

}
