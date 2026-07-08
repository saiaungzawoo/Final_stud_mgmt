package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentBean {
	
	private int id;
	private int amount;
	private LocalDate paymentDate;
	private String paymentMethod;
	private String transactionReference;
	private String paymentStatus;
	private int receiptDownloaded;
	private int courseId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	

}	
