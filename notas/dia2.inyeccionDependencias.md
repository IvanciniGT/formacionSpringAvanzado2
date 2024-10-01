Con respecto a la inyección de dependencias en Spring, tenemos 2 temas SEPARADOS a tratar:
- Cómo pedirle a Spring que inyecte una dependencia: Que me de un objeto de un determinado tipo
- Cómo decirle a Spring qué objeto inyectar: Qué entregar cuando alguien  pide un objeto de un determinado tipo

Y ahí es donde se produce el DESACOPLAMIENTO entre el código que necesita una dependencia y el código que la provee.
- Alguien pide un objeto de un TIPO
- Alguien provee un objeto de un TIPO
Y Spring hace match entre ambos en base al TIPO. Es el TINDER de las dependencias.

Ese TIPO es nuestra interfaz.

# Apartado 1: Cómo pedirle a Spring que inyecte una dependencia

## OPCION 1: @Autowired: QUE ESTA TOTALMENTE DESACONSEJADO (salvo en casos muy muy muy especiales)

```java

import org.springframework.beans.factory.annotation.Autowired;

public interface MiInterfaz {
    void metodo();
}

public class MiClase {
    @Autowired // spring, rellena esta variable de forma que apunte a un objeto de tipo MiInterfaz... que tu sabrás de donde sacas. No es mi problema, oh, yo, desarrolador de MiClase!
    private MiInterfaz miInterfaz;

    public MiClase() {
        // Aquí no puedo usar miInterfaz... aún no está inicializada
    }

    public void metodo() {
        miInterfaz.metodo();
    }
}

```
NOTAS:
1. Esto solo funciona si es SPRING quien hace el new de MiClase. Si es Spring el que instancia MiClase. 
   Spring escribirá el código:
    ```java
        MiClase miClase = new MiClase();
        miClase.miInterfaz = //LO QUE SEA QUE IMPLEMENTE MiInterfaz
    ```
    Si soy yo quien hace el new... no party! La variable se queda en null!
2. En realidad, Spring no hace: miClase.miInterfaz = //LO QUE SEA QUE IMPLEMENTE MiInterfaz
    Por ser una variable privada no es posible hacerlo. Spring hace uso de la reflexión para asignar el valor de la variable.
    Reflection??? Un paquete que hay en Java que permite hacer cosas que no deberían hacerse.
    Opera trabajando con los datos a nivel de la RAM, saltándose las restricciones de acceso que impone el compilador = PELIGROSO DE COJONES... además de lento.
3. Por ello, el uso de @Autowired está TOTALMENTE DESACONSEJADO!
4. Además, hay otra limitación FUNCIONAL. Y es... cuándo puedo empezar a usar mi variable? Una vez la instancia esté creada...
   Es decir, si yo necesito acceso al objeto al que apunta la variable en el constructor de MiClase, lo tengo? Tengo acceso ya a ese objeto? NO... Spring aún no lo ha inyectado.

## Opción 2: Simplemente pedir el dato que necesite en cualquier función

```java
public interface MiInterfaz {
    void metodo();
}

public class MiClase {
    public void metodo(MiInterfaz miInterfaz) { // No @Autowired ni nada. Spring ya me dará ese dato... Lo lee de la firma de la función.
        miInterfaz.metodo();
    }
}
```
NOTA:
1. Esto solo funcionará si Spring es quién llama a esa función!
   Si soy yo, en otro sitio de mi código el que invoca esta función... entonces es mi responsabilidad el dar el dato.

## Opción híbrida: GUAY , cuando me interesa y puedo usarla

```java

import org.springframework.beans.factory.annotation.Autowired;

public interface MiInterfaz {
    void unaFuncionDeLaInterfaz();
    void otraFuncionDeLaInterfaz();
}

public class MiClase {
    private final MiInterfaz miInterfaz;

    public MiClase(MiInterfaz miInterfaz) { // Como función cualquiera que es un constructor
                                            // Si pido datos Spring me los dará (SEGÚN HEMOS EXPLICADO EN LA OPCIÓN 2)
        this.miInterfaz = miInterfaz;
        // PODRE HACERLO! miInterfaz.unaFuncionDeLaInterfa();    // Esos datos, ya los tengo disponibles en el constructor
    }

    public void metodo() {
        miInterfaz.otraFuncionDeLaInterfaz();
    }
}

```
NOTA: 
1. Esto solo funcionará si SPRING quien hace el new de MiClase. Si es Spring el que instancia MiClase. 
   Y cómo le digo a Spring que debe ser él el que cree una instancia de MiClase? 




# Apartado 2: Cómo decirle a Spring qué objeto inyectar

