package com.kaligoExercise.kaligo.restservice.JSONClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class Supplier2 implements Serializable {

    public String hotel_id;
    public String destination_id;
    public String hotel_name;
    public Supplier2Location location;
    public String details;
    public Supplier2Amenities amenities;
    public Supplier2Images images;
    public ArrayList<String> booking_conditions;

}
