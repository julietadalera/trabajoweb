package Payroll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
class UploadController {

    // Pasta onde as imagens enviadas ficam guardadas (na raiz do projeto)
    static final Path PASTA = Paths.get("uploads");

    private static final Set<String> EXTENSOES = Set.of("jpg", "jpeg", "png", "gif");

    // Receber uma imagem (201 Created) e devolver o nome com que foi salva

    @PostMapping("/upload")
    ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {

        String original = file.getOriginalFilename();
        String extensao = "";

        if (original != null && original.contains(".")) {
            extensao = original
                .substring(original.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);
        }

        if (!EXTENSOES.contains(extensao)) {
            return ResponseEntity.badRequest().body("Formato nao permitido");
        }

        Files.createDirectories(PASTA);

        // Nome aleatorio: evita nomes repetidos e caminhos maliciosos
        String nome = UUID.randomUUID() + "." + extensao;

        file.transferTo(PASTA.resolve(nome));

        return ResponseEntity.status(HttpStatus.CREATED).body(nome);
    }
}
