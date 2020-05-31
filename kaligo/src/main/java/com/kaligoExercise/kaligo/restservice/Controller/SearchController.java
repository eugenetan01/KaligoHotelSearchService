package com.kaligoExercise.kaligo.restservice.Controller;

import com.kaligoExercise.kaligo.restservice.JSONClasses.*;
import com.kaligoExercise.kaligo.restservice.Utilities.DataUtility;
import com.kaligoExercise.kaligo.restservice.Utilities.HTTPUtility;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SearchController {

    @GetMapping("/kaligoSearch")
    public ArrayList<Response> kaligoSearch(@RequestParam Optional<String> destinationid, @RequestParam Optional<String[]> ids) {

        for(int i = 0; i< HTTPUtility.APIEndpoints.size(); i++){
            HTTPUtility.http(HTTPUtility.getURL(i), i);
        }

        ArrayList<Response> fullResult =  DataUtility.cleanDataS3();

        ArrayList<Response> result = new ArrayList<>();
        String destid = destinationid.orElse("");
        String[] idsSearch = ids.orElse(new String[0]);

        if(idsSearch.length > 0)
            result = searchByIds(idsSearch, fullResult);
        else if (!destid.isEmpty())
            result = searchByDestinationID(destid, fullResult);

        return result;
    }

    private ArrayList<Response> searchByDestinationID(String destinationid, ArrayList<Response> resList) {
        ArrayList<Response> result = new ArrayList<>();
        for (Iterator<Response> iterator = resList.iterator(); iterator.hasNext(); ) {
            Response resp = iterator.next();
            if (resp.destination_id.equals(destinationid)) {
                result.add(resp);
            }
        }
        return result;
    }

    public ArrayList<Response> searchByIds(String[] ids, ArrayList<Response> resList){
        ArrayList<Response> result = new ArrayList<>();
        for(String id : ids) {
            for (Iterator<Response> iterator = resList.iterator(); iterator.hasNext(); ) {
                Response resp = iterator.next();
                if (resp.hotel_id.equals(id)) {
                    result.add(resp);
                }
            }
        }
        return result;
    }
}
