package com.aavn.merchant.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Food {

	private String id;
	
	private String name;
	
	private double price;
	
	private int number;
}
