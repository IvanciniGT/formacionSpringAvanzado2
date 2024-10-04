
# Seguridad

Tokens de seguridad : JWT

Las apps web han cambiado mucho. Muchísimo.
Antiguamente las apps eran StateFull... Hoy en día los backends son StateLess.

Antiguamente a nivel de servidor teníamos el concepto de SESIÓN DEL USUARIO: HttpSession

Era un problemón trabajar así:
- Me tenía que responsabilizar en BACK de los detalles de la sesión de un usuario
- Necesito mucha RAM en servidor para eso.. y le cargo la CPU
- Y encima las tenía que guardar un montón de rato... aunque el usuario no estuviera haciendo nada.
  Configurábamos cosas como Timeouts de sesión (30 minutos)
- Limitación! Y si quiera además que un usuario pueda estar conectado desde 2 frontales? compartiendo SESSION!
- Replicación de sesiones en entornos de HA (Sticky Sessions): InfinySpan, Redis, Memcached

Las apps hoys en día a nivel de servidor son STATELESS.
No me responsabilizo de la sesión del usuario. Que los guarde el front... para eso lo he desjado.
El front ahora es una app Angular, React, Vue... y se encarga del frontal... interactuar con el usuario... y por ende de la sesión del usuario.

Una sesión era básicamente un Map<String, Object> con los datos de la sesión.
Internamente los servidores de aplicaciones guardaban eso en memoria... y lo replicaban entre ellos.
Eso si, los servidores de apps, identificaban cada sesión (cada MAP) con un ID único: JSESSIONID
Al conectar por primera vez un usuario con un servidor de apps, se le asignaba un JSESSIONID y se le enviaba en una cookie. A partir de ese momento, cualquier petición del usuario, enviaba el JSESSIONID en la cookie... y el servidor de apps lo usaba para identificar la sesión del usuario.

ESTO ROTO A DIA DE HOY. Cuando el cliente hace una petición, debe mandar TODOS los datos que sean necesario para atender la petición. Y el servidor no debe guardar nada de nada.

---

Además de esto... antiguamente en cada app teníamos nuestra propia gestión de usuarios, contraseñas, roles.
Y tenía mis tablas de usuarios.. contraseñas.. roles.. MIERDAS... que repetía de proyecto en proyecto!
Con el tiempo se fueron unificando un poquito las cosas: LDAP= BBDD centralizada de contraseñas.

Esto tenía muchos problemas:
- Cada app tenía su propia gestión de usuarios (datos diferentes y redundantes)
- Mogollón de trabajo que repetíamos de proyecto en proyecto
- Los desarrolladores (en general) no tenemos npi de seguridad... y la cagamos

---

Todo esto, hoy en día esta muy resuelto.
Trabajamos TODO con tokens de sesión: JWT (JSON Web Token).
Eso es un JSON (que se pasa en base64) que contiene los datos de la sesión del usuario:
- Su nombre, correo, roles, ... y cualquier cosa que necesitemos

Cuando lo necesito, genero ese token en BACKEND y se lo paso al FRONTEND.
Y desde ese momento, el FRONTEND se encarga de enviar ese token en cada petición.

Ese token además incorpora 2 datos:
- Fecha de expiración (30 minutos.. 3 días.. 1 año)
- Firma digital (para que el nadie pueda modificar el token sin que yo me de cuenta)

Además, esos tokens (su creación, gestión, validación) los delegamos en un servicios externos: IAM: Identity Access Management
- Keycloak
- Auth0
- AWS IAM
- Azure AD
- Google 
- Facebook

Quizás yo necesite algunos datos adicionales que no me pasa el IAM... muy específicos de mi app.
Y creo una tabla en mi BBDD con esos datos... Y asocio esos datos a un email(CLAVE).

# Identificación de usuarios

Que el cliente diga quién es.

# Autenticación

Que valide que el cliente es quién dice ser.

# Autorización

Sabiendo que eres quien dices ser... que puedes hacer.

---


USUARIO     NAVEGADOR           MotorJS   Servidor WEB      Servidor APP                    Servidor IAM
   ve a- >  http://miapp.com ------------->JS, HTML, CSS
            <-----------------------------
            --------------------> Generan HTML
            <--------------------
   click    --------------------> Y piensa?         
                                Uy.. para esto hace falta
                                estar logueado y tener permisos
            <-- REDIRECT: 300 ---
                http://myIAM.com
            --------------------------------------------------------------------------------> 
            <-------- mostrar formulario de login -------------------------------------------
se identifica--------------------------------------------------------------------------------> Lo autentica... y genera un TOKEN JWT
            <-------- TOKEN JWT... y un redirect de vuelta a la app -------------------------
            --------JWT--------> Lo valida... superficialmente:
                                    - Mira los datos
                                    - Que no haya expirado
                                    - Que la firma sea buena
                                 Y puede autorizarle (roles)
                                 -------------------------->
                                    petición + JWT          1º Validar al frontal (http://miapp.com) ah vale...
                                                            2º Validar el token en profundidad:
                                                                - Mirar los datos
                                                                - Que no haya expirado
                                                                - Que la firma sea buena
                                                                - Y le pregunta al IAM si no hay revocación
                                                                --------------------------> 1 Valida al servidor? Ah vale...
                                                                                               - Mira los datos
                                                                                               - Que no haya expirado
                                                                                               - Que la firma sea buena
                                                                                               - Y que no haya sido revocado (LOGOUT)
                                                                <---- Es bueno el JWT -----
                                                            Y entonces el backend hace la operación y resuelve todos los problemas del mundo.. o no!

USUARIO     NAVEGADOR           MotorJS   Servidor WEB      Servidor APP                    Servidor IAM

Evidentemente Spring viene muy preparado para trabajar con todo esto: Spring Security
- Nos permite integrar con IAM
- Incluso montar mi propio IAM (no tiene mucho sentido... pero se puede)
- DECLARAR fácilmente qué rutas necesitan autenticación y/o autorización
- Y probarlo fácilmente: Spring Security Test

---

# Como debo guardar una contraseña en BBDD? Encriptada... porque no tenemos npi de seguridad

NI DE COÑA GUARDO UNA CONTRASEÑA EN UNA BBDD!!!

Guardaré una huella (HASH) de la contraseña.
La única forma de poder calcular la contraseña sería mediante un ataque de fuerza bruta.
Y por eso, los sistemas buenos lo que haces es NO GUARDAR NI SIQUIERA UN HASH DE LA CONTRASEÑA...
sino un hash.. de un hash ... de un hash (y así 1024 veces) de la contraseña.

## Algoritmos de HASH (MD5, SHA-2048, letra del DNI)

Un función que:
- Dada un entrada siempre devuelve la misma salida
- Me garantiza poca probabilidad de que 2 datos distintos de entrada generen la misma salida
- Desde el dato de salida es imposible obtener el dato de entrada. La salida es un resumen del dato de entrada.

LETRA DEL DNI:
2300001 | 23
        +----------
      1   100000
      ^
      0-22 Y hay una letra asociada a cada número.


      ---

JWT:
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyODA4MTc3NCwiaWF0IjoxNzI4MDQ1Nzc0fQ.7rso9k3FhxUCS-Nx3ctE_8fjD_PbYvZ-USU8waw2uTw