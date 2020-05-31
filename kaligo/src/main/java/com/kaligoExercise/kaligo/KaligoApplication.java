package com.kaligoExercise.kaligo;

import com.kaligoExercise.kaligo.restservice.Utilities.DataUtility;
import com.kaligoExercise.kaligo.restservice.Utilities.HTTPUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KaligoApplication {

	public static void main(String[] args) {
		DataUtility.setS3WordDictionary();
		DataUtility.setAmenitiesFacilitiesList();
		DataUtility.setAmenitiesRoomList();
		HTTPUtility.setURL();
		SpringApplication.run(KaligoApplication.class, args);
	}

}
