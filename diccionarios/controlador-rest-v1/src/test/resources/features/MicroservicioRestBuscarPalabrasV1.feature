#language:es

  Característica: Controlador que expone por protocolo REST la funcionalidad del Servicio de Búsqueda de Palabras

    Antecedentes:  Tengo un sistema operativo, con su controlador, servicio y repositorio

    Esquema del escenario: Buscar una palabra que existe en un idioma

      Dado      en el repositorio tengo dado de alta el idioma "<idioma>"
      Y         en el repositorio tengo dada de alta la palabra "<palabra>" para el idioma anterior
      Y         en el repositorio tengo dado de alta el significado "<significado>" para la palabra anterior
      Y         que tengo un Objeto JSON
      Y         que ese objeto tiene una propiedad "idioma" con valor "<idioma>"
      Y         que ese objeto tiene una propiedad "palabra" con valor "<palabra>"

      Cuando    realizo una petición http "get" al endpoint "/api/v1/buscarPalabra2"
      Y         envío ese objeto JSON en el cuerpo de la petición

      Entonces  obtengo una respuesta HTTP
      Y         esa respuesta tiene por código de estado "OK"
      Y         el cuerpo de esa respuesta tiene un objeto JSON
      Y         ese objeto JSON contiene una lista elemento en la posición 1 es "<significado>"

      Ejemplos:

        | idioma | palabra | significado             |
        | ES     | manzana | Fruto del manzano       |
        | EN     | apple   | Fruit of the apple tree |

    Escenario: Buscar una palabra que no existe en un idioma

      Dado      en el repositorio tengo dado de alta el idioma "ES"
      Y         que tengo un Objeto JSON
      Y         que ese objeto tiene una propiedad "idioma" con valor "ES"
      Y         que ese objeto tiene una propiedad "palabra" con valor "archilococo"

      Cuando    realizo una petición http "get" al endpoint "/api/v1/buscarPalabra2"
      Y         envío ese objeto JSON en el cuerpo de la petición

      Entonces  obtengo una respuesta HTTP
      Y         esa respuesta tiene por código de estado "NO ENCONTRADO"


    Escenario: Buscar una palabra de un idioma inexistente

      Dado      que tengo un Objeto JSON
      Y         que ese objeto tiene una propiedad "idioma" con valor "el de los elfos"
      Y         que ese objeto tiene una propiedad "palabra" con valor "isnalvir"

      Cuando    realizo una petición http "get" al endpoint "/api/v1/buscarPalabra2"
      Y         envío ese objeto JSON en el cuerpo de la petición

      Entonces  obtengo una respuesta HTTP
      Y         esa respuesta tiene por código de estado "NO ENCONTRADO"
