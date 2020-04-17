import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int resumesNumber;

    public void clear() {
        Arrays.fill(storage, 0, resumesNumber,null);
        resumesNumber = 0;
    }

    public void save(Resume r) {
        storage[resumesNumber++] = r;
    }

    public Resume get(String uuid) {
        Resume result = null;
        for (int i = 0; i < resumesNumber; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                result = storage[i];
            }
        }
        return result;
    }

    public void delete(String uuid) {
        for (int i = 0; i < resumesNumber; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                storage[i] = storage[resumesNumber - 1];
                storage[resumesNumber - 1] = null;
                resumesNumber--;
                break;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, resumesNumber);
    }

    public int size() {
        return resumesNumber;
    }
}