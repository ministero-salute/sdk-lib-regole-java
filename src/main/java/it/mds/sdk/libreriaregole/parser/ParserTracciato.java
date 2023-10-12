package it.mds.sdk.libreriaregole.parser;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import org.eclipse.persistence.sessions.Record;

import java.io.File;
import java.util.List;

public interface ParserTracciato {

    List<RecordDtoGenerico> parseTracciato(File tracciato);
    List<RecordDtoGenerico> parseTracciatoBlocco(File file, int inizio, int fine);
}
