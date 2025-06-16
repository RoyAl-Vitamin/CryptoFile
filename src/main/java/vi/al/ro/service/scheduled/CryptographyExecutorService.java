package vi.al.ro.service.scheduled;

import javafx.concurrent.Task;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Log4j2
public class CryptographyExecutorService {

    private final ExecutorService executor;

    private static final CryptographyExecutorService service = new CryptographyExecutorService();

    private CryptographyExecutorService() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    public Future<Void> addTask(File inFile, File outFile, ThrowingBiFunction<File, File, Void, IOException> biFunction) {
        return executor.submit(() -> biFunction.apply(inFile, outFile));
    }

    public static CryptographyExecutorService getInstance() {
        return service;
    }

    public static void shutdown() {
        service.executor.shutdown();
        log.debug("ExecutorService shutdown");
    }

    public Future<?> addTask(Task<Void> task) {
        return executor.submit(task);
    }
}
