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
# Qué pruebas estoy definiendo? LAS DE SISTEMA

Característica: Poder buscar el significado de palabras mediante un microservicio

    # Este bloque, aplica a todos los escenarios
    Antecedentes: 
        Dado            Que tengo un microservicio en la ruta "/api/v1/buscar"
        Y               que tiene registrada la palabra "manzana" con significado "Fruto del manzano" para el idioma "ES"
        Y               que tiene registrada la palabra "melón" con significado "Fruto del melonero" para el idioma "ES"
        Y               que tiene registrada la palabra "melón" con significado "Persona con pocas luces" para el idioma "ES"
        Y               que tiene registrada la palabra "comer" con significado "Acción de ingerir alimentos" para el idioma "ES"
        Y               que no tiene registrada la palabra "archilococo" para el idioma "ES"
        Y               que no tiene registrado el idioma "de los elfos"

    Escenario: Buscar una palabra sin especificar idioma
        Dado            que tengo un objeto json,
        Y               que dentro tiene un campo "palabra" con valor: "manzana"
        Cuando          hago una petición POST con el objeto json,
        Entonces        debe devolver un código de respuesta HTTP 400
                        # BAD_REQUEST: No se ha especificado el idioma
    Escenario: Buscar una palabra sin especificar palabra
        Dado            que tengo un objeto json,
        Y               que dentro tiene un campo "idioma" con valor: "ES"
        Cuando          hago una petición POST con el objeto json,
        Entonces        debe devolver un código de respuesta HTTP 400
                        # BAD_REQUEST: No se ha especificado el idioma
    Escenario: Buscar una palabra sin especificar nada
        Dado            que tengo un objeto json,
        Cuando          hago una petición POST con el objeto json,
        Entonces        debe devolver un código de respuesta HTTP 400
                        # BAD_REQUEST: No se ha especificado el idioma

    Esquema del escenario: Buscar una palabra que existe en un idioma
        Dado            que tengo un objeto json,
        Y               que dentro tiene un campo "idioma" con valor: "<idioma>"
        Y               que dentro tiene un campo "palabra" con valor: "<palabra>"
        Cuando          hago una petición POST con el objeto json,
        Entonces        debe devolver un código de respuesta HTTP 200
        Y               debe devolver un objeto JSON, que contenga una lista de significados
        Y               ese objeto debe tener <numeros> significado(s) para la palabra
        Y               en la primera posición de la lista, debe contener el significado: "<primero>"
        Y               si tiene más de un significado, en la segunda posición de la lista, debe contener el significado: "<segundo>"
    
        Ejemplos:
            | palabra | numeros | primero            | segundo                 | idioma |
            | manzana | 1       | Fruto del manzano  |                         | ES     |
            | manzanas| 1       | Fruto del manzano  |                         | es     |
            | MANZANA | 1       | Fruto del manzano  |                         | es     |
            | melón   | 2       | Fruto del melonero | Persona con pocas luces | es     |
            | melones | 2       | Fruto del melonero | Persona con pocas luces | es     |
            | Melón   | 2       | Fruto del melonero | Persona con pocas luces | es     |
    
    Escenario: Buscar una palabra que existe en un idioma
        Dado            que tengo un objeto json,
        Y               que dentro tiene un campo "idioma" con valor: "ES"
        Y               que dentro tiene un campo "palabra" con valor: "comían"
        Cuando          hago una petición POST con el objeto json,
        Entonces        debe devolver un código de respuesta HTTP 404
                        # NOT_FOUND: La palabra no existe en el diccionario
                        # Se queda para la siguiente versión
    
    Escenario: Buscar una palabra que no existe en un idioma
        Dado            que tengo un objeto json,
        Y               que dentro tiene un campo "idioma" con valor: "ES"
        Y               que dentro tiene un campo "palabra" con valor: "archilococo"
        Cuando          hago una petición POST con el objeto json,
        Entonces        debe devolver un código de respuesta HTTP 404
                        # NOT_FOUND: La palabra no existe en el diccionario

    Escenario: Buscar una palabra en un idioma que no existe
        Dado            que tengo un objeto json,
        Y               que dentro tiene un campo "idioma" con valor: "de los elfos"
        Y               que dentro tiene un campo "palabra" con valor: "islandir"
        Cuando          hago una petición POST con el objeto json,
        Entonces        debe devolver un código de respuesta HTTP 404
                        # NOT_FOUND: El idioma no existe

    Escenario: Hay un problema en la aplicación
        Dado            que tengo un objeto json,
        Y               que dentro tiene un campo "idioma" con valor: "de los elfos"
        Y               que dentro tiene un campo "palabra" con valor: "islandir"
        Cuando          hago una petición POST con el objeto json,
        Entonces        debe devolver un código de respuesta HTTP 500
                        # INTERNAL_SERVER_ERROR: Error interno en la aplicación