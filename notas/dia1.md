# Ejemplo que vamos a usar a lo largo de la formación

Queremos una aplicación que nos permita buscar los significados de una palabra en un diccionario de un determinado idioma. Como funcionalidad adicional, me gustaría que también me permita consultar las palabras similares a una palabra dada (en cuanto a su ortografía).

De momento, para estos ejemplos conceptuales, vamos a pensar que nuestra aplicación es una app de consola.
    $ buscarPalabra ES "manzana"
        La palabra "manzana" en español significa "Fruto del manzano"

## Cómo planteáis el desarrollo de este proyecto?

Cuántos componentes tiene nuestra aplicación? (*.jar)
- Frontal: Consola
- Gestión de diccionarios -> Este se va a dividir en varios
Cuántas clases tiene nuestra aplicación? *.java
Cuántos métodos tiene nuestra aplicación? *.java

Pregunta: Podría yo montar esta aplicación en un único .jar, con una única clase, usando solo 1 método(el main?)
De hecho, es posible que el proceso de crear esta app sea más natural para un ser humano el montarlo en una única clase, que en varias... y con un solo método.

    main{
        // Pregunto al usuario por un idioma
        // Leo unos ficheros que contienen palabras en un idioma
        // Las cargo en memoria en alguna estructura de datos (Map<String,List<String>>)
        // Pregunto al usuario por una palabra
        // Busco la palabra en la estructura de datos
        // Muestro los significados de la palabra por consola
    }

Monto esa función <> Pruebas -> OK <> Refactorización <> Pruebas -> OK
<-------------------------------->   <------------------------------->
  Solo es la mitad del trabajo          Esto puede llevar tanto tiempo o más que el desarrollo
                                        que el conseguir que funcione.

Llegados a ese punto, ya tengo un producto? Si... o no? Hemos definido que un producto de software es algo que se desarrolla, se usa y requiere de un mantenimiento y de evolutivos.
Ese código es fácilmente mantenible? y evolucionable? NO
- Si quiero cambiar el día de mañana la interfaz de consola por una web, lo tendría fácil? NO
- Si quiero cambiar el día de mañana la forma de persistir los diccionarios (ficheros-> BBDD), lo tendría fácil? NO
- Hay código que sea reutilizable? NO
- Corro riesgo de al añadir una nueva funcionalidad cargarme algo que estuviera funcionando? SÍ

Esto no cumple con lo que se espera de un producto de software.

Al refactorizar, empezaría a partir esas función en funciones más pequeñas.
Y esas funciones a su vez, las voy agrupando entre si, en clases (cohesión).
    SoC -> Separation of Concerns -> SRP: Single Responsibility Principle
                                          Single Responsible Party - El principio del responsable único
    Cada clase debe atender a un único responsable. Siendo un responble una persona/actor motor de posibles cambios.

Algunas clases que me van a salir serán:
- ~~Idioma -> String~~  
- BuscadorDePalabras
  - buscarPalabra
  - buscarPalabrasSimilares
- Diccionario               Map<String,List<String>>     EditorDeDiccionarios / UsuarioDeDiccionarios
  - obtenerLosSignificadosDeUnaPalabra
  - crearPalabra
  - borrarPalabra
- RepositorioDiccionario
  - guardarDiccionario
  - leerDiccionario
  - listarIdiomasParaLosQueHayDiccionario
- InterfazUsuario
  - pedirPalabra
  - pedirIdioma
  - mostrarSignificado
  - mostrarDiccionarioNoEncontrado
  - mostrarPalabrasSimilares
  - mostrarPalabraNoEncontrada

INVARIANTE: Hay conceptos en nuestro sistema que se que no van a evolucionar.. o al menos habrá poca probabilidad de que evolucionen.
Y a estos invariantes no necesitaré de alguna forma prestarles tanta atención como a los conceptos que sí que van a evolucionar.


- Diccionario              
  - buscarPalabra
  - buscarPalabrasSimilares
  - obtenerLosSignificadosDeUnaPalabra
- RepositorioDiccionario
  - leerDiccionario
  - listarIdiomasParaLosQueHayDiccionario
- InterfazUsuario
  - pedirPalabra
  - pedirIdioma
  - mostrarSignificado
  - mostrarDiccionarioNoEncontrado
  - mostrarPalabrasSimilares
  - mostrarPalabraNoEncontrada

