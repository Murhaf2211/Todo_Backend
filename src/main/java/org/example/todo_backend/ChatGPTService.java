package org.example.todo_backend;

import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {
    public String correctText(String text) {
        // Make API call to OpenAI here (mocked for now)
        return "Corrected: " + text;
    }
}