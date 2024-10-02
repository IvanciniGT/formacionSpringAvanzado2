import java.util.function.*; // Nuevo paquete en JAVA 1.8
// Define interfaces que nos permiten representar funciones
// Las llamamos interfaces funcionales:
// - Function<T, R> : Representa una función que acepta un argumento de tipo T y devuelve un resultado de tipo R
// - Consumer<T> : Representa una operación que acepta un argumento de tipo T y no devuelve ningún resultado
//                 setter
// - Predicate<T>: Representa una función que acepta un argumento de tipo T y devuelve un valor booleano
//                 is... has...
// - Supplier<T> : Representa una función que no acepta argumentos y devuelve un resultado de tipo T
//                 getter de una clase

public class ProgramacionFuncional {
    
    public static void saluda(String nombre){
        System.out.println(nombre);
    }


    public static void main(String[] args){
        saluda("Ivan");

        Consumer<String> miFuncion = ProgramacionFuncional::saluda; // En java 1.8 aparece un operador nuevo ::, que nos permite referenciar funciones

        miFuncion.accept("Ivan");

        Function<Double, Double> doblar = ProgramacionFuncional::doblar;
        imprimir_resultado(doblar, 5);
        imprimir_resultado(ProgramacionFuncional::doblar, 5);
    
        // Para qué creamos funciones?
        // - Reutilización de código
        // - Estructurar mejor mi programa... facilita la lectura
        // - Porque necesite pasar una función como argumento a otra función
        // Si necesito pasar a una funcion otra funcion, pero no quiero:
        // - Ni reutilizarla
        // - Ni estructurar mejor mi programa (de hecho me la complicaría)
        // Puedo optar por una nueva estrategia: EXPRESIONES LAMBDA

        // Una expresion lambda es ante todo una expresion: Trozo de código que devuelve un valor
        // Qué valor devuelve una lambda: Una función anonima creada en tiempo de ejecución dentro de la propia expresión.
        // Las creamos con el operador flecha ->

        Function<Double, Double> funcion = (Double numero) -> { // Declarar una función con otra sintaxis.
            return numero * 2; // El tipo de retorno... qué pasa? Java lo conoce por definición, por el return.
        };
        Function<Double, Double> funcion2 = (numero) -> { 
            return numero * 2; // Y el tipo de entrada??? Aqui si lo infiere Java de la variable a la que se asigna la función
        };
        Function<Double, Double> funcion3 = numero -> { 
            return numero * 2; // Y el tipo de entrada??? Aqui si lo infiere Java de la variable a la que se asigna la función
        };
        Function<Double, Double> funcion4 = numero -> numero * 2;


        imprimir_resultado(funcion4, 5);
        imprimir_resultado(numero -> numero * 5, 5);

    }

    public static double doblar(double numero){
        return numero * 2;
    }

    public static void imprimir_resultado(Function<Double, Double> operacion, double numero){
        System.out.println(operacion.apply(numero));
    }

}
