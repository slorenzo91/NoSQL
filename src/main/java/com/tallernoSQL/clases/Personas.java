package com.tallernoSQL.clases;

public class Personas {

    private String ci;
    private String nombre;
    private String apellido;
    private int edad;

    // Default constructor (needed for Firestore)
    public Personas() {
    }

    // Constructor
    public Personas(String ci, String nombre, String apellido, int edad) {
        this.ci = ci;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    // Getters and setters
    public String getCI() {
        return ci;
    }

    public void setCI(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
	
}
