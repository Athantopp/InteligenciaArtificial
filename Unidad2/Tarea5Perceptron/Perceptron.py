# Datos de entrenamiento: tabla de verdad del AND
X = [(0, 0), (0, 1), (1, 0), (1, 1)]
y = [0, 0, 0, 1]

# Parámetros que el perceptrón va a aprender
w = [0.0, 0.0]   # un peso por entrada
b = 0.0          # sesgo (bias)
lr = 1         # tasa de aprendizaje

def predecir(x):
    z = w[0] * x[0] + w[1] * x[1] + b # suma ponderada 
    return 1 if z >= 0 else 0    # función escalon

# Entrenamiento
for epoca in range(10):
    errores= 0
    for xi, objetivo in zip(X, y):
        pred = predecir(xi)
        error = objetivo - pred # 0 si acerto, +1,-1 si fallo

        print(xi,pred, error)
        # Regla del perceptron ajustar solo si hubo error
        w[0] += lr * error * xi[0]
        w[1] += lr * error * xi[1]
        b += lr * error

        errores += abs(error)


    print(f"Epoca {epoca+1}: errores={errores}, w={w}, b={b:.1f}")
    if errores == 0:
        break

# Prueba
print("\nResultados:")
for xi in X:
   print(xi, "->", predecir(xi))