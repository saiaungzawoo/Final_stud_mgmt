


	package com.finalproject.Final.service;

	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Service;

	import com.finalproject.Final.repository.AnnouncementRepository;


	@Service
	public class AnnouncementScheduler {

	    private final AnnouncementRepository announcementRepo;


	    public AnnouncementScheduler(
	            AnnouncementRepository announcementRepo) {

	        this.announcementRepo = announcementRepo;
	    }


	    @Scheduled(fixedRate = 60000)
	    public void checkExpiredAnnouncements() {
	       System.out.println("CHECK EXPIRED RUNNING");

	        announcementRepo.updateExpiredAnnouncements();

	    }

	}