## Paquete com.diccionarios.api

    public interface Diccionario {
        public boolean existe(String palabra);
        public Optional<List<String>> obtenerLosSignificadosDeUnaPalabra(String palabra);
    }

    public interface RepositorioDiccionario {
        public Optional<Diccionario> dameDiccionarioDe(String idioma);
        public boolean tienesDiccionarioDe(String idioma);
    }

## Paquete com.diccionarios.ficheros

    import com.diccionarios.api.Diccionario;
    public class DiccionarioEnFichero implements Diccionario {

        private final String idioma;
        private final Map<String, List<String>> palabras;

        public boolean existe(String palabra){
            // ...
        }
        #public List<String> obtenerLosSignificadosDeUnaPalabra(String palabra){
        #   // ...
        #}
            // Y eso es otra gran mierda! Nadie en esta formación podría decirme cómo se comporta esa función:
            - Qué le paso? String : palabra
            - Qué devuelve? Lista de significados... SEGURO?
              Si le paso la palabra ARCHILOCOCO?
                - OPCION 1: null            \
                - OPCION 2: Lista vacía     / Son ambiguas... npi. Mira documentación o mira código.
                - OPCION 3: NoSuchWordException: Al menos es explicita en JAVA (throws NoSuchWordException).
                  Problema: Una exception es cara... y su uso debe estar reservado para casos excepcionales. Cuando no se a priori si una cosa puede ocurrir o no, hasta que no la intento.
        public Optional<List<String>> obtenerLosSignificadosDeUnaPalabra(String palabra){
            // ...
        }
        // Un Optional es como una caja. Siempre me dan la caja... que puede contener algo o no.
        // Puede estar vacía. Marca la intención. QUITA LA AMBIGÜEDAD
    }

    import com.diccionarios.api.RepositorioDiccionario;
    public class RepositorioDiccionarioEnFicheros implements RepositorioDiccionario {

        private final String rutaDiccionarios;

        public Optional<Diccionario> dameDiccionarioDe(String idioma){ // Esto sería una cagada grande
            // ...                                           // Desde Java 1.8, SonarQube me escupe
                                                             // este código a la cara
                                                             // Esto podría hacer que en futuro violasemos el principio de Substitución de Liskov
        }
        public boolean tienesDiccionarioDe(String idioma){
            // ...
        }
    }

## Paquete com.diccionarios.app

```java
    import com.diccionarios.common.Diccionario;
    import com.diccionarios.common.RepositorioDiccionario; // Ahora trabajo a nivel de API
    //import com.diccionarios.common.RepositorioDiccionarioEnFicheros; // Pero al hacer esto, me acabo de cagar de nuevo en el ppo de inversión de dependencias.
        // ESA LINEA ES LA MUERTE DEL PROYECTO
    public class Aplicacion {

        //... entre otras cosas

        public void procesarPeticion(String idioma, String palabra, RepositorioDiccionario repositorio ) { // Esto es un patrón de inyección de dependencias
            //RepositorioDiccionario repositorio = new RepositorioDiccionarioEnFicheros(); // ESTA LINEA COMPILA? Ahora si... pero RUINA!
            // Para resolver esto, puedo optar por distintas estrategias: PATRONES DE DESARROLLO: Patrón Factoría, Patrón singleton, Patrón de Inyección de dependencias
            Optional<Diccionario> potencialDiccionario = repositorio.dameDiccionarioDe(idioma);
            if(potencialDiccionario.isPresent()){
                Diccionario diccionario = diccionario.get();
                if(diccionario.existe(palabra)){
                    Optional<List<String>> significados = dic.obtenerLosSignificadosDeUnaPalabra(palabra);
                    if(significados.isPresent()){
                        interfazUsuario.mostrarSignificado(significados.get());
                    } else {
                        interfazUsuario.mostrarPalabraNoEncontrada();
                    }
                } else {
                    interfazUsuario.mostrarPalabraNoEncontrada();
                }
            } else {
                interfazUsuario.mostrarDiccionarioNoEncontrado();
            }
        }

    }
```
Oye Iván.. eso esta muy bien... pero, realmente lo único que has hecho es pasar el problema a otra parte del código.
Al final, lo que tengo es JAVA.
En algún sitio, habrá que hacer: `new RepositorioDiccionarioEnFicheros()`
Y allá donde sea que escriba ese código, ya tengo la fiesta montada.

Bueno.. es cierto... pero aquí es donde entra la inversión de control.
Si hago que esa función no la llame yo, sino que la ejecute Spring... entonces, el problema ya no es mío. Es de Spring.
Y Spring será quien tenga que escribir `new RepositorioDiccionarioEnFicheros()`

