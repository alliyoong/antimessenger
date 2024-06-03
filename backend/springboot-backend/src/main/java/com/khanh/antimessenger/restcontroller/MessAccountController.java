package com.khanh.antimessenger.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.khanh.antimessenger.constant.FileConstant.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class MessAccountController {

    @GetMapping(path = "/image/{username}/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/"+DEFAULT_IMAGE_FILE_NAME, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getDefaultProfileImage() throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER  + FORWARD_SLASH + DEFAULT_IMAGE_FILE_NAME));
    }
}
