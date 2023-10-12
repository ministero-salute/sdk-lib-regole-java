package it.mds.sdk.libreriaregole.dtos;

import it.mds.sdk.gestoreesiti.modelli.AbstractSecurityModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CampiInputBean extends AbstractSecurityModel {

     String periodoRiferimentoInput;
     String annoRiferimentoInput;
     String codiceRegioneInput;

    @Builder(setterPrefix = "with")
    public CampiInputBean(String periodoRiferimentoInput,
                          String annoRiferimentoInput,
                          String codiceRegioneInput) {
        this.periodoRiferimentoInput = periodoRiferimentoInput;
        this.annoRiferimentoInput = annoRiferimentoInput;
        this.codiceRegioneInput = codiceRegioneInput;

    }
}
