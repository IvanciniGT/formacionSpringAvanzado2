

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