En cualquier caso hay que escribirlo. Lo que quiero es no escribirlo yo. Prefiero que Spring lo GENERE de forma dinámica por mi.
Y si algo cambia, que lo regenere Spring, en base a las nuevas DECLARACIONES que hagamos.

Y Spring, escribirá esa linea en tiempo de ejecución. AL MENOS HOY EN DIA.
Antaño, no era así. En Spring teníamos ficheros XML donde decíamos que clase queríamos que Spring instanciase (hiciera un new) cuando alguien necesitase un objeto de un tipo concreto.

Ya era un paso... Centralizábamos la declaración de las dependencias en un único sitio. Pero era un coñazo. Y si cambiaba algo, había que ir a buscar en todos los XMLs donde se declaraba esa dependencia. TOTAL !!!

Hoy en día se resuelve de otra forma MUY DIFERENTE! Y es lo que vamos a ver en esta formación.
---

                    RepositorioDiccionarioEnFicheros
                        |
                        v
    Aplicación ---> RepositorioDiccionario ---> Diccionario
            ---> Diccionario
                        ^
                        |
                    DiccionarioEnFichero

    El objetivo es simple: A mi app de consola le debe importar el tipo de almacenamiento que se use para guardar los diccionarios? NO 
    Si le importa, el problema es que en el futuro cambia el tipo de almacenamiento, y eso me obliga a cambiar la app de consola: VAYA MIERDA DE MANTENIBILIDAD

---


Un producto de software es algo que se desarrolla, se usa y require de un MANTENIMIENTO y de EVOLUTIVOS.

Un coche es un producto que se desarrolla, se usa y requiere de un mantenimiento.

# Principios SOLID de desarrollo de software

Para qué sirven los principios SOLID?

Ayudarnos a escribir código que sea fácil de mantener, de entender, de extender y de probar.

## ¿Pero qué son? SON PRINCIPIOS.

Los principios (en general) son creencias o verdades fundamentales que nos ayudan a guiar nuestras acciones y a tomar decisiones. No me dicen cómo proceder. Sino las cosas a las debemos prestar atención o dar importancia... en mi vida (personal o profesional).

## Son 5 principios

Es adecuado el número... no se. Cada letra de la palabra SOLID corresponde a un principio.

S - Single Responsibility Principle
O - Open/Closed Principle
L - Liskov Substitution Principle
I - Interface Segregation Principle
D - Dependency Inversion Principle

# Inversión de dependencias

Un componente de alto nivel no debería depender de la implementación de un componente de bajo nivel. Ambos deberían depender de abstracciones (interfaces). Se llama así porque la dependencia con respecto a la implementación se invierte.

    Clase A -> Clase B                  ESTO ROMPE CON EL PRINCIPIO DE INVERSION DE LA DEPENDENCIA
    Clase A -> Interface <- Clase B     Esto cumple con el principio de inversión de la dependencia
    Si os fijáis la flecha que antes apuntaba hacia la clase B ahora apunta hacia la interfaz.

Una consecuencia de respetar el principio de inversión de dependencias es que vamos a poder hacer pruebas unitarias. Y me temo que sin esto, el hacer pruebas unitarias, POR DEFINICIÓN, es imposible.

Y muchos creeréis que habéis hecho pruebas unitarias... y de esos pocos seréis los que realmente las hayáis hecho.

# Inyección de dependencias

Patrón de desarrollo de software por el cuál las clases NO CREAN INSTANCIAS DE LOS OBJETOS QUE NECESITAN... si no que le son suministrados desde el exterior.

# Inversión de control

Básicamente consiste en delegar el flujo de la aplicación a un framework. Es el contenedor el que se encarga de llamar a los métodos de nuestra aplicación (al menos a algunos de ellos).
Pero el cambio es más grande. Cuando trabajamos con Spring, cambiamos el paradigma de programación... y esto, a priori, cuando no se ha trabajado con Spring, puede ser un poco chocante.

## Paradigma de programación

Son simplemente una forma hortera de referirnos a las distintas formas que tenemos de usar un lenguaje.
En los lenguajes naturales(los que hablamos), hay distintos paradigmas: 

    > Felipe, pon una silla debajo de la ventana: IMPERTATIVA: Por la intención: DOY UNA ORDEN. 
                                                               Por otro lado, el tiempo verbal es el imperativo.
    mkdir : make directory