Partiendo de que existe un código del tipo:
```java
public interface MiInterfaz {
    void metodo();
}
public class MiClase {
    private final MiInterfaz miInterfaz;
    public MiClase(MiInterfaz miInterfaz) {
        this.miInterfaz = miInterfaz;
    }
    public void metodo() {
        miInterfaz.metodo();
    }
}
```

Cómo le explico a Spring que objeto debe entregar al crear la clase MiClase?
Es decir, cuando spring escriba y ejecute el código:
```java
    MiInterfaz miInterfaz = new ?????() //(COMO LE DIGO DE QUE DEBE HACER EL NEW)
    MiClase miClase = new MiClase(miInterfaz);
```

2 Opciones:

## Opción 1:Usando la anotación @Component en alguna clase que implemente MiInterfaz

```java
import org.springframework.stereotype.Component;

@Component // Si alguien pide un objeto de tipo MiInterfaz (~~un objeto de tipo MiInterfazImpl~~) dale una instancia de esta clase
public class MiInterfazImpl implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola Mundo");
    }
}
```

Notas:
1. Esto solo funcionará si Spring escanea esta clase al arrancar. Eso habrá que pedírselo... ya veremos como.
2. Por defecto, Spring solo creará una única instancia de esta clase, es decir, la tratará como si la hubiéramos implementado según un patrón SINGLETON. Cuidado... No es un SINGLETON lo que tengo. Es decir, yo podría por mis propios CODIGOS crear otra instancia de MiInterfazImpl. Lo único que se me garantiza es que Spring no lo hará... A no ser que me interese que lo haga:
3. Si quiero que Spring cree una nueva instancia cada vez que alguien pida un objeto de tipo MiInterfaz, debo añadir la anotación @Scope("prototype") a la clase MiInterfazImpl
4. Hay muchas Anotaciones que extienden la anotación Component. Por ejemplo, @Service, @Repository, @Controller, @RestController...
   Algunas de ellas SOLO AÑADEN SEMANTICA, es decir:
    - Si ponemos la anotación @Service en lugar de @Component, lo que estamos diciendo a los DESARROLLADORES que lean mi CODIGO es que esta clase es un servicio, es decir que se encarga de la lógica de negocio. A Spring se la trae al peiro.
   Otras NO... o mejor dicho si, pero no solo:
    - Si ponemos la anotación @RestController, obtenemos:
      - Un mayor significado semántico para otros desarrolladores, que entenderán rápidamente la naturaleza de la clase (el objetivo que tiene, por qué está ahí)
      - Y además Spring hará cosas adicionales, en base a esa naturaleza. Por ejemplo, Spring mapeará las funciones de esa clase a llamadas HTTP en un servidor de aplicaciones.
   En Spring CORE (que es la librería base de Spring, se define @Component y unas poquitas más: @Service, @Repository, @Controller)
   Otras librerías de SPRING nos van dando más anotaciones:
      - Spring MVC: @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping 
      - SpringBatch: @Job, @Step, @Tasklet, @Chunk, @Reader, @Writer, @Processor
      - StringStateMachine: @WithStateMachine, @OnTransition, @OnStateChanged, @OnExtendedStateChanged
      - SpringDataJPA: @Transactional
      Y en Spring hay 200 librerías 
5. AQUI HAY MAGIA! Esas anotaciones son las que nos están ofreciendo un lenguaje DECLARATIVO. AQUÍ ES DONDE NO HAY FLUJO.

NOTA ADICIONAL: Si esta opción me sirve, dejar de leer el resto, la Opción 2 NO APLICA!

### Ejemplo de caso donde entraría un SCOPE PROTOTYPE

App -> RepositorioDeDiccionarios 
Cuantos repositorios necesito en mi app? Necesito crear 800 repositorios, cada ves que alguien pida un repositorio crear uno?
NO... creo uno y cuando alguien pida un repositorio le doy el mismo repositorio.

En cambio en otros escenarios
Transacción -> ResultadosDeLaTransacción <- ResultadosDeLaTransacciónMemoria

```java
class ResultadosDeLaTransacciónMemoria{
    private List<Resultado> resultados;

    public void añadirResultado(Resultado resultado){
        resultados.add(resultado
}
```
La clase Transaccion necesita para funcionar un ResultadosDeLaTransacción (TIENE UNA DEPENDENCIA)
Pero cada transacción necesita un ResultadosDeLaTransacción distinto.
Lo que no quiero es que Transacción DEPENDA RIGIDAMENTE DE MI IMPLEMENTACIÓN DE ResultadosDeLaTransacción
El día de Mañana podría tener distintas implementaciones de ResultadosDeLaTransacción:
- Una que vaya cacheando en RAM (como la que tengo ahora)
- Otra que vaya escribiendo en disco

