package vn.trinhlam.jobhunter.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.trinhlam.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.trinhlam.jobhunter.service.FileService;
import vn.trinhlam.jobhunter.util.annotation.ApiMessage;
import vn.trinhlam.jobhunter.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${trinhlam.upload-file.base-uri}")
    private String baseUri;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("upload single file success")
    public ResponseEntity<ResUploadFileDTO> uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam(name = "folder", required = false) String folder)
            throws URISyntaxException, IOException, StorageException {

        // validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("file is empty. Please upload file");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (isValid == false) {
            throw new StorageException("Invalid file extension only about: " + allowedExtensions.toString());
        }
        // create a dir if not exist
        this.fileService.createUploadFolder(baseUri + folder);
        // save file
        String uploadedFile = this.fileService.store(file, folder);

        return ResponseEntity.ok().body(new ResUploadFileDTO(uploadedFile, Instant.now()));
    }
}