Estamos muy acostumbrados al uso del lenguaje imperativo. pero es una mierda!
Por un motivo... nos hace olvida nuestro objetivo. Nos hace olvidar el QUÉ queremos conseguir, y nos centramos en el CÓMO conseguirlo.

    Felipe. SI (IF) hay algo que no sea una silla debajo de la ventana,
        Lo quitas (Imperativo)
    Felipe. SI (IF) no hay una debajo de la ventana,
        Felipe, IF no hay sillas, 
            vete al ikea y compra una silla (Imperativo)
        Pon una silla debajo de la ventana (Imperativo)

    Cuál era mi objetivo? Tener una silla debajo de la ventana.

    Podría haber dicho: Felipe: Debajo de la ventana ha de haber una silla. (ESTO ES IMPERATIVO? NO)
                                Eso es desiderativo... expreso un deseo, un objetivo.
                                En informática, esto se llama PROGRAMACIÓN DECLARATIVA
    Hoy en día, las herramientas, lenguajes, frameworks, que más triunfan son las que nos permiten expresarnos con lenguaje declarativo:
    - Docker-compose
    - Kubernetes / OpenShift
    - Terraform
    - Ansible
    - Angular
    - Spring

Nos encantan los lenguajes declarativos. Nos ayudan a centrarnos en el objetivo.. y no perdernos en los detalles... en el como conseguir algo.

    Spring es un framework que nos permite trabajar con un paradigma de programación declarativo.

La clave es que el framework es el que pone el flujo.. y eso nos desquicia un poco al principio. El flujo no está... al menos en nuestro código. El flujo está dentro del framework.Y una de las cosas que implica aprender Spring es aprender el flujo que impone Spring.

Nuestra función main en java va a ser 1 línea de código. 
```java
public class Aplicacion {
    public static void main(String[] args){
        SpringApplication.run(Aplicacion.class, args); // Spring, ejecuta tu mi app
                                                       // Inversión de control (Delegamos el flujo a Spring)
    }
}
```

Oye y qué cojones hace tu app? explícame algo!!!
Y nuestro trabajo es contarle a Spring (Declarar) que debe hacer nuestra app... En realidad más que que debe hacer, le contaremos lo que queremos que TENGA nuestra aplicación.

> Imagina que quiero montar un proceso Batch: Un proceso que:

- Lea datos de personas de un fichero
- Ah! y que solo lo haga para los mayores de edad.
- Ah! y que cuando acabe también me mande un correo
- Ah! y que si todo va bien con los datos de las personas, los meta en una BBDD
- Ah! y que cada persona la valide... sus datos. En concreto el DNI: que sea válido
- Ah! y también que calcule la edad de la persona (desde la Fecha de nacimiento. Ese dato también va a la base de datos)
- Ah! y que cuando empiece me mande un correo

¿Qué hemos hecho? Que son esos puntos que he escrito? REQUISITOS funcionales
Eso es lo que quiero que haga mi sistema... estoy dando flujo?

## Vamos a darlo:

Escribo el código:

1. Envia un email de inicio de carga
2. Abre un fichero
3. Lee linea a linea [BUCLE]
   1. Lee los datos de la persona, de la linea
   2. Calcula la edad
   3. Valida el DNI
   4. Filtra por mayores de edad
   5. Guarda en la BBDD
4. Cierra el fichero
5. Envia un email de fin de carga

Qué lenguaje estamos usando? Imperativo

Y arriba, al definir los requisitos? Declarativo

De hecho, Spring tiene una librería que se llama Spring Batch que nos permite hacer esto de forma declarativa.

Esto es la inversión de control!

# Qué es Spring?

Es un Framework para el desarrollo JAVA (Kotlin) que proporciona Inversión de Control.
Spring contiene como framework más de 200 librerías que se pueden utilizar para el desarrollo de aplicaciones empresariales.

La clave de Spring está en la forma en la que nos ayuda a adoptar/usar un patrón de Inyección de dependencias. A su vez, esto es consecuencia de ser un framework que ofrece Inversión de Control.

---

Las cosas han cambiado... y es normal... nuevos tiempos, nuevos problemas, nuevas necesidades.

Los frameworks no evolucionan porque si. Y no evolucionan de forma aislada.
Los lenguajes, los frameworks, las herramientas, las arquitecturas, las metodologías, las culturas de trabajo... todo evoluciona conjuntamente. Con un objetivo: cubrir las nuevas necesidades  que van surgiendo y enfrentar los nuevos problemas que van apareciendo.

No podemos mirar a Spring de forma aislada... si intento llevar Spring a las formas de trabajo de hace 15 años, no encaja! No encaja porque no está diseñado para eso.

    Spring <> SOLID <> Agile <> Devops <> Pruebas <> git <> maven

