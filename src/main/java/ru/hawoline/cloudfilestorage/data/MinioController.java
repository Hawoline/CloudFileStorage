package ru.hawoline.cloudfilestorage.data;

import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/files")
public class MinioController {
    private MinioClient minioClient;

    @Autowired
    public MinioController(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @GetMapping("/create")
    public String createBucket() {

        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket("hello").build()
            );

        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        }

        return "hello";
    }

    @PostMapping("/upload")
    public @ResponseBody String upload(@RequestParam MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        try {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket("hello")
                    .object(file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        }

        return "name";
    }

    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(@RequestParam("path") String path) {
        try {
            InputStream stream =
                    minioClient.getObject(GetObjectArgs
                            .builder()
                            .bucket("hello")
                            .object(path)
                            .build());
            InputStreamResource resource = new InputStreamResource(stream);

            // 3. Формируем заголовки ответа
            HttpHeaders headers = new HttpHeaders();

            // Указываем тип контента как универсальный бинарный поток
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // Заставляем браузер/клиент скачивать файл, а не пытаться открыть его в веб-странице
            headers.setContentDispositionFormData("attachment", path);

            // Возвращаем ResponseEntity с потоком данных
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        }
    }
}