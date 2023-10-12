package it.mds.sdk.libreriaregole.exception;

public class ValidazioneImpossibileException extends RuntimeException {

    public ValidazioneImpossibileException() { super(); }

    public ValidazioneImpossibileException(String message) {
        super(message);
    }

    public ValidazioneImpossibileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidazioneImpossibileException(Throwable cause) {
        super(cause);
    }

    protected ValidazioneImpossibileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
