from sklearn.datasets import fetch_california_housing
import pandas as pd

# 1. CARGAR EL DATASET
california = fetch_california_housing(as_frame=True)

# 2. DEFINIR X y Y 
X = california.data['MedInc'].values[:50]
Y = california.target.values[:50]


# 3. CALCULAR LOS PROMEDIOS
x_mean = X.mean()
y_mean = Y.mean()

# 4. CALCULAR LA PENDIENTE (b1) Y EL INTERCEPTO (b0)
# b1 = suma((x - x_prom) * (y - y_prom)) / suma((x - x_prom)^2)
b1 = ((X - x_mean) * (Y - y_mean)).sum() / ((X - x_mean) ** 2).sum()
# b0 = y_prom - b1 * x_prom  (la recta pasa por el punto promedio)
b0 = y_mean - b1 * x_mean

print(f"Ecuación: Y = {b0:.4f} + {b1:.4f} * X")

# 5. PREDICCIONES CON LA RECTA
Y_pred = b0 + b1 * X

# 6. CALCULAR EL ERROR Y EL R*R
ss_res = ((Y - Y_pred) ** 2).sum()    # error que el modelo no explica
ss_tot = ((Y - y_mean) ** 2).sum()    # variación total de Y
r2 = 1 - ss_res / ss_tot
mse = ss_res / len(Y)                 # error cuadrático medio

print(f"R^2: {r2:.4f}")
print(f"MSE: {mse:.4f}")

# 7. PREDICCIÓN DE EJEMPLO
print(f"Predicción para MedInc = 5: {b0 + b1 * 5:.4f}")