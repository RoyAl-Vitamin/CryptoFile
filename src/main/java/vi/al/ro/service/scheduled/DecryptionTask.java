package vi.al.ro.service.scheduled;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import vi.al.ro.service.cryptography.CryptographyAndCheckStatusService;

import java.io.File;

public class DecryptionTask extends Task<Void> {

    private final File inFile;

    private final File outFile;

    private final CryptographyAndCheckStatusService cryptographyService;

    private final TaskType taskType;

    private final ChangeListener<String> changeListener;

    public DecryptionTask(File inFile, File outFile, CryptographyAndCheckStatusService cryptographyService, TaskType taskType) {
        this.inFile = inFile;
        this.outFile = outFile;
        this.cryptographyService = cryptographyService;
        this.taskType = taskType;
        this.changeListener = (observableValue, oldValue, newValue) -> DecryptionTask.this.updateMessage(newValue);
    }

    @Override
    protected Void call() throws Exception {
        cryptographyService.getMessage().get().addListener(changeListener);
        switch (taskType) {
            case DECRYPT -> cryptographyService.decryptFile(inFile, outFile);
            case ENCRYPT -> cryptographyService.encryptFile(inFile, outFile);
            default -> throw new IllegalArgumentException("Не удалось установить значение параметра TaskType");
        }
        cryptographyService.getMessage().get().removeListener(changeListener);
        return null;
    }
}
