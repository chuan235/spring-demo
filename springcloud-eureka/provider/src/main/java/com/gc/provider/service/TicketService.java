package com.gc.provider.service;

import org.springframework.stereotype.Service;

@Service
public class TicketService {

    public String getTicket(){
        System.out.println("8001");
        return "《复仇者联盟4》";
    }
}
