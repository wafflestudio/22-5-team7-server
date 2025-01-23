package com.toyProject7.karrot.auction.controller

import com.toyProject7.karrot.auction.service.AuctionService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class AuctionWebSocketController(
    private val auctionService: AuctionService,
    private val messagingTemplate: SimpMessagingTemplate,
) {
    @MessageMapping("/chat/sendPrice")
    fun sendMessage(auctionMessage: AuctionMessage) {
        val savedMessage = auctionService.updatePrice(auctionMessage)
        messagingTemplate.convertAndSend("/topic/auction/${auctionMessage.auctionId}", savedMessage)
    }
}
