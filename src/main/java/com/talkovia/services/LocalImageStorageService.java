package com.talkovia.services;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@Service
public class LocalImageStorageService {

    private static final String UPLOAD_DIR = "uploads";
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    private static final Set<String> ALLOWED_MIME = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final Tika tika = new Tika();

    public String save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Arquivo vazio");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("Arquivo muito grande");
        }

        try {
            // Ler o arquivo UMA vez
            byte[] bytes = file.getBytes();

            // Detectar MIME real (conteúdo)
            String realMime = tika.detect(bytes);

            if (!ALLOWED_MIME.contains(realMime)) {
                throw new RuntimeException("Formato de imagem inválido");
            }

            // Decodificar imagem (valida + remove payload escondido)
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));

            if (image == null) {
                throw new RuntimeException("Arquivo não é uma imagem válida");
            }

            // Criar diretório
            Files.createDirectories(Path.of(UPLOAD_DIR));

            // Nome imutável
            String filename = UUID.randomUUID() + ".webp";
            Path path = Path.of(UPLOAD_DIR, filename);

            // Reencode (EXIF removido aqui)
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
            if (!writers.hasNext()) {
                throw new RuntimeException("WebP writer não disponível");
            }

            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            // Habilita compressão
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            // Pegar tipo válido do próprio writer
            String[] compressionTypes = param.getCompressionTypes();
            if (compressionTypes != null && compressionTypes.length > 0) {
                param.setCompressionType(compressionTypes[0]);
            }
            param.setCompressionQuality(0.8f); // Ajuste

            try (OutputStream os = Files.newOutputStream(path)) {
                try (ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
                    writer.setOutput(ios);
                    writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
                }
            } finally {
                writer.dispose();
            }

            // Retornar URL pública
            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
