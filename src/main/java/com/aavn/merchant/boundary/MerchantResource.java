package com.aavn.merchant.boundary;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.aavn.merchant.entity.Food;
import com.aavn.merchant.entity.Order;

@Path("/mechant/foods")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MerchantResource {

    private Set<Food> foods = new HashSet<>();
    
    private Set<Order> orders = new HashSet<>();

    public MerchantResource() {
        foods.add(new Food("f01", "Bánh tráng trộn", 3.0, 35));
        foods.add(new Food("f02", "T-ra-sua", 5.0, 40));
        foods.add(new Food("f03", "Cháo hàu sữa", 10.0, 100));
        foods.add(new Food("f04", "Cháo thịt bò", 13.0, 30));
        foods.add(new Food("f05", "Cơm tấm", 21.0, 55));
        foods.add(new Food("f06", "Sầu riêng", 15.0, 62));
    }

    @GET
    public Response getAllFood() {
        return Response.ok(foods).build();
    }
    
    @GET
    @Path("{id}")
    public Response getFood(@PathParam("id") String id) {
        Food food = foods.stream().filter(existingFood -> existingFood.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
        return Response.ok(food).build();
    }

    @PUT
    @Path("/orders")
    public Response order(@QueryParam("order") Order order) {
    	order.getOrderDetails().stream().forEach(orderDetail -> {
        	Food food = foods.stream()
        			.filter(existingFood -> existingFood.getId().equals(orderDetail.getFoodId()))
        			.findFirst()
        			.orElseThrow(NotFoundException::new);
        	if (food.getNumber() >= orderDetail.getNumber()) {
        		food.setNumber(food.getNumber() - orderDetail.getNumber());
        	}
        	throw new IllegalArgumentException();
        });
    	order.setId(UUID.randomUUID().toString());
        orders.add(order);
        return Response.ok().build();
    }

    @DELETE
    @Path("/cancel-order")
    public Response cancelOrder(@QueryParam("order") Order cancelOrder) {
        orders.removeIf(order -> order.getId().contentEquals(cancelOrder.getId()));
        
        cancelOrder.getOrderDetails().stream().forEach(orderDetail -> {
        	Food food = foods.stream()
        			.filter(existingFood -> existingFood.getId().equals(orderDetail.getFoodId()))
        			.findFirst()
        			.orElseThrow(NotFoundException::new);
        	food.setNumber(food.getNumber() + orderDetail.getNumber());
        });
        return Response.ok().build();
    }
}