# Metodologías tradicionales -> Metodologías ágiles

## Metodologías waterfall y sus variantes: Espiral, V...

Al usar / seguir estas formas de trabajo, nos dimos cuenta que teníamos problemas. Y tratamos de buscarles solución = Metodologías ágiles

Que son otras formas de trabajo diferentes... Que resuelven los problemas que teníamos con las metodologías tradicionales... al menos la mayoría... pero cuidado, que han venido con sus propios problemas.

HITO1: Fecha: 10 de Octubre
       **Requisitos**: 1,2,3,4,5

       Si llegábamos al día 10 de Octubre y no estaba el R4... qué pasaba? Cómo se gestionaba?
       - PROYECTO EN RETRASO
       - OSTIAS PA TO LOS LAOS
       - Se planifica el HITO: HITO1 -> 20 de Octubre = VAMOS CON RETRASO

HITO2: Fecha: 10 de Noviembre
       Requisitos: 6,7,8,9,10

## De qué va eso de una metodología ágil? Cuál es / son los objetivos principales?

La característica principal de una metodología ágil es ENTREGAR EL PRODUCTO DE FORMA INCREMENTAL AL CLIENTE.
Con un objetivo: 
- Satisfacer al cliente
- Obtener feedback rápido

SPRINT1: **Fecha: 10 de Octubre**
         Requisitos: 1,2,3,4,5

        Qué pasa si llego al 10 de Octubre usando una metodología ágil.. y no esta el R4? Cómo lo gestiono?
       - PROYECTO EN RETRASO
       - OSTIAS PA TO LOS LAOS
       - Se instala el R1, R2, R3 y R5 en producción el día que estaba acordado.
       - El R4, se deja para el siguiente sprint

SPRING2: Fecha: 10 de Noviembre
         Requisitos: 6,7,8,9,10

ITERACIONES EN LAS METODOLOGIAS AGILES vs HITOS en las metodologías tradicionales
- Le damos importancia a la fecha sobre los requisitos
- Una iteración va asociada a una INSTALACION EN PRODUCCION (satisfacer al cliente y obtener feedback rápido)
- No hago el gilipollas como antaño... que planificaba el día 1, los 20 hitos del proyecto... incluyendo los del año que viene al máximo nivel. Hoy en día me encargo de planificar ESTE SPRING(ESTA ITERACION)... la siguiente... BUENO... ya veremos...

... Claro.. esto parece que esta guay.. y de hecho nos está mostrando que funciona.
Pero... también tiene su contrapartida.. y eso no me lo cuentan en los cursos de SCRUM.

Cada iteración que hemos dicho que implica? INSTALACION EN PRODUCCION
                                            INSTALAR EN PREPRODUCCION
                                            Y pruebas a nivel de PRODUCCION

ITERACION 1: + 5% de funcionalidad
    Instalar en preproducción
    Pruebas en preproducción         Qué pruebo? 5% de la funcionalidad
    Instalar en producción

ITERACION 2: + 10% de funcionalidad
    Instalar en preproducción
    Pruebas en preproducción         Qué pruebo? 10% de la funcionalidad + 5% de la anterior (a ver si he roto algo)
    Instalar en producción

Las pruebas se multiplican... con respecto a las metodologías tradicionales.
Y las instalaciones se multiplican... con respecto a las metodologías tradicionales.

Y de donde sale pasta? Y los recursos? Y el tiempo? No hay, ni pasta, ni recursos, ni tiempo para hacer todo esto. SIMPLEMENTE NO LA HAY.

Y entonces? cuál es la solución? AUTOMATIZAR TODO: Instalaciones, pruebas, empaquetados compilaciones... TODO.
Evidentemente NO PUEDO IR a una metodología ágil sin tener una cultura de automatización, sin adherirme a las prácticas de Devops.

## Extraído del manifiesto ágil:

> El software funcionando es la MEDIDA principal de progreso       > Define un indicador para un cuadro de mando!

La MEDIDA principal de progreso es el software funcionando        (**)
         <- adjetivo
         <--------- complemento preposicional
-------------------------------    ----------------------
            SUJETO                    COPULA O ATRIBUTO

La forma en la que debemos MEDIR (INDICADOR) el grado de avance de nuestro proyecto (progreso) es el concepto: SOFTWARE FUNCIONANDO.

Y por qué esta ésto ahí?

