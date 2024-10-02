
# Programacion funcional = EUREKA !!!!!

Ya era hora (hasta puñetero Java 1.8 no lo pusieron) de tener programación funcional en JAVA.

## ¿Qué es la programación funcional?

Esto no es eso de las flechas.. ni lo de los streams...

Decimos que un lenguaje de programación soporta paradigma funcional cuando me permite que una variable apunte a una función... y porteriormente ejecutar la función desde la variable. YA!

Y el tema no es lo que es la programación funcional. El tema es lo que puedo empezar a hacer cuando el lenguaje me permite eso:
- Pasar funciones a otras funciones como parámetros
- Crear funciones que devuelvan funciones como resultado
Y aquí es cuando la cabeza explota!

```java

String texto = "HOLA"; // Asignar texto a hola!

```
Una variable no es una cajita donde pongo cosas.. 
Al menos en JAVA o en PYTHON o en TS o en JS... En C si o C++.
En JAVA una variable es una referencia aa un objeto
3 cosas ocurren ahí:
- "HOLA"            Crea en RAM un objeto de tipo String con valor "hola"
- String texto      Crea una variable llamada texto que puede apuntar a objetos de tipo String 
                      // JAVA ES UN LENGUAJE DE TIPADO FUERTE!
- =                 Asigno la variable al dato

```java
texto = "ADIOS";
```
- "ADIOS"           Crea en RAM un objeto de tipo String con valor "adios"
                    Dónde? en el mismo sitio sonde estaba "HOLA" o en otro?  En otro
                    Y en ese momento en JAVA hay 2 String en RAM
- texto             Lo desacociamos del antiguo valor
                    "HOLA" queda huefano de variable... nadie le apunta... nadie le quiere.
                    Y en C, mediante la dirección de memoria, se podría acceder a ese objeto
                    Pero en JAVA no se puede. Se acaba de convertir en basura = GARBAGE
                    Y quizás o quizás no, el recolector de basura lo elimine en algún momento 
- =                 Asigno la variable a nuevo texto: "ADIOS"



