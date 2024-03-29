package com.example.socialnetwork.repository;

public class CodulLuiCezar {


    public static String criptareCezar(String text, int cheie) {
        StringBuilder rezultat = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                char ch = (char) (text.charAt(i) + cheie);

                if ((Character.isUpperCase(text.charAt(i)) && ch > 'Z') || (Character.isLowerCase(text.charAt(i)) && ch > 'z')) {
                    ch = (char) (text.charAt(i) - (26 - cheie));
                }

                rezultat.append(ch);
            } else {
                rezultat.append(text.charAt(i));
            }
        }

        return rezultat.toString();
    }


    public static String decriptareCezar(String text, int cheie) {
        return criptareCezar(text, 26 - cheie);
    }

}