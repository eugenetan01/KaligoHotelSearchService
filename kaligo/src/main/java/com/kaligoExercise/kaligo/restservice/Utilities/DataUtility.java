package com.kaligoExercise.kaligo.restservice.Utilities;

import com.kaligoExercise.kaligo.restservice.JSONClasses.*;
import org.springframework.lang.Nullable;

import java.util.*;

public class DataUtility {
    public static Supplier1[] supp1;
    public static Supplier2[] supp2;
    public static Supplier3[] supp3;
    public static ArrayList<String> amenitiesFacilitiesList;
    public static ArrayList<String> amenitiesRoomList;
    public static HashMap<String, String> s3WordDictionary;
    public static HashSet<String> generalListOriginal = new HashSet<>();

    public static void setS3WordDictionary(){
        s3WordDictionary = new HashMap<>();
        s3WordDictionary.put("tub", "bathtub");
    }

    public static void setAmenitiesFacilitiesList(){
        amenitiesFacilitiesList = new ArrayList<>();
        amenitiesFacilitiesList.add("businesscenter");
        amenitiesFacilitiesList.add("childcare");
        amenitiesFacilitiesList.add("wifi");
        amenitiesFacilitiesList.add("drycleaning");
        amenitiesFacilitiesList.add("breakfast");
        amenitiesFacilitiesList.add("bar");
        amenitiesFacilitiesList.add("concierge");
        amenitiesFacilitiesList.add("indoor pool");
    }

    public static void setAmenitiesRoomList(){
        amenitiesRoomList = new ArrayList<>();
        amenitiesRoomList.add("tv");
        amenitiesRoomList.add("coffee machine");
        amenitiesRoomList.add("kettle");
        amenitiesRoomList.add("hair dryer");
        amenitiesRoomList.add("iron");
        amenitiesRoomList.add("aircon");
        amenitiesRoomList.add("minibar");
        amenitiesRoomList.add("tub");
    }

    public static String amenitiesStringFormatter(String s1, String s2){
        String str = s1.replaceAll("\\s","");
        if(str.equals(s2))return s1;
        return null;
    }

    public static ArrayList<Response> cleanDataS2(){
        ArrayList<Response> resListSupp2 = new ArrayList<>();

        for(Supplier2 s2 : supp2){
            Response res = new Response();
            res.hotel_id = s2.hotel_id;
            RespLocation respLoc = new RespLocation();
            respLoc.address = s2.location.address;
            respLoc.country = s2.location.country;
            res.location = respLoc;
            res.description = s2.details;
            res.hotel_name = s2.hotel_name;
            res.amenities = s2.amenities;
            res.booking_conditions = s2.booking_conditions;
            res.destination_id = s2.destination_id;
            Supplier2Amenities amenities = new Supplier2Amenities();
            amenities.general = s2.amenities.general;
            ArrayList<String> list = s2.amenities.general;
            generalListOriginal.addAll(list);
            amenities.room = s2.amenities.room;
            res.amenities = amenities;
            res = imageLinks(s2, null, "Supplier2",res);
            resListSupp2.add(res);
        }

        return resListSupp2;
    }

    public static Response imageLinks(@Nullable Supplier2 s2, @Nullable Supplier3 s3, String supplierName, Response resp){

        if(supplierName.equals("Supplier2") && s2!=null){

            RespImage img = new RespImage();
            ArrayList<ResponseLink> rooms = new ArrayList<>();
            ArrayList<ResponseLink> site = new ArrayList<>();
            for(Supplier2Link s2link : s2.images.rooms){
                ResponseLink rlink = new ResponseLink();
                rlink.link = s2link.link;
                rlink.description = s2link.caption;
                rooms.add(rlink);
            }
            for(Supplier2Link s2link : s2.images.site){
                ResponseLink rlink = new ResponseLink();
                rlink.link = s2link.link;
                rlink.description = s2link.caption;
                site.add(rlink);
            }
            img.rooms = rooms;
            img.site = site;
            resp.images = img;

        }else if(supplierName.equals("Supplier3") && s3!=null){
            RespImage img = resp.images;
            if(img == null){
                img = new RespImage();
            }

            ArrayList<ResponseLink> rooms = resp.images.rooms;
            if (rooms == null) {
                rooms = new ArrayList<>();
            }

            for(Supplier3Link s3link : s3.images.rooms){
                if(rooms.size()>0){
                    HashMap<String, String> linksToAAdd = new HashMap<>();
                    for(ResponseLink rlink : rooms){
                        if(!rlink.link.equals(s3link.url)) {
                            linksToAAdd.put(s3link.url, s3link.description);
                        }
                    }
                    for(String key : linksToAAdd.keySet()){
                        ResponseLink toAdd = new ResponseLink();
                        toAdd.link = key;
                        toAdd.description = linksToAAdd.get(key);
                        rooms.add(toAdd);
                    }
                }else{
                    ResponseLink toAdd = new ResponseLink();
                    toAdd.link = s3link.url;
                    toAdd.description = s3link.description;
                    rooms.add(toAdd);
                }
            }

            ArrayList<ResponseLink> amenities = new ArrayList<>();
            for(Supplier3Link s3link : s3.images.amenities){
                ResponseLink rlink = new ResponseLink();
                rlink.link = s3link.url;
                rlink.description = s3link.description;
                amenities.add(rlink);
            }

            img.rooms = rooms;
            img.amenities = amenities;
            resp.images = img;
        }

        return resp;
    }