### Ejemplo de patrón Singleton
```java
// Patrón singleton: Esta clase SOLO puede tener una única instancia
public class MiSingleton {
    private static volatile MiSingleton instancia = null; // volatile: JAVA puede (y lo hace por defecto) cachear variables en los registros de la CPU. Con volatile le decimos que no lo haga, ya que esa variable puede haber sido modificada por otro hilo, que se ha ejecutado en otra CPU.
    private MiSingleton() {}
    public static MiSingleton getInstancia() {
        if (instancia == null) {    // Para asegurar que el lock (synchronized) solo se ejecute si no tengo ya una instancia...
                                    // es decir, las primeras veces que llamos a este método, ya que el lock es una operación costosa.
            synchronized (MiSingleton.class) {       // Para evitar condiciones de carrera
                                                        // Es decir, que 2 hilos paralelos puedan llegar al if de abajo.. 
                                                        // ambos 2 pasar el if... y que cada uno cree su instancia
                if (instancia == null) {             // Para asegurar que la linea de dentro solo se ejecute si no tengo ya una instancia
                    instancia = new MiSingleton();
                }
            }
        }
        return instancia;
    }
}
```


## Opción 2: Esta solo se usa si la anterior no es factible.

Escenario de uso?

La opción 1 requiere que ponga la anotacion @Component (o una variante) en la clase que implementa MiInterfaz.
Y si la clase que implementa MiInterfaz no es mía? Es de una librería de terceros? Puedo ponerle la anotación @Component en la clase?

ESTE ES EL ESCENARIO DE USO: Cuando quiero que Spring inyecte una instancia de una clase que no es mía.

```java

package libreria.de.terceros;

public interface SuInterfaz {
    void metodo();
}

public class SuClase implements SuInterfaz {
    public void metodo() {
        System.out.println("Hola Mundo");
    }
}

public class OtraClase implements SuInterfaz {
    public void metodo() {
        System.out.println("Hola Mundo");
    }
}

```
Y yo en mi código necesito una instancia de SuInterfaz... pero no puedo ponerle la anotación @Component a ninguna de las 2 clases.
Sabré cual quiero.. eso si... Y digo... quiero una instancia de OtraClase.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import libreria.de.terceros.OtraClase;
import libreria.de.terceros.SuInterfaz;

@Configuration // Esta anotación le indica a Spring que debe instanciar esta clase al arrancar Spring
               // para tener acceso a poder ejecutar todas las funciones que haya en ella y tengan la anotación @Bean
public class MiConfiguracion {
    @Bean // Spring, cuando alguien pida un objeto de tipo SuInterfaz, Spring entregue lo que esta función devuelva
    //@Scope("prototype") // Si quiero que Spring cree una nueva instancia cada vez que alguien pida un objeto de tipo SuInterfaz
    public SuInterfaz seLlamaráComoSea() {  // No es relevante el nombre.. Nunca ejecutaré yo esta función. La ejecutará Spring
        // En ella, yo creo la instancia que quiero que Spring inyecte. En este caso una instancia de OtraClase
        return new OtraClase();
    }
}

public class MiClase {
    private final SuInterfaz suInterfaz;
    public MiClase(SuInterfaz suInterfaz) { // Spring, al llamar a este constructor, me dará una instancia de SuInterfaz
                                            // Que mediante la @Configuration y el @Bean, le he dicho que sea 
                                            // Lo que devuelva la función seLlamaráComoSea()
        this.suInterfaz = suInterfaz;
    }
    public void metodo() {
        suInterfaz.metodo();
    }
}
```

NOTAS:
1. Esto solo funcionará si SPRING lee la clase MiConfiguracion al arrancar. Eso habrá que pedírselo... ya veremos como.
2. Por defecto, Spring solo llamará a la función seLlamaráComoSea() una única vez, y cacheará el resultado. Desde ese momento siempre devolverá la misma instancia cada vez que se pida un objeto de tipo SuInterfaz. Es decir, por defecto un comportamiento tipo SINGLETON.
   Si quiero que Spring llame a la función seLlamaráComoSea() cada vez que alguien pida un objeto de tipo SuInterfaz, debo añadir la anotación @Scope("prototype") a la función seLlamaráComoSea()

# NOTAS GENERALES SOBRE EL ARRANQUE DE SPRING

## ESCANEO DE COMPONENTES 

Cuando mi aplicación arranca, ya dijimos que lo único que hace es ejecutar el método main de la clase que le he indicado.
Y en ese método main, lo único que hago es decirle a Spring que arranque.
```java
package com.curso;
import org.springframework.boot.SpringApplication;

