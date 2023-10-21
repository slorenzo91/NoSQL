package com.tallernoSQL.domicilios.controller;

import com.google.gson.JsonParser;
import com.tallernoSQL.clases.Domicilios;
import com.tallernoSQL.clases.Personas;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public String createAddress(@RequestBody Domicilios address) {
		
		DocumentReference docRef = db.collection("personas").document(address.getIdPersona());
		
		 try {
		        DocumentSnapshot docSnapshot = docRef.get().get();
		        
		        if (docSnapshot.exists()) {
		        	 Map<String, Object> domicilioData = new HashMap<>();
		             domicilioData.put("departamento", address.getDepartamento());
		             domicilioData.put("localidad", address.getLocalidad());
		             domicilioData.put("calle", address.getCalle());
		             domicilioData.put("apartamento", address.getApartamento());
		             domicilioData.put("padron", address.getPadron());
		             domicilioData.put("ruta", address.getRuta());
		             domicilioData.put("letra", address.getLetra());
		             domicilioData.put("barrio", address.getBarrio());
		             domicilioData.put("numero", address.getNumero());
		             domicilioData.put("referencePersona", docRef); // Adding the reference here.

		             // Create a new document in the "domicilios" collection.
		             DocumentReference newDomicilioRef = db.collection("domicilios").document();
		             newDomicilioRef.set(domicilioData).get(); // Writing the domicilio data with the reference.

		             return "Domicilio creado con referencia a la persona con CI: " + address.getIdPersona();
		             
		        }else {
		        	return "No existe una persona con la CI:" + address.getIdPersona();
		        }	
		 } catch (InterruptedException | ExecutionException e) {
		        e.printStackTrace();
		        return "Error: " + e.getMessage();
		 }
	}

	@PostMapping("/persona/agregar")
	public String addPerson(@RequestBody Personas persona) {
		
		DocumentReference docRef = db.collection("personas").document(persona.getCI());
		
		 try {
		        DocumentSnapshot docSnapshot = docRef.get().get();
		        
		        if (docSnapshot.exists()) {
		            return "Ya existe una persona con la CI:" + persona.getCI();
		        }else {
		    		// Create a map to store the values you want to set in the new document
		    		Map<String, Object> docData = new HashMap<>();
		    		docData.put("nombre", persona.getNombre());
		    		docData.put("apellido", persona.getApellido());
		    		docData.put("edad", persona.getEdad());

		    		// Add a new document with a specific ID
		    		DocumentReference addedDocRef = db.collection("personas").document(persona.getCI());

		    		// Use the set() method of the DocumentReference to save the data
		    		try {
		    			// Set the data for the specific document (overrides if already exists)
		    			addedDocRef.set(docData).get();  // Using .get() to block on the future
		    			return "Document added with ID: " + persona.getCI();
		    		} catch (InterruptedException | ExecutionException e) {
		    			e.printStackTrace();
		    			return "Error adding document: " + e.getMessage();
		    		}
		        }	
		 } catch (InterruptedException | ExecutionException e) {
		        e.printStackTrace();
		        return "Error: " + e.getMessage();
		 }
	}
}
	