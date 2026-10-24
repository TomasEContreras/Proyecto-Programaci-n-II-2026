package com.LOTUS.jteducation.util;

/**
 * Validaciones reutilizables para los constructores y setters de las entidades.
 * Todos los métodos son estáticos: así se pueden llamar desde un constructor sin el riesgo de que una
 * subclase los sobrescriba (por eso NO hace falta poner "final" en los setters de las entidades).
 * Cada método valida y devuelve el valor ya limpio, con una única salida al final.
 */
public final class Validaciones {

    // Largo de los VARCHAR(45) de la base de datos
    public static final int TEXTO_MAX = 45;

    private Validaciones() {
        // Clase de utilidades: no se instancia
    }

    // El valor no puede ser null (relaciones obligatorias, enums, fechas)
    public static <T> T requerido(T valor, String mensaje) {
        if (valor == null) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor;
    }

    // Texto obligatorio, sin espacios sobrantes y con largo máximo. "campo" arma el mensaje de error
    public static String texto(String valor, int maximo, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        String limpio = valor.trim();
        if (limpio.length() > maximo) {
            throw new IllegalArgumentException(campo + " no puede superar los " + maximo + " caracteres.");
        }
        return limpio;
    }

    // Entero dentro de un rango (ambos extremos incluidos)
    public static int entero(int valor, int minimo, int maximo, String mensaje) {
        if (valor < minimo || valor > maximo) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor;
    }
}
