package com.example.blog2.service;

import com.example.blog2.po.Message;

import java.util.List;


public interface MessageService {
    List<Message> listMessage();

    Message saveMessage(Message message);

    Message updateMessage(Long id, Message message);

    Message getMessageById(Long id);

    List<String> messageCountByMonth();

    void deleteMessage(Long id);
}