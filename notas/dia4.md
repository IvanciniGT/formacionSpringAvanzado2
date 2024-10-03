

Microservicio para la gestión de animalitos en AnimalitosFermin.com

    ServicioAnimalitos  ->  RepositorioAnimalitos
    .nuevoAnimalito() ·······> .save()
    - persistir los datos
    - enviar un email


    Lógica de negocio       Lógica de persistencia

    ServicioEmails
    .enviarEmail(datos) 

# UNITARIA 

    Desde el punto de vista de YO desarrollador del servicio de animalitos, cuál es mi RESPONSABILIDAD al implementar la función nuevoAnimalito()? Enviar un email? NO

    Mi responsabilidad es SOLICITAR el envío de un email.. no enviarlo.

    De cara a la prueba, voy a comprobar que se envíe un email? NO... no es mi responsabilidad
    Ahora qué si quiero comprobar? QUE HE SOLICITADO el envío de un email con los datos adecuados

    Para hacer estas pruebas (UNITARIAS) no me vale usar el Servicio de Emails real... ni el Repositorio de Animalitos real... Hago mocks... ME AISLO DE ELLOS: YA QUE:

      1. Pueden no estar desarrollados \
      2. Pueden tener errores          /  RUIDO EN LAS PRUEBAS

    ServicioDeEmailsDeCartonPiedra { // ESTO ES UN DUMMY (no hace nada)
        enviarEmail(Datos datos) { }
    }
    // En este caso, para la prueba unitaria NECESITO ASEGURAR QUE LA FUNCION HA SIDO LLAMADA CON LOS DATOS ADECUADOS: NECESITO UN SPY


     ServicioDeEmailsDeCartonPiedra { // ESTO ES UN SPY
        private Datos datos;
        public Datos getDatosConLosQueSeHaEnviadoElEmail() { 
            return datos;
        }
        enviarEmail(Datos datos) { 
            this.datos = datos;
        }
    }

    Yo siempre ejecuto al hacer la prueba una llamada a .nuevoAnimalito()
        Aquí me aseguro que esté solicitando el envío de un email
           Y que esé solicitando la persistencia de los datos

# INTEGRACION (siempre se hace 2 a 2 componentes)

Con cuantos componentes se integra mi componente(el que estoy desarrollando)? 2
Pues 2 juegos d pruebas independientes:
- Pruebas de Integración del Servicio de Animalitos con el Repositorio de Animalitos
        Pero ojo.. quiero seguir quitando ruido... Uso un servicio de emails DUMMY

    Yo siempre ejecuto al hacer la prueba una llamada a .nuevoAnimalito()
        Aquí me aseguro que los datos han sido persistidos (BBDD DE PEGA: H2)

- Pruebas de Integración del Servicio de Animalitos con el Servicio de Emails
        Pero ojo.. quiero seguir quitando ruido... Sigo usando el repositorio de animalitos de cartón piedra 
        Y por ejemplo 

    Yo siempre ejecuto al hacer la prueba una llamada a .nuevoAnimalito()
        Que en la bandeja de entrada POP3 o IMAP del usuario de turno hay un correo REAL! (servidor de correo de pega: MAILTRAP)


# SISTEMA (end2End) Si los junto todos

    Yo siempre ejecuto al hacer la prueba una llamada a .nuevoAnimalito()
        Aquí me aseguro que los datos han sido persistidos (BBDD REAL)
        Que en la bandeja de entrada POP3 o IMAP del usuario de turno hay un correo REAL! (SERVIDOR DE EMAIL REAL)


Esto es importante.. 
- Primero porque me permite ir probando las cosas según las tengo... no he de esperar a nadie
    - Servicio de emails: HAGO SU PRUEBA UNITARIA
    - Repositorio de Animalitos: HAGO SU PRUEBA UNITARIA
    - Servicio de Animalitos: HAGO SU PRUEBA UNITARIA
    - ServicioDeEmails x ServicioAnimalitos: HAGO SU PRUEBA DE INTEGRACIÓN
    - ServicioAnimalitos x RepositorioAnimalitos: HAGO SU PRUEBA DE INTEGRACIÓN
    - Servicio de Emails x ServicioAnimalitos x RepositorioAnimalitos: HAGO SU PRUEBA DE SISTEMA
    CONFIANZA +1 
    Necesito saber cómo voy (AGILES y además en sensato)
- Porque si algo falla, quiero rápidamente saber el origen del fallo. Si el día de mañana cambio algo... lanzo todas las pruebas.


Voy a meter en el Servicio de Animalitos la lógica de Envío de Emails? NO
- Mantenibilidad
  - Quiero tener las operaciones de Emails en un sitio aparte.,.. de forma que  si hay que cambiar algo en el envío de emails no tenga que tocar el servicio de Animalitos
