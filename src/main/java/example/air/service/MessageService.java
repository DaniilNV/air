package example.air.service;

import example.air.entity.Users;
import example.air.entity.dto.MessageDto;
import example.air.repositores.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Page<MessageDto> messageList(Pageable pageable, String filter, Users user) {
        if (filter != null && !filter.isEmpty()) {
            return messageRepository.findByTag(filter, pageable, user);
        } else {
            return messageRepository.findAll(pageable, user);
        }
    }

    public Page<MessageDto> messageListForUser(Pageable pageable, Users author) {
        return messageRepository.findByUser(pageable, author);
    }
}