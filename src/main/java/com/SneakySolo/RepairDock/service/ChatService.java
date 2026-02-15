package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.domain.bid.Bid;
import com.SneakySolo.RepairDock.domain.bid.BidStatus;
import com.SneakySolo.RepairDock.domain.chat.ChatMessage;
import com.SneakySolo.RepairDock.domain.user.User;
import com.SneakySolo.RepairDock.repository.BidRepository;
import com.SneakySolo.RepairDock.repository.ChatRepository;
import com.SneakySolo.RepairDock.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final BidRepository  bidRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository, BidRepository bidRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
    }

    public ChatMessage sendMessage (String message, Long bidId, Long senderId) {

        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("bid not found"));

        if (!bid.getStatus().equals(BidStatus.ACCEPTED)) {
            throw new RuntimeException("bid hasn't been accepted yet");
        }

        if (!bid.getRequest().getCustomer().getId().equals(senderId)
        && !bid.getRepairShop().getOwner().getId().equals(senderId)) {
            throw new RuntimeException("sender or service provider isn't the particular one ");
        }

        User sender = userRepository.findById(senderId).
                orElseThrow(() -> new RuntimeException("sender not found"));

        if (!sender.isEnabled()) {
            throw new RuntimeException("sender is not authorized");
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message);
        chatMessage.setSender(sender);
        chatMessage.setBid(bid);

        return chatRepository.save(chatMessage);
    }
}