- Reutilización
  - Las funciones de envío de emails quiero poder en un momento usarlas desde otros servicios no solo desde el de Animalitos... Veterinarios

---

mappers! y DTOs

# Principios SOLID de desarrollo de software

O: Principio Open/closed

Un componente debe estar cerrado para su modificación pero abierto para su extensión.
Debo poder extender la funcionalidad de un sistema sin necesidad de modificar lo que ya tengo hecho.
- PRIMERA ABERRACION: Dependerá del cambio concreto que deba hacer, el que tenga que modificar o no el código ya existente.
- SEGUNDA ABERRACION: No me voy a preocupar hoy de los cambios que vengan mañana! NPI de lo que me va a pasar mañana...
- Que no se entiende. A qué doy importancia.. a dónde va?
  El hecho es que el tio Bob lo aplica guay en el concepto de ARQUITECTURA LIMPIA.
    La importancia del principio está en la C closed... Y la idea es no que no haya que cambiar el código si viene una modificación. LA IDEA ES PROTEGER EL CODIGO ANTES CAMBIOS (al menos parte del código) 

Los principios SOLID están guay... en su idea... LOS ENUNCIADOS SON UNA MIERDA !

## Ingeniería de Software vs Ciencias de la Computación

Ciencias de la computación es una ciencia EXACTA: Como las matemáticas, física.
Ingeniería busca soluciones adecuadas a problemas, con limitaciones de recursos y tiempo.

Un componente debe estar cerrado para su modificación pero abierto para su extensión.
                                                                                                                Carga SQL
|-----FRONTAL------------|  v1.2.0                              v2.0.0                  v2.0.0                      v
Web-Angular  v2             CONTROLADOR_REST V1                 SERVICIO                REPOSITORIO                 BBDD
FORMULARIO -> SERVICIO      Lógica es exponer un servicio       Lógica de negocio       Lógica de persistencia      Lógica del dato
Lógica de     Lógica de comunicación                                                                                PL/SQL
captura de    con el backend
datos
** VALIDACION                                                   ** VALIDACION                                       LA UNICA
Por cortesia                                                    Por cortesia                                        QUE ES OBLIGATORIA

Guardo personas. Quiero guardar una persona con su DNI.
Me dicen que SOLO HAY UN ÚNICO SITIO DONDE PUEDO PONER LA VALIDACIÓN DEL DNI (que tiene un patrón correcto: 8 números y una letra y que la letra es la correcta)

Android v1                                                                                                             
iOs v1
                            DTO                                 DTO                     Entidades
                            AnimalitoRestDTOV1        <-->      AnimalitoDTO    <-->    AnimalitoEntity
                                nombre               mapper        nombre       mapper    nombre
                                edad                               edad                   edad
                                foto:byte[]?                       multimedia: List<Multimedia>
                                                                                          multimedia: List<Multimedia>
                                                                        ^
                                                        AnimalitoDTO nuevoAnimalito(AnimalitoDTO) 
                            CONTROLADOR_REST V2
                            DTO
                            AnimalitoRestDTOV2      <-mapper->
                                nombre  
                                edad
                                multimedia: List<Multimedia>

¿Qué consigo con esto? Que una capa no exponga Tipos de datos que no haya definido la propia capa
PERO PRA QUE???? TO ESTE FOLLON !!!

KISS 
- No te compliques la vida hoy.. Cuando sea necesario, ya lo harás

@Data
public class AnimalitoEntidad {
    private String nombre;
    private int fechaDeNacimiento;
}

@Data
public class AnimalitoDTO {
    private String nombre;
    private int edad;
}

@Data
public class AnimalitoRestDTOV1 {
    private String nombre;
    private int edad;
}

// Este tipo de cosas las implementamos con librerías que nos ayudan: MapStruct
public class Mapeador {
    public AnimalitoDTO aDTO(AnimalitoEntidad entidad) {
        return new AnimalitoDTO(entidad.getNombre(), entidad.getEdad());
    }
    public AnimalitoEntidad aEntidad(AnimalitoDTO dto) {
        return new AnimalitoEntidad(dto.getNombre(), dto.getEdad());
    }
}

public class MapeadorControlador{
    public AnimalitoDTO aDTO(AnimalitoRestDTOV1 dto) {
        return new AnimalitoDTO(dto.getNombre(), dto.getEdad());
    }

    public AnimalitoRestDTOV1 aRestDTO(AnimalitoDTO dto) {
        return new AnimalitoRestDTOV1(dto.getNombre(), dto.getEdad());
    }
}



---

