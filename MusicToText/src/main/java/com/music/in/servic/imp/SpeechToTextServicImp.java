package com.music.in.servic.imp;

import com.music.in.servic.SpeechToTextServic;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.IOException;
import java.io.InputStream;

@Service
public class SpeechToTextServicImp implements SpeechToTextServic{
    @Value("${models.path}")
    private String MODEL_PATH;
    @Override
    public ResponseEntity<?> transcribeAudioFile(MultipartFile file) {
        try {
            // Load the Vosk model (ensure the model is available at the specified path)
            Model model = new Model(MODEL_PATH);

            // Prepare the recognizer with the model and sample rate
            InputStream inputStream = file.getInputStream();
            Recognizer recognizer = new Recognizer(model, 16000); // Assuming sample rate of 16000 Hz

            // Transcribe the audio file
            int nbytes;
            byte[] buffer = new byte[4096]; // Buffer for reading the audio stream
            StringBuilder transcription = new StringBuilder();
            while ((nbytes = inputStream.read(buffer)) >= 0) {
                if (recognizer.acceptWaveForm(buffer, nbytes)) {
                    // Get the full result as JSON and extract the "text" field
                    transcription.append(recognizer.getResult()).append("\n");
                } else {
                    // Get the partial result and append it
                    transcription.append(recognizer.getResult()).append("\n");
                }
            }

            // Append the final result
            transcription.append(recognizer.getFinalResult());
            // Return the transcription
            return ResponseEntity.ok("Transcription: \n" + transcription.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the audio file: " + e.getMessage());
        }
    }
}
