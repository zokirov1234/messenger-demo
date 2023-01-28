package com.messenger.service;

import com.messenger.exp.ItemNotFoundException;
import com.messenger.model.dto.attach.AttachDTO;
import com.messenger.model.entity.AttachEntity;
import com.messenger.model.enums.MessageTypes;
import com.messenger.repository.AttachRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AttachService {

    private final AttachRepository attachRepository;

    public static String attachFolder = "attaches/";


    public AttachDTO attached(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ItemNotFoundException("File is empty");
        }
        AttachDTO attachDTO = new AttachDTO();
        try {
            String pathFolder = getYmDString(); // dddd/mm/dd
            String uuid = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename());
            String fileName = uuid + "." + extension;

            File folder = new File(attachFolder + pathFolder); // attaches/2023/06/20
            if (!folder.exists()) {
                folder.mkdirs();
            }
            byte[] bytes = file.getBytes();

            Path path = Paths.get(attachFolder + pathFolder + "/" + fileName);
            Files.write(path, bytes);

            AttachEntity attach = attachRepository.save(
                    AttachEntity.builder()
                            .size(file.getSize())
                            .originalName(file.getOriginalFilename())
                            .extension(extension)
                            .path(pathFolder)
                            .build()
            );
            attachDTO.setExtension(extension);
            attachDTO.setId(attach.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attachDTO;
    }


    private String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day; // 2023/04/23
    }

    private String getExtension(String fileName) { // mp3/jpg/png/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }
}
