package com.music.in.servic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SpeechToTextServic {
    ResponseEntity<?> transcribeAudioFile(MultipartFile file);
}
