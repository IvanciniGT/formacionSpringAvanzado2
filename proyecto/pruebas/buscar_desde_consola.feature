#language:es
# Para que vale este fichero:
# 1. Estoy revisando los requisitos de mi sistema PRUEBAS ESTATICAS
# 2. Estoy entendiendo el comportamiento que debe tener mi app
# 3. Lo estoy dejando patente para que no sea yo el único tenga esto en mi cabeza
# 4. Porque con el chatGPT o el Copilot, tardo menos en escribirlo que en explicarlo o pensar en ello
# 5. Porque este fichero define las pruebas de sistema que voy a hacer          \ Los tengo en un único fichero
# 6. Porque este fichero también está definiendo los requisitos de mi sistema   / Hay una única fuente de verdad
# 7. Porque este fichero va a ser ejecutable desde INTELLIJ o desde ECLIPSE. ESTE FICHERO SERA EJECUTABLE
#     Es que este fichero le estamos escribiendo en lenguaje GHERKIN, para su procesamiento por CUCUMBER (BDD)
# Qué pruebas estoy definiendo? LAS DE SISTEMA (caja blanca o caja negra? caja negra)
#      Imaginad la función altaDePalabra . En caja negra cuál sería la prueba? que me devuelva OK
#      Imaginad la función altaDePalabra . En caja blanca cuál sería la prueba? que me devuelva OK y que el dato esté en la BBDD

Característica: Poder buscar el significado de palabras en un idioma por consola

    # Este bloque, aplica a todos los escenarios
    Antecedentes: 
        Dado            Que tengo una aplicación de consola
        Y               que tiene registrada la palabra "manzana" con significado "Fruto del manzano" para el idioma "ES"
        Y               que tiene registrada la palabra "melón" con significado "Fruto del melonero" para el idioma "ES"
        Y               que tiene registrada la palabra "melón" con significado "Persona con pocas luces" para el idioma "ES"
        Y               que tiene registrada la palabra "comer" con significado "Acción de ingerir alimentos" para el idioma "ES"
        Y               que no tiene registrada la palabra "archilococo" para el idioma "ES"
        Y               que no tiene registrado el idioma "de los elfos"

    Esquema del escenario: Buscar una palabra que existe en un idioma
        Cuando          le paso idioma: "<idioma>"
        Y               le paso la palabra "<palabra>"
        Entonces        debe mostrar por consola <numero> significado(s) para la palabra
        Y               debe mostrar por consola el "<primero>" de la palabra  
        Y               si tiene más de un significado, debe mostrar por consola el "<segundo>" de la palabra
    
        Ejemplos:
            | palabra | numeros | primero            | segundo                 | idioma |
            | manzana | 1       | Fruto del manzano  |                         | ES     |
            | manzanas| 1       | Fruto del manzano  |                         | es     |
            | MANZANA | 1       | Fruto del manzano  |                         | es     |
            | melón   | 2       | Fruto del melonero | Persona con pocas luces | es     |
            | melones | 2       | Fruto del melonero | Persona con pocas luces | es     |
            | Melón   | 2       | Fruto del melonero | Persona con pocas luces | es     |

    Escenario: Buscar una palabra que no existe en un idioma
        Cuando          le paso idioma: "ES"
        Y               le paso la palabra "comía"
        Entonces        debe mostrar por consola: "La palabra no existe en el diccionario" 
                        # Se queda para la siguiente versión
    
    Escenario: Buscar una palabra que no existe en un idioma
        Cuando          le paso idioma: "ES"
        Y               le paso la palabra "archilococo"
        Entonces        debe mostrar por consola: "La palabra no existe en el diccionario"

    Escenario: Buscar una palabra en un idioma que no existe
        Cuando          le paso idioma: "de los elfos"
        Entonces        debe mostrar por consola: "El idioma no existe"

    Escenario: Hay un problema en la aplicación
        Dado            que tiene un problema interno
        Entonces        debe mostrar por consola: "Error interno en la aplicación"
