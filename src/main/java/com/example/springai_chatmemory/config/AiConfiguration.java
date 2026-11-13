package com.example.springai_chatmemory.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {


    /**
     * Creating a ChatMemory bean that uses a JDBC repository to store chat messages.
     * The MessageWindowChatMemory is configured to keep a maximum of 10 messages in memory
     * This allows for contextually relevant answers to be provided to the user based on their previous interactions with the AI assistant.
     * The JdbcChatMemoryRepository is used to store the chat messages in a database, allowing for persistence across application restarts.
     * The ChatMemory bean is then used to create a MessageChatMemoryAdvisor, which is used to provide the chat memory to the Chat
     * @param jdbcChatMemoryRepository
     * @return
     */

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();
    }


    @Bean
    public ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory) {

        /**
         *  using the MessageChatMemoryAdvisor to provide the chat memory to the ChatClient.
         *  This allows the ChatClient to access the chat memory and use it to provide contextually relevant answers to the user.
         *  The MessageChatMemoryAdvisor is built using the chat memory.
         *  springai 2.0.1 does not provide default conversational id so conversational id must be provided to the chat memory advisor.
         */

        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build();

        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        you are a helpful assistant that can answer questions. 
                        You have access to the chat memory and can use it to provide contextually relevant answers.
                        """)
                .defaultAdvisors(messageChatMemoryAdvisor, new SimpleLoggerAdvisor())
                .defaultOptions(ChatOptions
                        .builder()
                        .model("nvidia/nemotron-3-ultra-550b-a55b:free")
                        .temperature(0.7)
                        .maxTokens(1000))
                .build();
    }
}
