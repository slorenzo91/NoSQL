package com.tallernoSQL.domicilios.controller;

import com.tallernoSQL.clases.Domicilios;
import com.tallernoSQL.clases.Personas;
import org.springframework.web.bind.annotation.*;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
            //CREO UNA INSTANCIA DE FIRESTORE
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
		// Creo una query donde la referencia tiene que ser igual a referencePersona
		Query query = domicilios.whereEqualTo("referencePersona", referencePersona);
		// Obtengo los resultados de forma asincrona
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		
		List<Domicilios> domiciliosList = new ArrayList<>();

		try {
			//A cada documento del resultado
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                if (document.exists()) {
                	//transformo el doc en objeto
                    Domicilios domicilio = document.toObject(Domicilios.class);
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

	///domicilio/obtenerPorCriterio?departamento=Montevideo
	@GetMapping("/domicilio/obtenerPorCriterio")
	public List<Domicilios> getAddressByCriteria(
		 @RequestParam(required = false) String barrio,
		 @RequestParam(required = false) String localidad,
		 @RequestParam(required = false) String departamento){
		
		List<Domicilios> domiciliosList = new ArrayList<>();
		
	    try {
	        Query domiciliosQuery = db.collection("domicilios");

	        // FILTROS
	        if (barrio != null && !barrio.isEmpty()) {
	            domiciliosQuery = domiciliosQuery.whereEqualTo("barrio", barrio);
	        }

	        if (localidad != null && !localidad.isEmpty()) {
	            domiciliosQuery = domiciliosQuery.whereEqualTo("localidad", localidad);
	        }

	        if (departamento != null && !departamento.isEmpty()) {
	            domiciliosQuery = domiciliosQuery.whereEqualTo("departamento", departamento);
	        }

	        // Hacemos la query
	        QuerySnapshot querySnapshot = domiciliosQuery.get().get();

	        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
	            
	            // Transformo cada resultado en un objeto
	            Domicilios domicilio = document.toObject(Domicilios.class);
	            
	            // Obtengo la referencia a "Persona"
	            DocumentReference docRef = (DocumentReference) document.get("referencePersona");
	            
	            DocumentSnapshot docSnapshot;
	            try {
	                docSnapshot = docRef.get().get();
	            } catch (InterruptedException | ExecutionException e) {
	                e.printStackTrace();
	                continue;
	            }
	            if (docSnapshot.exists()) {
	                domicilio.setIdPersona(docSnapshot.getId());
	                domiciliosList.add(domicilio);
	            }
	        }	
        } catch (InterruptedException | ExecutionException e) {
	        e.printStackTrace();
	    }
	    return domiciliosList;
	}

	@PostMapping("/domicilio/agregar")
	public String createAddress(@RequestBody Domicilios address) {
		
		DocumentReference docRef = db.collection("personas").document(address.getIdPersona());
		
		 try {
		        DocumentSnapshot docSnapshot = docRef.get().get();
		        // Verifico que exista la persona con el Id indicado
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
		             domicilioData.put("referencePersona", docRef);

		             // Creo un nuevo documento con el map creado
		             DocumentReference newDomicilioRef = db.collection("domicilios").document();
		             newDomicilioRef.set(domicilioData).get();

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
		        // Verifico que no exista la persona
		        if (docSnapshot.exists()) {
		            return "Ya existe una persona con la CI:" + persona.getCI();
		        }else {
		    		Map<String, Object> docData = new HashMap<>();
		    		docData.put("nombre", persona.getNombre());
		    		docData.put("apellido", persona.getApellido());
		    		docData.put("edad", persona.getEdad());

		    		// Agrego un nuevo documento con el Id y los datos
		    		DocumentReference addedDocRef = db.collection("personas").document(persona.getCI());
		    		try {
		    			addedDocRef.set(docData).get();
		    			return "Persona agregada con la CI: " + persona.getCI();
		    		} catch (InterruptedException | ExecutionException e) {
		    			e.printStackTrace();
		    			return "Error agregando a la persona: " + e.getMessage();
		    		}
		        }	
		 } catch (InterruptedException | ExecutionException e) {
		        e.printStackTrace();
		        return "Error: " + e.getMessage();
		 }
	}
}
	