Cómo medíamos antes el grado de avance de un proyecto los JEFES DE PROYECTO?        HITOS
Y cómo sabía si se habían cumplido los hitos? si se habían implementado los requisitos comprometidos en el hito

   > Preguntar al desarrollador: Has implementado el R1? Sí

Qué significa SOFTWARE FUNCIONANDO? Software que se ajusta a requisitos... a lo que se espera de él.
Quién dice que el software está funcionando, listo? está ajustado a requisitos?
    - El CLIENTE.. PERDÓN??? Al cliente le debe llegar un producto que funcione, que esté acabo, que cumpla con requisitos!
    - Evidentemente LAS PRUEBAS

HHoy en día, para ver el grado de avance de un proyecto, miro a ver cuántas nuevas pruebas he superado en una unidad de tiempo.
¿Esta semana, cuántas pruebas he superado? 10: VAMOS BIEN
Oye.. y cuántas pruebas hemos superado en total? 1: VAMOS COMO EL CULO

Además, me interesa saber no una cifra absoluta sin contexto... sino una cifra relativa... con respecto a el total de pruebas a superar.
- Hemos pasado 10 pruebas de 15: VAMOS BIEN
- Hemos pasado 10 pruebas de 100: VAMOS COMO EL CULO

---

# Devops

Devops es una cultura, una filosofía, un movimiento, en pro de la AUTOMATIZACION !
Vamos a automatizar, chic@s!!! Qué? Todo lo que hay entre el DEV-> OPS

ETAPAS/ TRABAJOS DISTINTOS EN EL CICLO DE VIDAS DE UNA APLICACIÓN

            Automatizable?          Herramientas para automatizarlo?
- Plan            NO
- Code            NO
- Build           SÍ
                                        Java: MAVEN, GRADLE, SBT, ANT
                                        C#:   MSBUILD, DOTNET, NUGET
                                        JS:   NPM, YARN, WEBPACK
-------------------------------------------------------------------------------> Desarrollo ágil.
- Test              
    Definición    NO
    Ejecución     SI
                                        Frameworks de pruebas:
                                            JUNIT, TESTNG, MSTEST, NUNIT, TESTUNIT, JEST, MOCHA, JASMINE
                                            Cucumber, JBehave
                                            Selenium, Appium, Katalon
                                            SoapUI, Postman, ReadyAPI, Karate
                                            JMeter, Gatling,LoadRunner 
                                            SonarQube, JaCoCo, Clover
Eso si... para hacer pruebas lo primero que necesito es un entorno donde ejecutarlas.
    - Me valen las pruebas realizadas en la máquina del desarrollador?    PARA NADA ! Ese máquina puede esta maleada! NO ME FIO!
    - Me valen las pruebas realizadas en la máquina del tester?           PARA NADA ! Ese máquina puede esta maleada! NO ME FIO!
    - Me valen las pruebas realizadas en un entorno precreado de Pruebas? PARA NADA ! Ya ni eso... NO ME FIO!
                                                                          Después de 50 instalaciones, ese entorno está MALEADO!
    - Hoy en día la tendencia es a crear entornos de USAR y TIRAR. Creo el entorno para hacer las pruebas... hago las pruebas y me cargo el entorno... Y mañana dios dirá! Porque si parto de un entorno limpio cada vez, SI ME FIO!
      - Quién me ayuda con esto? CONTENEDORES !
-------------------------------------------------------------------------------> INTEGRACION CONTINUA:
    Tener CONTINUAMENTE la última versión del código instalada en un entorno de pruebas y sometida a pruebas automáticas.
    PREGUNTA: Cuál es el producto de un pipeline de Integración Continua? UN INFORME DE PRUEBAS EN TIEMPO REAL (**)
- Release: El hecho de liberar un artefacto. De poner en manos de mi cliente un producto utilizable en su entorno de producción
                 SI -> Nexus, Artifactory
-------------------------------------------------------------------------------> ENTREGA CONTINUA: CONTINUOUS DELIVERY
Cada vez que hay una versión nueva (RELEASE) la pongo en manos de mi cliente en automático
- Deploy:        SI \
-------------------------------------------------------------------------------> DESPLIEGUE CONTINUO: CONTINUOUS DEPLOYMENT
Cada vez que hay una versión nueva (RELEASE) la pongo en producción en automático
- Operate        SI  > Kubernetes (Openshift, Tanzu, Karbon, Rancher)
- Monitor        SI /
-------------------------------------------------------------------------------> DEVOPS (CULTURA DE AUTOMATIZACION)

