package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository <ChatMessage, Long>{
    List<ChatMessage> findByBidIdOrderByTimestampAsc(Long bidId);

}
