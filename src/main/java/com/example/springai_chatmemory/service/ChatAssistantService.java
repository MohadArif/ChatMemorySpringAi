package com.example.springai_chatmemory.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatAssistantService {

    private final ChatClient chatClient;

    public ChatAssistantService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
//
//    public String chatWithAi(String question){
//        return chatClient.prompt()
//                .user(question)
//                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,"my-conversation-id"))
//                .call()
//                .content();
//    }


    /**
     *  Now user specific conversation id is passed to the chat memory advisor
     *  This allows for multiple users to have their own conversation history with the AI assistant.
     *  here i'm using userId as the conversation id, but in a real application,
     *  you might want to use a more secure and unique identifier for each user.
     * @param question
     * @return
     */
    public String chatWithAi(String question,String userId){
        return chatClient.prompt()
                .user(question)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,userId))
                .call()
                .content();
    }
}
