package com.julian.proyectoinmobiliaria.util;

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

    // filtro que permite numeros decimales (punto o coma, solo uno, no al principio, no dos seguidos)
    public static final InputFilter DECIMAL_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source == null) return null;
            StringBuilder builder = new StringBuilder(dest);
            builder.replace(dstart, dend, source.subSequence(start, end).toString());
            String result = builder.toString();
            // Permite solo digitos, un punto o una coma, no al principio, no dos puntos/comas seguidos, max 1 separador
            if (result.matches("^\\d+(\\.|,)?\\d*$")) {
                // Solo un punto o coma permitido
                int dots = result.length() - result.replace(".", "").length();
                int commas = result.length() - result.replace(",", "").length();
                if (dots <= 1 && commas <= 1 && (dots + commas) <= 1) {
                    return null;
                }
            }
            return "";
        }
    };
}
