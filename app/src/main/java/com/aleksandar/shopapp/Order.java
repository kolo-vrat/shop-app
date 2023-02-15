package com.aleksandar.shopapp;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private String name;
    private String address;
    private String country;
    private String phone;
    private String status;
    private Map<String, String> products;

    public Order() {}

    public Order(String name, String address, String country, String phone, String status, Map<String, String> products) {
        this.name = name;
        this.address = address;
        this.country = country;
        this.phone = phone;
        this.status = status;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getProducts() {
        return products;
    }

    public void setProducts(Map<String, String> products) {
        this.products = products;
    }

    public static Order toOrder(HashMap<String, Object> map) {
        return new Order(
                (String) map.get("name"),
                (String) map.get("address"),
                (String) map.get("country"),
                (String) map.get("phone"),
                (String) map.get("status"),
                (Map<String, String>) map.get("products")
        );
    }
}
