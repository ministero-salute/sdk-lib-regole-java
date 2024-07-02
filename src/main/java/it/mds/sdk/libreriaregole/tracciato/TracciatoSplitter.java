/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.tracciato;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;

import java.nio.file.Path;
import java.util.List;

public interface TracciatoSplitter<T extends RecordDtoGenerico> {

    public List<Path> dividiTracciato(Path tracciato);

    public List<Path> dividiTracciato(List<T> records, String idRun);

}
