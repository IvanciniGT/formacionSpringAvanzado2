
def saluda():
    print("Hola")

saluda()

# Programación funcional:
miFuncion = saluda # Referencio a saluda
miFuncion() # Ejecutando saluda, a través de la variable miFuncion

def funcion_generadora_saludos(nombre):
    return "Hola " + nombre

def imprime_saludo(funcion_generadora_de_saludos, nombre):
    print(funcion_generadora_de_saludos(nombre))

imprime_saludo(funcion_generadora_saludos, "Juan")

