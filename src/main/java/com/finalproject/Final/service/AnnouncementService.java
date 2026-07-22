package com.finalproject.Final.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.AnnouncementBean;
import com.finalproject.Final.repository.AnnouncementRepository;

@Service
public class AnnouncementService {


    @Autowired
    private AnnouncementRepository announcementRepository;


    public void update(AnnouncementBean announcement){

       // announcementRepository.update(announcement);

    }

}