Por cierto... con esas herramientas automatizo tareas PUNTUALES. Pero eso no es suficiente... NECESITO AUTOMATIZAR la ORQUESTACION DE TODAS ESAS TAREAS: Servidor de Automatización: Jenkins, Bamboo, TeamCity, GitLabCI, CircleCI, TravisCI, GitHubActions

---

# Pruebas

## Vocabulario en el mundo del testing

CAUSA RAIZ  El motivo por el que cometemos un error es lo que llamamos la causa raíz.
ERROR       Un error es cometido por un ser humano (por estar despistado, cansado, falto de conocimiento). Ya se sabe... errar es humano.
DEFECTO     Al cometer un error, un humano puede introducir un DEFECTO en el producto.
FALLO       Ese DEFECTO puede (o no) manifestarse al usar el producto. 
            Si se manifiesta, lo llamamos un FALLO = Desviación del comportamiento esperado.


## Para qué valen las pruebas?

- Asegurar el cumplimiento de unos requisitos
- Detectar fallos al operar/usar el producto. Pero el fallo no es el objetivo. El objetivo es encontrar el DEFECTO.
  - Por tanto, las pruebas también deben proporcionar toda la información disponible para ayudar a la rápida identificación de defectos desde los fallos que provocan. El procedimiento de llegar a un defecto desde un fallo, se denomina DEPURACION o DEBUGGING.
- Detectar DEFECTOS en el producto, sin necesidad de llegar a usar ese producto (sin necesidad de ejecutarlo).
- En ocasiones, me puede interesar hacer un análisis de causas raíces... para tomar acciones preventivas que eviten nuevos ERRORES > DEFECTOS > FALLOS en el futuro.
- Identificar áreas de mejora en el producto
- Para saber cómo vamos en el proyecto? A tiempo? retrasados? adelantados? A través de los informes de pruebas.  (**)
- Y para muchas más cosas

## Tipos de pruebas

Hay muchas formas de clasificar las pruebas... paralelas entre si!

## Según el objeto de la prueba

- Funcionales:      Las que atienden a requisitos funcionales
- No funcionales:   Las que atienden a requisitos no funcionales
  - Rendimiento
  - Carga
  - Estrés
  - Seguridad
  - UX
  - ...

### Según el nivel de prueba: (SCOPE) 

Qué sabéis de fabricación de bicicletas? NPI = GENIAL !!!

    Decathlon
        Ruedas
        Sillín
            Lo probaré. Montado sobre 4 hierros mal soldaos
                - Cómodo: Unitaria de UX
                - Si aguanta a un tio de 150 kgs o se desmorona? Unitaria de carga
                - Si después de usarlo un mes, el cuero no se ha desgastado? Unitaria de estrés
        Sistema de frenos
            Lo probaré.
                Voy a montar 4 hierros mal soldaos (bastidor), a ese le ancho el sistema de frenos:
                - ejecuto la función: apretarPalancaDeFreno()
                - resultado esperado: que las pinzas cierren... y a lo mejor con cierta presión (que mediré con un sensor que ponga en medio de las pinzas)
        Cuadro
        Dinamo/batería y una luz integrada en el cuadro
            Lo probaré... Tengo cuadro donde instalarlo? Si.. o no....
                Puedo montar 4 hierros mal soldaos (bastidor), sujetar allí (atornillar.. soldar... ese tinglao y probar...)
                Voy a girar la dinamo (con una palanca mismo) y ver si la luz se enciende

- Unitarias         Se centra en una característica única (como cualquier otra prueba) de un componente AISLADO
                    Y al aislarlo, si falla algo, se que es lo que estoy probando... no otra cosa. 

                    Pregunta: Si todas estas pruebas van bien.. me garantiza eso que la bici va a funcionar? NO
                    Entonces, para qué las hago? Qué gano? CONFIANZA +1
                    Vamos bien... estamos dando pasos en firme.

- Integración       Se centra la COMUNICACION entre 2 componentes

                    Integro el sistema de frenos con la rueda...monto las 2 cosas en un bastidor... con la rueda entre las pinzas.
                    - ejecuto la función: apretarPalancaDeFreno()
                    - resultado esperado: si la rueda frena.
                       Y mira que no... Resulta que la rueda es demasiado estrecha (llanta) para las pinzas (lo que cierran) y las pinzas no llegan a COMUNICAR la fuerza de rozamiento a la rueda.
                       Tengo un problema con las ruedas? NO
                       Tengo un problema con el sistema de frenos? NO
                       Lo que tengo es un problema de INTEGRACION entre el sistema de frenos y las ruedas.
                    Y menos mal que lo hemos detectado rápido.. anda que si la nueva rueda (he tenido que cambiar a una mas ancha) no entra en el cuadro...

                    Pregunta: Si todas estas pruebas van bien.. me garantiza eso que la bici va a funcionar? NO
                    Entonces, para qué las hago? Qué gano? CONFIANZA +1
                    Vamos bien... estamos dando pasos en firme.

