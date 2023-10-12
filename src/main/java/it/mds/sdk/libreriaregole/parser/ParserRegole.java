package it.mds.sdk.libreriaregole.parser;

import it.mds.sdk.libreriaregole.regole.beans.RegoleFlusso;

import java.io.File;

public interface ParserRegole {
    RegoleFlusso parseRegole(File regole);
}
