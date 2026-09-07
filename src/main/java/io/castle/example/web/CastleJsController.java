package io.castle.example.web;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Serves the Castle UMD build from the npm install.
 */
@Controller
public class CastleJsController {

    private static final Path DIST = Path.of("node_modules", "@castleio", "castle-js", "dist")
            .toAbsolutePath().normalize();

    @GetMapping("/vendor/castle-js/{filename}")
    public ResponseEntity<Resource> castleJs(@PathVariable String filename) {
        Path file = DIST.resolve(filename).normalize();
        if (!file.startsWith(DIST) || !Files.isRegularFile(file)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/javascript"))
                .body(new FileSystemResource(file));
    }
}
