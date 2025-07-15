package com.audio.audiotranscribe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TranscriptionService {
    private final OpenAiAudioTranscriptionModel TranscriptionModel;

    public String transcribeAudio(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("audio","wav");
        file.transferTo(tempFile);
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .language("en")
                .temperature(0f)
                .build();

        FileSystemResource audioFile = new FileSystemResource(tempFile);

        AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile, options);
        AudioTranscriptionResponse response = TranscriptionModel.call(transcriptionRequest);

        tempFile.delete();
        return response.getResult().getOutput();

    }

}
