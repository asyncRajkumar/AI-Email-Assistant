package com.emailwrite.AI_Assistant.service;

import com.emailwrite.AI_Assistant.model.EmailRequest;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final OllamaChatModel chatModel;

    @Autowired
    public EmailGeneratorService(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generateEmailReply(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content.Do not generate a subject line for it and do write anything else rather then it");
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
        }
        prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());

        ChatResponse response = this.chatModel.call(new Prompt(prompt.toString()));


        String generatedText = response.getResult() // Get the first Generation
                .getOutput() // Get the output (generated content)
                .getText();

        String result = generatedText.replaceAll("(?s)<think>.*?</think>", "").trim();

        return result;



//        ChatResponse response = chatModel.call(
//                new Prompt(
//                        "Generate the names of 5 famous pirates.",
//                        OllamaOptions.builder()
//                                .model("deepseek-r1:1.5b")
//                                .temperature(0.4)
//                                .build()
//                ));
//        return response.generations().get(0).assistantMessage().textContent().trim();
    }
}
