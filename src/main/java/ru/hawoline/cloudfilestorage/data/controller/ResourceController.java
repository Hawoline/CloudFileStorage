package ru.hawoline.cloudfilestorage.data.controller;

import io.minio.*;
import io.minio.errors.*;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hawoline.cloudfilestorage.data.PersonDetailsService;
import ru.hawoline.cloudfilestorage.data.entity.FileResponse;
import ru.hawoline.cloudfilestorage.data.entity.ResourceResponse;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

@Controller
@RequestMapping("/resource")
public class ResourceController {
    private MinioClient minioClient;
    private final String bucketName = "user-files";
    private final String rootDirectory = "user";
    private final PersonDetailsService personDetailsService;

    @Autowired
    public ResourceController(MinioClient minioClient, PersonDetailsService personDetailsService) {
        this.minioClient = minioClient;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping
    public @ResponseBody ResourceResponse getResourceInfo(@RequestParam("path") String path, Principal principal) {
        try {
            InputStream stream =
                    minioClient.getObject(GetObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(getFullPathToResource(path, principal))
                            .build());
            InputStreamResource resource = new InputStreamResource(stream);
            String name = path.substring(path.lastIndexOf("/"));
            if (name.lastIndexOf("/") == name.length() - 1) {
                return new ResourceResponse(path, name, "FOLDER");
            } else {
                return new FileResponse(path, name, "FILE", resource.contentLength());
            }

        } catch (ErrorResponseException | IOException | InvalidResponseException | NoSuchAlgorithmException |
                 ServerException | XmlParserException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam("path") String path, Principal principal) {
        try {
            minioClient.removeObject(RemoveObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(getFullPathToResource(path, principal))
                    .build());
            return ResponseEntity.ok().build();
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

    }

    private @NonNull String getFullPathToResource(String path, Principal principal) {
        return String.format("%s-%s-files/%s", rootDirectory, personDetailsService.loadUserByUsername(principal.getName()).getId(), path);
    }

    @PostMapping()
    public @ResponseBody ResourceResponse upload(@RequestParam MultipartFile file, @RequestParam("path") String path, Principal principal) {
        System.out.println(file.getOriginalFilename());
        try {
            String pathToResource = getFullPathToResource(path, principal);
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(pathToResource)
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

        if (file.getName().lastIndexOf("/") == file.getSize() - 1) {
            return new ResourceResponse(path, file.getOriginalFilename(), "FOLDER");
        } else {
            return new FileResponse(path, file.getOriginalFilename(), "FILE", file.getSize());
        }
    }

    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(@RequestParam("path") String path, Principal principal) {
        try {
            String pathInMinio = getFullPathToResource(path, principal);
            InputStream stream =
                    minioClient.getObject(GetObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(pathInMinio)
                            .build());
            InputStreamResource resource = new InputStreamResource(stream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", pathInMinio);

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