@SpringBootApplication // Le indicamos a Spring que escanee el paquete de la clase actual y sus subpaquetes en busca de COMPONENTES y CONFIGURACIONES y más cosas... que ya veremos
//@ComponentScan("paquete") // Le indicamos a Spring que escanee un paquete y sus subpaquetes en busca de COMPONENTES y CONFIGURACIONES
public class MiAplicacion {
    public static void main(String[] args) {
        SpringApplication.run(MiAplicacion.class, args); // Spring, arranca la aplicación: Inversion de CONTROL
    }
}
```

En ese momento Spring es cuando debe escanear las clases que a mi me interesen.... para detectar COMPONENTES y CONFIGURACIONES. Esto lo haremos con algunas anotaciones adicionales:
@ComponentScan(paquetes) : Le indicamos a Spring que escanee un paquete y sus subpaquetes en busca de COMPONENTES y CONFIGURACIONES
@SpringBootApplication : Le indicamos a Spring que escanee el paquete de la clase actual y sus subpaquetes en busca de COMPONENTES y CONFIGURACIONES... y además hará más cositas... que ya veremos.

## VALIDACIONES INICIALES

Spring va a mirar al arrancar todo mi código. Y en lo relativo a inyección de dependencias, va a hacer algunas comprobaciones:
- Si encuentra métodos que debe invocar (métodos normales o constructores) que necesiten una Lista de objetos de un determinado tipo, se asegurará que haya al menos un objeto de ese tipo. Pero si hay varios, no hay problema en este caso... Tomará Todos y los inyectará dentro de una Lista.


```java

public interface MiInterfaz {
    void metodo();
}
@Component
public class UnaClaseQueImplementaMiInterfaz implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola Mundo");
    }
}
@Component
public class OtraClaseQueImplementaMiInterfaz implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola Mundo");
    }
}

public class MiClase {
    private final List<MiInterfaz> miInterfaz;
    public MiClase(List<MiInterfaz> miInterfaz) { // Spring, al llamar a este constructor, me dará una lista con todas las instancias de MiInterfaz: En nuestro caso: UnaClaseQueImplementaMiInterfaz y OtraClaseQueImplementaMiInterfaz
        this.miInterfaz1 = miInterfaz1;
    }
}
```



- Si encuentras métodos que debe invocar (métodos normales o constructores) que necesiten un objeto de un determinado tipo, se asegurará que mediante alguna opción le hayamos dicho unequivocamente a Spring qué objeto debe inyectar.
  - UNEQUIVOCAMENTE: 
    - Si no le hemos dicho nada, Spring no sabrá qué objeto inyectar y no arrancará.
    - Si hay más de una opción, Spring no sabrá cuál elegir y no arrancará.
  Nota:
    - Y si necesito 2 objetos diferentes del mismo tipo en 2 métodos diferentes? Hemos de:
      - OPCION 1: Indicarle una opción fija, que no deje duda: @Primary
      - OPCION 2: Qualificamos las opciones: @Qualifier("tipo1") @Qualifier("tipo2")

```java

public interface MiInterfaz {
    void metodo();
}
@Component
//@Primary // Fuerza a que se tome ésta... si hay más de una opción... PERO En nuestro caso, no es lo que queremos
@Qualifier("tipo1") // Si hay más de una opción, le decimos a Spring que esta es la que queremos
public class UnaClaseQueImplementaMiInterfaz implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola Mundo");
    }
}
@Component
@Qualifier("tipo2") // Si hay más de una opción, le decimos a Spring que esta es la que queremos
public class OtraClaseQueImplementaMiInterfaz implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola Mundo");
    }
}

public class MiClase {
    private final MiInterfaz miInterfaz1;
    public MiClase(@Qualifier("tipo1") MiInterfaz miInterfaz1) {
        this.miInterfaz1 = miInterfaz1;
    }
}

public class MiOtraClase {
    private final MiInterfaz miInterfaz2;
    public MiOtraClase(@Qualifier("tipo2") MiInterfaz miInterfaz2) {
        this.miInterfaz2 = miInterfaz2;
    }
}
```

---

# Hay un tema que nos quedaba por ver!
## Cómo le digo a Spring que debe ser él el que cree una instancia de una clase mia?

Ya hemos visto una opción: Si a una clase le pongo anotación @Component o @Configuration... o variantes de ellas, Spring se encargará de crear una instancia de esa clase al arrancar.

Y no hay más!