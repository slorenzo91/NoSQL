package com.tallernoSQL.clases;

public class Personas {

    private String CI;
    private String Nombre;
    private String Apellido;
    private int edad;

    // Default constructor (needed for Firestore)
    public Personas() {
    }

    // Constructor
    public Personas(String CI, String Nombre, String Apellido, int edad) {
        this.CI = CI;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.edad = edad;
    }

    // Getters and setters
    public String getCI() {
        return CI;
    }

    public void setCI(String CI) {
        this.CI = CI;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
	
}