Habitualmente trabajamos con protocolo HTTP(s)
Eso está guay.. siempre y cuando me interesen las funcionalidades que me ofrece HTTP

HTTP Sirve para comunicaciones SINCRONAS UNIDIRECCIONALES

    cliente -> Request -> Servidor -> Response -> Cliente

Y si quiero comunicaciones asincronas? o bidireccionales?

A lo mejor me interesa que el servidor se pueda poner en contacto con el cliente

    cliente ---mensaje---> Servidor
            <---mensaje---

    chat
    notificaciones

    WebSockets: Protocolo que opera sobre TCP/IP bidireccional y full-duplex y asincrono

        http://
        ws://

---

```java
@RequestMapping(value = "/buscarPalabra", method = RequestMethod.GET)
public ResponseEntity<List<String>> buscarPalabraQuery(@RequestParam(required = true) String idioma,
                                                        @RequestParam(required = true) String palabra){
    return buscarPalabras(idioma, palabra);
}
```

# Queremos pruebas unitarias? SIEMPRE !!!
        |                   |
        v                   v
    [Serv.Apps (http) -> CONTROLADOR -> Servicio -> Repositorio -> BBDD]

## Qué otros casos tengo que probar.. A TODOS LOS NIVELES

no pasan palabra
no pasan idioma

idioma no encontrado
palabra no encontrada

con los case de los idiomas y las palabras

30 pruebas! UFF son muchas!!!! Son las necesarias!

Ahora... lo puedo comparar con las veces que manualmente probaría esto.. a lo largo del ciclo de vida del proyecto... RIDUCULAS 30 pruebas.
Incluso comparandolas con las veces que voy a probar durante el desarrollo v1 ... RIDICULAS 30 pruebas

## Happy path

Dado
    que tengo un controlador de estos
    que tengo un servicio de BuscadorPalabras de carton-piedra (mock)
    que que ese servicio, cuando le pregunte por los significados de la palabra manzana en idioma español, devolverá: [Fruto del manzano]
Cuando
    me llaman a la función buscarPalabraQuery con idioma "es" y palabra "perro"
Entonces
    me devuelve un responseEntity con el código 200 y el cuerpo [Fruto del manzano]

# Queremos pruebas de Integración? SI, ya que tiene relación con otros componentes
## Con el buscador
Dado
    que tengo un controlador de estos
    que tengo un servicio de BuscadorPalabras de verdad de la buena
    que tengo un repositorio de cartón piedra
    y que ese repositorio, cuando le pregunte por los significados de la palabra manzana en idioma español, devolverá: [Fruto del manzano]
Cuando
    me llaman a la función buscarPalabraQuery con idioma "es" y palabra "perro"
Entonces
    me devuelve un responseEntity con el código 200 y el cuerpo [Fruto del manzano]

## Con el servidor de apps
Dado
    que tengo un controlador de estos
    y un servidor de apps
    que tengo un servicio de BuscadorPalabras de carton-piedra (mock)
    que que ese servicio, cuando le pregunte por los significados de la palabra manzana en idioma español, devolverá: [Fruto del manzano]
Cuando
    me llaman al endpoint /buscarPalabra con idioma "es" y palabra "perro"
Entonces
    me devuelve un HTTP Request con el código 200 y en el cuerpo del request un JSON con [Fruto del manzano]


# Queremos pruebas de SISTEMA? SI!, este es mi sistema (El microservicio!)

    [Serv.Apps (http) -> CONTROLADOR -> Servicio -> Repositorio -> BBDD]

Dado
    que tengo un controlador de estos
    y un servidor de apps
    que tengo un servicio de BuscadorPalabras guay
    y que tengo un repositorio guay
    y una bbdd guay
    y en la bbdd está la palabra manzana en idioma español, con significados Fruto del manzano
Cuando
    me llaman al endpoint /buscarPalabra con idioma "es" y palabra "perro"
Entonces
    me devuelve un HTTP Request con el código 200 y en el cuerpo del request un JSON con [Fruto del manzano]

# ^ OPCION 1: FUNDAMENTALISTA PURITANO TECNOLOGICO
# v OPCION 2: UN POCO MAS PRACTICA


    [Serv.Apps (http) -> CONTROLADOR -> Servicio -> Repositorio] -> BBDD

Dado
    que tengo un controlador de estos
    y un servidor de apps
    que tengo un servicio de BuscadorPalabras guay
    y que tengo un repositorio guay
    y el repositorio tiene cargado la palabra manzana en idioma español, con significados Fruto del manzano
Cuando
    me llaman al endpoint /buscarPalabra con idioma "es" y palabra "perro"
Entonces
    me devuelve un HTTP Request con el código 200 y en el cuerpo del request un JSON con [Fruto del manzano]
