package com.tallernoSQL.domicilios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;

import jakarta.annotation.PostConstruct;

import java.io.*;
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
	public String getAddress() {
		
		// Crear referencia a la coleccion de domicilios
		CollectionReference domicilios = db.collection("domicilios");
		// Create a query against the collection.
		Query query = domicilios;
		// retrieve  query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		try {
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			  System.out.println(document.getData());
			  String json = new Gson().toJson(document.getData());
			  System.out.println(json);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return "Api rest responde ok :)";
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
	