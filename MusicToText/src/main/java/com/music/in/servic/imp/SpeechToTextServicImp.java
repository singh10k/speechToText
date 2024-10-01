package com.music.in.servic.imp;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import com.music.in.servic.SpeechToTextServic;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class SpeechToTextServicImp implements SpeechToTextServic{

    @Override
    public ResponseEntity<?> transcribeAudioFile(MultipartFile file) {
        try {
            // Convert the uploaded file to ByteString
            ByteString audioBytes = ByteString.copyFrom(file.getBytes());

            // Configure the recognition request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16) // Set to LINEAR16 for WAV files
                    .setSampleRateHertz(16000) // Adjust this to match your WAV file's sample rate
                    .setLanguageCode("en-US") // Set language
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Initialize Google Cloud Speech-to-Text client
            try (SpeechClient speechClient = SpeechClient.create()) {
                // Perform the transcription request
                RecognizeResponse response = speechClient.recognize(config, audio);
                List<SpeechRecognitionResult> results = response.getResultsList();

                // Process and return the transcription
                StringBuilder transcription = new StringBuilder();
                for (SpeechRecognitionResult result : results) {
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    transcription.append(alternative.getTranscript()).append("\n");
                }

                return ResponseEntity.ok("Transcription: \n" + transcription.toString());

            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error transcribing the audio file: " + e.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error reading the file: " + e.getMessage());
        }
    }
}
