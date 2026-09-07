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
import java.util.List;

/**
 * Serves the Castle browser SDK from the npm install. 2.x ships
 * castle.browser.js; 3.x ships castle.umd.js. The HTML always requests
 * castle.umd.js.
 */
@Controller
public class CastleJsController {

    private static final Path DIST = Path.of("node_modules", "@castleio", "castle-js", "dist")
            .toAbsolutePath().normalize();

    @GetMapping("/vendor/castle-js/{filename}")
    public ResponseEntity<Resource> castleJs(@PathVariable String filename) {
        Path file = resolve(filename);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/javascript"))
                .body(new FileSystemResource(file));
    }

    static Path resolve(String filename) {
        List<String> names = switch (filename) {
            case "castle.umd.js" -> List.of("castle.umd.js", "castle.browser.js");
            case "castle.browser.js" -> List.of("castle.browser.js", "castle.umd.js");
            default -> List.of(filename);
        };
        for (String name : names) {
            Path candidate = DIST.resolve(name).normalize();
            if (!candidate.startsWith(DIST) || !Files.isRegularFile(candidate)) {
                continue;
            }
            return candidate;
        }
        return null;
    }
}
