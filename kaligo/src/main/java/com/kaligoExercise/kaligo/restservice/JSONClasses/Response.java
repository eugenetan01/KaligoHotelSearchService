package com.kaligoExercise.kaligo.restservice.JSONClasses;

import java.util.ArrayList;

public class Response {
    public String hotel_id;
    public String destination_id;
    public String hotel_name;
    public RespLocation location;
    public String description;
    public Supplier2Amenities amenities;
    public RespImage images;
    public ArrayList<String> booking_conditions;
}
