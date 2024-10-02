package onyx.challenge.application.port.outbound;

public interface  FileStorage {
    String store(byte[] data, String storedFilename);
    byte[] load(String storedFilename);
    void delete(String storedFilename);
}