- De sistema
    Me subiré en la bici... y me haré 50 kms... a ver si llego vivo. ESTA PRUEBA LA TENGO QUE HACER SIEMPRE

    Pregunta: Si todas estas pruebas van bien.. me garantiza eso que la bici va a funcionar, cumple requisitos? SI, para eso son!
    Pregunta2: Y entonces... si todas mis pruebas de sistema van bien, necesito hacer pruebas unitarias y de integración? NO, para qué.

    2 trucos:
    - "si van bien": Y si van mal? que ha fallao? NPI... ponte a averiguarlo
    - Cuándo puedo hacer estas pruebas? Cuando tengo el sistema acabado... y mientras tanto? voy a ciegas? a ver si llego?

## Esto mismo ahora aplica al software

Nuestro software va a tener muchos componentes... Con DEPENDENCIAS UNOS DE OTROS.

    Aplicación -> RepositorioDeDiccionarios -> Diccionarios

    P.Unitaria? Para poder hacer pruebas unitarias, por definición, hemos de AISLAR LOS COMPONENTES. 
    Necesitaremos montar 4 hierros mal soldaos (un bastidor) y unos sensores para aislar la aplicación del proveedor de diccionarios.
    En su lugar, lo apoyo en un bastidor: MOCKS: Test-Doubles: Fakes, Stubs, Spies, Mocks, Dummies (Martin Fowler)
    Y tendré que montar un RepositorioDeDiccionariosMock... donde pueda atornillar la aplicación y probarla de forma aislada (es decir, sin estar conectada al RepositorioDeDiccionarios real)
    El mock se que no falla.. es solido... al fin y al cabo: 4 hierros mal soldaos.

    Es imposible hacer pruebas unitarias sin AISLAR COMPONENTES... y para poder aislarlos, necesitamos que no haya DEPENDENCIAS RIGIDAS entre ellos. Una de las gracias de usar un framework como Spring es que nos ayuda a desacoplar componentes... y a poder realizar pruebas unitarias y de integración, que de otra forma, por definición, serían imposibles...
    y entonces no podría saber QUE TAL VOY... n podría adoptar una metodología ágil... ni nada! SE CAE TODO
    Porque todas estas son piezas que encajan entre si.

### Según el procedimiento de ejecución

- Dinámicas: Implican ejecutar el software    -----> FALLOS
- Estáticas: No implican ejecutar el software -----> DEFECTOS: SonarQube, JaCoCo, Clover

### Según el conocimiento del objeto de la prueba

- De caja negra: No se conoce el código fuente
- De caja blanca: Se conoce el código fuente

### Otras

- Regresión: Cuando vuelvo a ejecutar las pruebas que ya había pasado a ver si he roto algo después de hacer cambios
- ...


## TDD: Test Driven Development

Para saber de antemano cuántas pruebas tengo que superar, necesito de antemano definir las pruebas que voy a hacer.
Y esto además es algo natural... en cualquier industria... Y NO NOS ENTRA EN LA CABEZA NI A OSTIAS a los desarrolladores!

TDD implica:
- Automatizar la ejecución de las pruebas
- Definir las pruebas antes de implementar la funcionalidad (escribir el código) ESTO ES LO QUE NOS VUELVE LOCOS A LOS DESARROLLADORES
- Refactorizar del código en cada iteración

Esto nos ayuda a ir infinitamente más rápido en el proyecto.

        MADRID              GUADALAJARA                                                                         BARCELONA
                                YO                                                                               DESTINO
                                    coche ---> 6 horas
                  <-------- coche + 40 min coche   
        Aeropuerto ---------------------------------------------------------------------------------------------> 3 horas

Nos gusta mucho el cacharreo a los desarrolladores... ESCRIBIR CODIGO!!!
Automatizar las pruebas implica, dejar de hacer pruebas. Pasamos a escribir más código: Programas para probar nuestro programa.

---

-->
Las formas en las que puedo decir a Spring lo que quiero que utilice cuando alguien necesite un objeto de un tipo concreto
Las formas en las que puedo pedirle a Spring que me de un objeto de un tipo concreto