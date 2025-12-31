package com.hotel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------
    //Preus d'habitació
    public static final float precioHabitacionEstandar = 50;
    public static final float precioHabitacionSuite = 100;
    public static final float precioHabitacionDeluxe = 150;

    //Preus serveis
    
    public static final float servicioDesayuno = 10;
    public static final float servicioGimnasio = 15;
    public static final float servicioSpa = 20;
    public static final float servicioPiscina = 25;

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");
            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("Eixint del sistema... Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Llistar reserves per tipus");
        System.out.println("5. Obtindre una reserva");
        System.out.println("6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
       switch (opcio) {
            case 1:
                reservarHabitacio();
                break;
            case 2:
                alliberarHabitacio();
                break;
            case 3:
                consultarDisponibilitat();
                break;
            case 4:
                obtindreReservaPerTipus();
                break;
            case 5:
                obtindreReserva();
                break;
            case 6:
                System.out.print("Gracies per usar el nostre servei de reserves");
                break;
            default:
                System.out.println("Opció no vàlida");

        }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
           String habitacionSeleccionada = seleccionarTipusHabitacioDisponible();
        ArrayList<String> serviciosSeleccionados = seleccionarServeis();
        Float precioTotal = calcularPreuTotal(habitacionSeleccionada, serviciosSeleccionados);
        int codigoReserva = generarCodiReserva();

        ArrayList<String> datosReserva = new ArrayList<>();
        datosReserva.add(habitacionSeleccionada);
        datosReserva.add(precioTotal.toString());
        datosReserva.addAll(serviciosSeleccionados);

        reserves.put(codigoReserva, datosReserva);
     
        Integer disponibilidad = disponibilitatHabitacions.get(habitacionSeleccionada);
        disponibilidad --;
        disponibilitatHabitacions.put(habitacionSeleccionada, disponibilidad);
        System.out.println("¡¡Reserva realizada con éxito!!\n Codi de reserva: " + codigoReserva);
    
    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus.
     */
    public static String seleccionarTipusHabitacio() {
        System.out.println("\nSeleccione tipo de habitación: ");
        System.out.println("1. Estándar  - " + Float.toString(precioHabitacionEstandar) + " euros. \n2. Suite  - " + Float.toString(precioHabitacionSuite) + " euros. \n3. Deluxe  - " + Float.toString(precioHabitacionDeluxe) + " euros. ");
       
        Integer seleccion = sc.nextInt();
        switch (seleccion) {
            case 1:
                return TIPUS_ESTANDARD;
            case 2:
                return TIPUS_SUITE;
            case 3:
                return TIPUS_DELUXE;
            default:
                return null;
        }
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        System.out.println("\nTipos de habitación disponibles:");
        for (Map.Entry<String, Integer> fila : disponibilitatHabitacions.entrySet()) {
            String llave = fila.getKey();
            Integer valor = fila.getValue();
            Float precioHabitacion = preusHabitacions.get(llave);
            System.out.println("Hay " + valor + " habitaciones " + llave + " disponibles - " + precioHabitacion + " euros.");

        }

        String habitacionSeleccionada = seleccionarTipusHabitacio();
        if (habitacionSeleccionada != null && disponibilitatHabitacions.get(habitacionSeleccionada) > 0) {
            return habitacionSeleccionada;
        } else {
            return null;
        }
    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        ArrayList<String> resultadoFinalServicios = new ArrayList<>();
        int opcionElegidaUsuario;
                
        do {
            System.out.println("\nSeleccione, si lo desea, alguno de nuestros servicios adicionales: ");
            System.out.println("1. Desayuno - " + servicioDesayuno + " euros. \n2. Gimnasio - " + servicioGimnasio + " euros. \n3. Spa - " + servicioSpa + " euros. \n4. Piscina - " + servicioPiscina + " euros. \n5. No deseo añadir ningún servicio adicional.");
            opcionElegidaUsuario = sc.nextInt();
            String resultado;
            switch (opcionElegidaUsuario) {
                case 1:
                    resultado = SERVEI_ESMORZAR;
                    break;
                case 2:
                    resultado = SERVEI_GIMNAS;
                    break;
                case 3:
                    resultado = SERVEI_SPA;
                    break;
                case 4:
                    resultado = SERVEI_PISCINA;
                    break;
                case 5:
                    resultado = "cap servei";
                    break;
                default:
                    resultado = null;
            }

            if (opcionElegidaUsuario == 5) {
                System.out.println("Selección de servicios finalizada.\n");

            } else if (resultado == null) {
                System.out.println("Esa opción no es válida. Por favor, seleccione una opción del 1 al 5.");

            } else if (!resultadoFinalServicios.contains(resultado)) {
                System.out.println("El servicio " + resultado + " ha sido añadido.");
                resultadoFinalServicios.add(resultado);

            } else {
                System.out.println("Este servicio ya ha sido seleccionado.");
            }

        } while (opcionElegidaUsuario != 5);
        return resultadoFinalServicios;

    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        System.out.println("Calculando precio total:");
        Float precioFinal = preusHabitacions.get(tipusHabitacio);
        System.out.println("El precio de la habitación es " + precioFinal + " euros.\n");
        if (serveisSeleccionats.size() > 0 ){
            System.out.println("Precio servicios añadidos: ");
            for (String servicio : serveisSeleccionats) {
                Float precioServicio = preusServeis.get(servicio);
                System.out.println("El precio del servicio " + servicio + " es de " + precioServicio + " euros.");
                precioFinal += precioServicio;
            }
        }

        System.out.printf("\nSubtotal: %.2f euros.\n",precioFinal);
        Float ivaReserva = precioFinal*IVA;
        System.out.printf("IVA (21%%): %.2f euros.\n",ivaReserva);
 
        Float precioTotal = precioFinal + ivaReserva;
        System.out.printf("Total Reserva: %.2f euros.\n\n",precioTotal);

        return precioTotal;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        int codigo;
        do {
            codigo = (int) (Math.random() * 900) + 100;
        } while (reserves.containsKey(codigo));

        return codigo;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\nIntroduzca el número de reserva a liberar: \n");
        int codigoReserva = sc.nextInt();

        if(reserves.containsKey(codigoReserva)){

            String tipoHabitacion = reserves.get(codigoReserva).get(0);
            Integer disponibilidad = disponibilitatHabitacions.get(tipoHabitacion);

            disponibilidad ++;
            disponibilitatHabitacions.put(tipoHabitacion, disponibilidad);

            reserves.remove(codigoReserva);
            System.out.println("Habitación liberada correctamente.\nDisponibilidad actualizada.");
        }else{
            System.out.println("El código introducido no pertenece a ninguna reserva.");
        }
    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        System.out.println("\nMostrando disponibilidad actual: ");
        for(Map.Entry<String, Integer> fila : disponibilitatHabitacions.entrySet()){
            
            String key = fila.getKey();
            Integer capacidadOriginal = capacitatInicial.get(key);

            Integer valor = fila.getValue();
            System.out.println("Hi ha " + valor + " habitacions del Tipus " + key + " disponibles de un total de " + capacidadOriginal);
        }
    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
        if(codis.length != 0)
        {
            int numeroReserva = codis[0];
            ArrayList<String> datosReserva = reserves.get(numeroReserva);
            String tipoHabitacionReservada = datosReserva.get(0);

            if(tipoHabitacionReservada.equals(tipus)){
                mostrarDadesReserva(numeroReserva);
            }

            if(codis.length > 1){
                int[] codigosNuevo = new int[codis.length -1];

                System.arraycopy(codis,1,codigosNuevo,0,codigosNuevo.length);
                llistarReservesPerTipus(codigosNuevo, tipus);
            }
        }else{
            System.out.println("No hay reservas del tipo: " + tipus);
        }
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        System.out.println("\n===== CONSULTAR RESERVA =====");
        System.out.println("\nIntroduzca el número de reserva: \n");
        int codigoReserva = sc.nextInt();

        if(reserves.containsKey(codigoReserva)){
            mostrarDadesReserva(codigoReserva);
        }else{
            System.out.println("El código introducido no pertenece a ninguna reserva.");
        }
 
    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        String tipoHabitacionElegida = seleccionarTipusHabitacio();

        Set<Integer> keySet = reserves.keySet();
        int tamanoLista = keySet.size();
        int[] codigos = new int[tamanoLista];
        
        int i = 0;
            
        for (Integer llave : keySet){
            codigos[i] = llave;
            i++;
        }    
        
       llistarReservesPerTipus(codigos, tipoHabitacionElegida);
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {
       if (reserves.containsKey(codi)){
        ArrayList<String> datosReserva = reserves.get(codi);
        String tipoHabitacion = datosReserva.get(0);
        String precioTotal = datosReserva.get(1);

        System.out.println("\nInformación de la reserva "+ codi);
        System.out.println("Tipo de habitación: " + tipoHabitacion);
        System.out.println("Precio Total: " + precioTotal);


        for (int i = 2; i < datosReserva.size(); i++) {
            System.out.println("Servicio adicional: " + datosReserva.get(i));
        }

        } else {
            System.out.println("No existen reservas con ese código.");     
        }
    
    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
                System.out.print(missatge);
                valor = sc.nextInt();
                correcte = true;
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
