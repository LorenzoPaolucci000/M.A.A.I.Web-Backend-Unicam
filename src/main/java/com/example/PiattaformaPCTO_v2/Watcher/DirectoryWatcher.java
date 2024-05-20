package com.example.PiattaformaPCTO_v2.Watcher;

import com.example.PiattaformaPCTO_v2.service.SimpleProfessoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
@Component

public class DirectoryWatcher {

    private final SimpleProfessoreService fileService;

    @Autowired
    public DirectoryWatcher(SimpleProfessoreService fileService) {
        this.fileService = fileService;
        this.watchedDirectoryPath = Paths.get("src/main/resources/activity/");
    }


    private final Path watchedDirectoryPath;




    // Metodo per ottenere il percorso della directory monitorata
    public Path getWatchedDirectoryPath() {
        return watchedDirectoryPath;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        Path path = Paths.get("src/main/resources/activity/");
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            new Thread(() -> {
                while (true) {
                    WatchKey key;
                    try {
                        key = watchService.take();
                    } catch (InterruptedException ex) {
                        return;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }

                        // The filename is the context of the event.
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();

                        System.out.println("New file detected: " + fileName);

                        // Call your service to process the file
                        fileService.processNewFile(path.resolve(fileName));
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}