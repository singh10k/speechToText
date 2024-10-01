package com.music.in.controller;

import com.music.in.servic.SpeechToTextServic;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/speechToText")
@AllArgsConstructor
public class SpeechToTextController {
    final private SpeechToTextServic speechToTextServic;
    @PostMapping("/transcribe")
    public ResponseEntity<?> transcribeAudioFile(@RequestPart("file") MultipartFile file) {
         return speechToTextServic.transcribeAudioFile(file);
    }
}
