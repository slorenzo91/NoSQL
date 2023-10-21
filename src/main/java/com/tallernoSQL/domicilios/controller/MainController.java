package com.tallernoSQL.domicilios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.tallernoSQL.clases.Domicilios;

import jakarta.annotation.PostConstruct;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@RestController
@RequestMapping("/nosql")
public class MainController {

	private Firestore db;

    @PostConstruct
    public void init() {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("serviceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

            db = com.google.firebase.cloud.FirestoreClient.getFirestore();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@GetMapping("/domicilio/obtenerPorPersona/{idPersona}")
	public List<Domicilios> getAddress(@PathVariable("idPersona") String idPersona) {
		
		System.out.println(idPersona);
		
		DocumentReference referencePersona = db.collection("personas").document(idPersona);
		
		CollectionReference domicilios = db.collection("domicilios");
		// Create a query against the collection.
		Query query = domicilios.whereEqualTo("referencePersona", referencePersona);
		// retrieve  query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		
		List<Domicilios> domiciliosList = new ArrayList<>();

		try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                if (document.exists()) {
                    Domicilios domicilio = document.toObject(Domicilios.class);
                    System.out.println(domicilio);
                    domiciliosList.add(domicilio);
                }
            }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return domiciliosList;
	}

	@GetMapping("/domicilio/obtenerPorCriterio/{idCriterio}")
	public String getAddressByCriteria(){
		return "Obtener por criterio";
	}

	@PostMapping("/domicilio/agregar")
	public String createAddress() {
		return "Agregar domicilio";
	}

	@PostMapping("/persona/agregar")
	public String addPerson(){
		return "Agregar persona";
	}
}
	