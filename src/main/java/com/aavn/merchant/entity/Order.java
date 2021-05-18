package com.aavn.merchant.entity;

import java.util.List;

import lombok.Data;

@Data
public class Order {

	private String id;
	
	private List<OrderDetail> orderDetails;
}
