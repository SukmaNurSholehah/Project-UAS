/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

/**
 *
 * @author Sukma Nur
 */
public class sesion {
   private static String username;
    private static String fullName;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        sesion.username = username;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        sesion.fullName = fullName;
    }
    
    public static void clear() {
        username = null;
        fullName = null;
    }
}
