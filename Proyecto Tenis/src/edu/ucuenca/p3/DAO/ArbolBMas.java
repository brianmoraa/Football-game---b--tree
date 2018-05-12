package edu.ucuenca.p3.DAO;

import edu.ucuenca.p3.Modulos.Nodo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArbolBMas {

    private Nodo nodoRaiz;
    private static final int longitud_vector = 4;   //longitud vector de vectores que contienen llaves, objetos
    private RandomAccessFile archivo_aleatorio;

    public ArbolBMas(String file) {
        try {
            this.archivo_aleatorio = new RandomAccessFile(file + ".dat", "rw"); //Creacion de archivo de tipo acceso aleatorio

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArbolBMas.class.getName()).log(Level.SEVERE, null, ex);
        }

        nodoRaiz = leerValorNodo(0);
        if (nodoRaiz == null) {
            nodoRaiz = new Nodo();
            nodoRaiz.valor_hoja = true;
            escribirValorNodo(nodoRaiz, 0);

        }

    }

    public void escribirValorNodo(Nodo nodo, long posicionNodo) {
        try {
            archivo_aleatorio.seek(posicionNodo);   //Se salta a la posición del registro, 
            //el comando seek permite realizar esa función.
            byte[] obj = Serializar.serialize(nodo);     //Se serializa la variable tipo Nodo
            //el cual contiene un vector de objetos, claves y cantidad de hijos
            byte[] objNodo = new byte[2000];  //La longitud total del array será de 1000 bytes
            System.out.println(obj.length);
            System.arraycopy(obj, 0, objNodo, 0, obj.length);   //Copiamos el array obj al array objNodo
            int i = obj.length;
            for (int j = i; j < 2000; j++) {
                objNodo[j] = -1;  //Se rellena con valores -1
            }
            archivo_aleatorio.write(objNodo);       //Se escribe en el archivo de acceso aleatorio el array objNodo
        } catch (IOException ex) {
            Logger.getLogger(ArbolBMas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Nodo leerValorNodo(long posicionNodo) {
        Nodo nodo = null;
        try {
            archivo_aleatorio.seek(posicionNodo); //Se salta a la posición del registro
            byte[] data = new byte[2000];
            archivo_aleatorio.readFully(data);  //Se leerá el número solicitado de bytes, en éste caso 1000 bytes
            nodo = (Nodo) Serializar.deserialize(data);       //Se deserializa el array 
        } catch (IOException | ClassNotFoundException ex) {
//            Logger.getLogger(ArbolBMas.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return nodo;
    }

    public void agregar(String key, Object object) {        //Se agrega la clave y el objeto serializado

        //La raíz siempre va a estar al principio del archivo
        Nodo tmpNodoRaiz = leerValorNodo(0);    //Se lee el nodo de la primera posición
        if (tmpNodoRaiz.numero_llaves == (2 * longitud_vector - 1)) {      //En caso de que el nodo  esté lleno 
            System.out.println("El nodo se ha lleno y se procede a dividirse");
            try {
                Nodo n_nodoRaiz = new Nodo();      //Se instancia una variable Nodo y ésta será el nodo raíz
                nodoRaiz = n_nodoRaiz;              //Nodo raiz = Nodo padre
                n_nodoRaiz.valor_hoja = false;                    //El estado de Hoja es false debido a que es raiz/nodo padre
                long totalSize = archivo_aleatorio.length();    //Se toma la longitud de bytes del archivo

                nodoRaiz.ubicacionHijo[0] = totalSize;      //A la ubicación 0 del array ubicaciónHijo se da el tamaño del archivo

                escribirValorNodo(nodoRaiz, 0);             //Se escribe los valores en el nodo padre 
                escribirValorNodo(tmpNodoRaiz, totalSize);  //Se sobre-escribe el nodo que antiguamente no era raiz en la última posición del archivo

                dividirNodo(n_nodoRaiz, 0, tmpNodoRaiz, 0); //Se inserta la clave en el árbol y divide 
                insertar(n_nodoRaiz, key, object, 0); // Inserta la clave del objeto en el árbol con la raiz 

            } catch (IOException ex) {
                Logger.getLogger(ArbolBMas.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            insertar(tmpNodoRaiz, key, object, 0);
        }
    }

    public Object get(String key) {
        //Se empieza a buscar desde el nodo raíz
        return get(nodoRaiz, key);
    }

    // Método de búsqueda recursiva
    private Object get(Nodo nodo, String key) {
        int i = 0;
        //el bucle verifica la cantidad de llavez que existen en el nodo, empezando desde el nodo raiz
        // y al mismo tiempo verifica si la clave es mayor, en caso de no serlo se detiene y almacena la cantidad de valores recorridos i
        while (i < nodo.numero_llaves && mayorQue(key, nodo.llaves[i])) {
            i++;
        }
        if (i < nodo.numero_llaves && key.compareTo(nodo.llaves[i]) == 0) { //En caso de ser el la clave deseada se devuelve el 
            System.out.println("El valor del índice de las llaves es el mismo al del objeto");
            return nodo.objetos[i];                                         //el valor del objeto con el índice recorrido
        }
        if (nodo.valor_hoja) {                  //En caso de ser nodo hoja se retorna null
            //Quiere decir que acabó la búsqueda
            //System.out.println("Es nodo hoja: " + nodo.valor_hoja);
            return null;
        } else {

            return get(leerValorNodo(nodo.ubicacionHijo[i]), key);
        }
    }

    public void eliminar(String key) {
        eliminar(nodoRaiz, key, 0);
    }

    public void eliminar(Nodo nodo, String key, long posicionNodo) {
//        long posicionNodo = nodo.ubicacion_siguiente_nodo;
        int i = 0;
        while (i < nodo.numero_llaves && mayorQue(key, nodo.llaves[i])) {
            i++;
        }
        if (i < nodo.numero_llaves && key.compareTo(nodo.llaves[i]) == 0) {
            nodo.estados[i] = 0;
            System.out.println("Se reescribió el nodo");
            escribirValorNodo(nodo, posicionNodo);

        }

        if (!nodo.isValor_hoja()) {
            eliminar(leerValorNodo(nodo.ubicacionHijo[i]), key, nodo.ubicacionHijo[i]);
        } else {
            System.out.println("Se ha llegado al nodo hoja del árbol !");
        }
    }

    private void dividirNodo(Nodo nodoPadre, int i, Nodo nodo, long ubicacion) {

        Nodo nuevoNodo = new Nodo();
        nuevoNodo.valor_hoja = nodo.valor_hoja;     //El nodo se setea el estado de si es o no nodo hoja
        nuevoNodo.numero_llaves = longitud_vector;  //La cantidad de llaves se iguala a la longitud del vector 

        long totalSize = 0;     //instancio la longitud del tamaño del archivo 

        try {
            totalSize = archivo_aleatorio.length();//Se obtiene la posición que es la longitud del archivo
        } catch (IOException ex) {
            Logger.getLogger(ArbolBMas.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*Se escribe la mitad hacia la derecha al nuevo nodo hijo*/
        for (int j = 0; j < longitud_vector; j++) {     //Solo se reordena la mitad al ser dividido
            nuevoNodo.llaves[j] = nodo.llaves[j + longitud_vector - 1]; //Se reordena los arrays de claves del nodo inferior a la derecha 
            nuevoNodo.objetos[j] = nodo.objetos[j + longitud_vector - 1];   //pasando los valores de objetos y claves al nuevo nodo hoja

            nuevoNodo.estados[j] = nodo.estados[j + longitud_vector - 1];
        }
        if (!nuevoNodo.valor_hoja) {        //En caso de no ser hoja, osea puede ser nodo padre, entonces tiene hijos y su respectiva ubicación
            for (int j = 0; j < longitud_vector + 1; j++) {
                nuevoNodo.ubicacionHijo[j] = nodo.ubicacionHijo[j + longitud_vector - 1];
            }
            for (int j = longitud_vector; j <= nodo.numero_llaves; j++) {
                nodo.ubicacionHijo[j] = 0;      //Se rellena con 0 las ubicaciones restantes del nodo original, ya que las otras fueron
            }                                   //escritas en nuevoNodo, el nodo original es el que va a la izquierda 
        } else {
            nuevoNodo.ubicacion_siguiente_nodo = nodo.ubicacion_siguiente_nodo; // Se setea el valor de la posición del siguiente nodo en el RAF
            nodo.ubicacion_siguiente_nodo = totalSize;
            System.out.println("Ubicación de siguiente nodo: " + nuevoNodo.ubicacion_siguiente_nodo);

        }
        //El nodos original tienen valores nulos en sus arrays de objetos y llaves
        //a partir de la mitad para adelante
        for (int j = longitud_vector - 1; j < nodo.numero_llaves; j++) {
            nodo.llaves[j] = null;
            nodo.objetos[j] = null;

            nodo.estados[j] = null;

        }
        nodo.numero_llaves = longitud_vector - 1;   // Se setea el valor del número de llaves a la longitud del vector -1

        for (int j = nodoPadre.numero_llaves; j >= i + 1; j--) {
            nodoPadre.ubicacionHijo[j + 1] = nodoPadre.ubicacionHijo[j];        //Se escribe la ubicación del nodo hijo 
        }                                                                       //ya que al dividirse, el valor promedio sube un nivel como separador

        escribirValorNodo(nuevoNodo, totalSize);            //Se escribe el valor del nodo en el nodo hijo en la posición de la longitud del archivo

        nodoPadre.ubicacionHijo[i + 1] = totalSize;         //Se setea la ubicación del nodo hijo como la longitud del archivo
//        System.out.println("llaves: "+nodoPadre.numero_llaves);
        //debido a que ahí se escribió el nuevo nodo
        for (int j = nodoPadre.numero_llaves - 1; j >= i; j--) {    //Se setea los valores del nodo padre
            nodoPadre.llaves[j + 1] = nodoPadre.llaves[j];          //hacia la derecha en caso de haber más
            nodoPadre.objetos[j + 1] = nodoPadre.objetos[j];        // claves, un nodo padre no lleno
                                                                    // [valor, valor, _ , valor valor]
            nodoPadre.estados[j + 1] = nodoPadre.estados[j];        //dependiendo del valor de i, puede estar 
        }                                                           // al principio, final, etc.

        nodoPadre.llaves[i] = nuevoNodo.llaves[0];      //Al dividirse un nuevo nodo se setean los valores de llaves y objetos
        nodoPadre.objetos[i] = nuevoNodo.objetos[0];    // del nodo inferior al valor de i, ya que son el mismo valor y comparten las 

        nodoPadre.estados[i] = nuevoNodo.estados[0];

        nodoPadre.numero_llaves++;                      // mismas claves, es decir el mismo valor es el que sube, por ende comparten características

        escribirValorNodo(nodoPadre, ubicacion);            //Se escribe el valor del nodo padre en la ubicación dada como parámetro

    }

    //Métodos de comparación de valores - menores a la izquierda y mayores a la derecha
    private boolean menorQue(String key, String mKey) {
        String pal1 = key.toUpperCase();
        String pal2 = mKey.toUpperCase();
        int tmp = pal1.compareTo(pal2);
        return tmp <= 0;

    }

    private boolean mayorQue(String key, String mKey) {
        String pal1 = key.toUpperCase();
        String pal2 = mKey.toUpperCase();
        int tmp = pal1.compareTo(pal2);
        return tmp > 0;
    }

//    private boolean validarEstados(Nodo nodo) {
//        int i = 0;
//        boolean eliminado = false;
//        System.out.println("Verificación de estados");
//        while (eliminado == false || nodo.numero_llaves > i) {
//            if (nodo.estados[i] == 0) {
//                eliminado = true;
//            }
//            i++;
//        }
//        return eliminado;
//    }
    private boolean validarEstados(Nodo nodo) {
        int i = 0;
        boolean eliminado = false;
        System.out.println("Verificación de estados");
        System.out.println("Cantidad de claves: " + nodo.numero_llaves);
        if (nodo.numero_llaves > 0) {
            while (i < nodo.numero_llaves) {
                if (nodo.estados[i] != null && nodo.estados[i] == 0) {
                    System.out.println("Se verificó que el nodo fue eliminado !");
                    eliminado = true;
                    break;
                }
                i++;
            }
        }
        return eliminado;
    }

    private void insertar(Nodo nodo, String key, Object object, long ubicacion) {
//        System.out.println("Método insertar");
        int i = nodo.numero_llaves - 1;
        //Se resta i - 1 para la ubicación de la última llave en array en el nodo
        System.out.println("Valor de i " + i);
        if (nodo.valor_hoja) {              //En caso de ser hoja el nodo  
            //Se setean las ubicaciones de objetos y claves dependiendo de su valor
            while (i >= 0 && menorQue(key, nodo.llaves[i])) {   //Valores menores a la clave van a la izquierda
                if (nodo.estados[i] == null || nodo.estados[i] != 0) {

                    nodo.llaves[i + 1] = nodo.llaves[i];
                    nodo.objetos[i + 1] = nodo.objetos[i];
                    nodo.estados[i + 1] = nodo.estados[i];
                }
                i--;
            }
            i++;
            System.out.println("Valor de i luego del bucle: " + i);
            
            
            //Se escribe el valor de la clave y el objeto en el array correspondiente luego de ordenar valores
            nodo.llaves[i] = key;
            nodo.objetos[i] = object;

            nodo.estados[i] = 1;
            
            /*Bucle para validar la cantidad de claves del nodo*/
            int cantidad_claves = 0;
            for (int j = 0; j < nodo.llaves.length; j++) {
                if(nodo.llaves[j] != null){
                    cantidad_claves ++;
                }
            }
            
            //Aumenta la cantidad de llaves (claves) en el nodo correspondiente
//            nodo.numero_llaves++;
            nodo.numero_llaves = cantidad_claves;
            
            //Se escribe el nodo ya seteado con sus arrays de objetos y claves en la posición indicada 
            escribirValorNodo(nodo, ubicacion);

        } else {

            while (i >= 0 && menorQue(key, nodo.llaves[i])) {
                i--;
            }
            i++;
            //El método leerValorNodo devolverá el nodo escrito en la ubicación i
            //del array, lo que se almacena en el vector de ubicacionHijo[] 
            //es el tamaño del archivo al momento de ser inscrito el nodo
            Nodo nodohijo = leerValorNodo(nodo.ubicacionHijo[i]);       // Se lee el nodo de la posición i tras 
            // validar si son valores menores a éste.
            if (nodohijo.numero_llaves == (2 * longitud_vector - 1)) {  //En caso de que la cantidad de claves sea tope
                dividirNodo(nodo, i, nodohijo, ubicacion);              //Se divide el nodo
                if (mayorQue(key, nodo.llaves[i])) {
                    i++;                                                //Se setea la posición i 
                }
            }
            insertar(nodohijo, key, object, nodo.ubicacionHijo[i]);     //Y se inserta ese nodo en la ubicación del vector i 
            //luego de compararse los valores mayores 
        }

    }

}
