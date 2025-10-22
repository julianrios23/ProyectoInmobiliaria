package com.julian.proyectoinmobiliaria.draweractivity.ui.perfil;

// yo creo filtros para inputs: letras y digitos

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilters {

    // filtro que permite letras, espacios y acentos (incluye ñ)
    public static final InputFilter LETTERS_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source == null) return null;
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                String s = String.valueOf(c);
                // solo permito letras (mayusculas y minusculas), acentos, ñ y espacio
                if (!s.matches("[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]")) {
                    return ""; // bloqueo el caracter
                }
            }
            return null; // acepto el input
        }
    };

    // filtro que permite solo digitos
    public static final InputFilter DIGITS_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source == null) return null;
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (!Character.isDigit(c)) {
                    return ""; // bloqueo el caracter
                }
            }
            return null; // acepto el input
        }
    };
}

