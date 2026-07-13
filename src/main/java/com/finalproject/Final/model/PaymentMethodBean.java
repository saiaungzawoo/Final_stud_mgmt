package com.finalproject.Final.model;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethodBean {

	private String paymentMethodId;
	private String name;
	private String description;
	private Integer isActive;

}
