package ru.topjava.basejava.Exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message) {
        this(message, null, null);
    }

    public StorageException(String message, Exception exception) {
        this(message, null, exception);
    }

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, String uuid, Exception exception) {
        super(message, exception);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
