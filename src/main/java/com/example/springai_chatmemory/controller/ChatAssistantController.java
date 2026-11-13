package com.example.springai_chatmemory.controller;


import com.example.springai_chatmemory.service.ChatAssistantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class ChatAssistantController {


    private final ChatAssistantService chatAssistantService;

    public ChatAssistantController(ChatAssistantService chatAssistantService) {
        this.chatAssistantService = chatAssistantService;
    }

//    @GetMapping("/chat/{question}")
//    public String chatWithAi(@PathVariable("question") String question){
//        return chatAssistantService.chatWithAi(question);
//    }

    @GetMapping("/chat/{question}")
    public String chatWithAi(@PathVariable("question") String question,
                             @RequestHeader(value = "userId", required = true) String userId){
        return chatAssistantService.chatWithAi(question,userId);
    }
}