    public static ArrayList<Response> cleanDataS1(){
        ArrayList<Response> toClean = cleanDataS2();

        for(Response s2cleaned : toClean){
            for(Supplier1 s1 : supp1){
                if(s1.Id.equals(s2cleaned.hotel_id)){
                    s2cleaned.location.lng = s1.Longitude;
                    s2cleaned.location.lat = s1.Latitude;
                    s2cleaned.location.city = s1.City;
                    if(s1.Facilities != null){
                        for(String facil : s1.Facilities){
                            String toLowerFacil = facil.toLowerCase().trim();
                            if(amenitiesFacilitiesList.contains(toLowerFacil)){
                                s2cleaned.amenities.general.add(toLowerFacil);
                            }
                            else if (amenitiesRoomList.contains(toLowerFacil)){
                                String toAdd = s3WordDictionary.get(toLowerFacil);
                                if (toAdd != null) {
                                    s2cleaned.amenities.room.add(toAdd);//s2cleaned.amenities.room[roomArray.length-1] = toLowerFacil;
                                }else{
                                    s2cleaned.amenities.room.add(toLowerFacil);
                                }
                            }
                        }
                    }
                }
                s2cleaned = cleanAmenities(s2cleaned);
            }
        }
        return toClean;
    }


    public static ArrayList<Response> cleanDataS3(){
        ArrayList<Response> toClean = cleanDataS1();

        for(Response resp : toClean){
            for(Supplier3 s3 : supp3){
                if(s3.id.equals(resp.hotel_id)){
                    if(resp.location.lng == null || resp.location.lng.isEmpty()) resp.location.lng = s3.lng;
                    if(resp.location.lat == null || resp.location.lat.isEmpty()) resp.location.lat = s3.lat;
                    imageLinks(null, s3, "Supplier3", resp);
                    if(s3.amenities!=null){
                        for(String facil : s3.amenities){
                            String toLowerFacil = facil.toLowerCase().trim();
                            if(amenitiesFacilitiesList.contains(toLowerFacil)){
                                resp.amenities.general.add(toLowerFacil);
                            }
                            else if (amenitiesRoomList.contains(toLowerFacil)){
                                String toAdd = s3WordDictionary.get(toLowerFacil);
                                if (toAdd != null) {
                                    resp.amenities.room.add(toAdd);
                                }else {
                                    resp.amenities.room.add(toLowerFacil);
                                }
                            }
                        }
                    }
                    resp = cleanAmenities(resp);
                }
            }
        }
        return toClean;
    }


    private static Response cleanAmenities(Response resp) {
        for(int i =0; i<resp.amenities.general.size(); i++){
            String general = resp.amenities.general.get(i);
            general = general.replaceAll("\\s","");
            resp.amenities.general.set(i, general);
        }
        Set<String> set = new HashSet<>(resp.amenities.general);
        resp.amenities.general.clear();
        resp.amenities.general.addAll(set);

        for(int i=0; i<resp.amenities.general.size(); i++){
            for(String s: generalListOriginal){
                String check = amenitiesStringFormatter(s,resp.amenities.general.get(i));
                if(check!=null) {
                    if (!resp.amenities.general.contains(check)) {
                        resp.amenities.general.set(i, check);
                    }
                }
            }
        }


        Set<String> setRoom = new HashSet<>(resp.amenities.room);
        resp.amenities.room.clear();
        resp.amenities.room.addAll(setRoom);
        return resp;
    }


}
