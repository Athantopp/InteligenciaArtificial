# Aplicacion para probar en tiempo real el modelo ya entrenado.
# Requisito: haber corrido antes entrenar.py (genera digit_mlp.keras).

import os
import numpy as np
import tensorflow as tf
import tkinter as tk
from tkinter import filedialog, messagebox
from PIL import Image, ImageDraw, ImageTk

#Cargar el modelo previamente entrenado se busca junto a este archivo o en la carpeta actual
def buscar_modelo():
    carpeta_script = os.path.dirname(os.path.abspath(__file__))
    for carpeta in (carpeta_script, os.getcwd()):
        ruta = os.path.join(carpeta, "digit_mlp.keras")
        if os.path.exists(ruta):
            return ruta
    return None


ruta_modelo = buscar_modelo()
if ruta_modelo is None:
    raise SystemExit("No se econtro digit_mlp.keras")

model = tf.keras.models.load_model(ruta_modelo)
print("Modelo cargado desde:", ruta_modelo)


#Preparar la imagen solo adapta el dibujo o la foto al formato MNIST, no es parte del modelo

def preparar(img, invertir=None):
    """Deja la imagen en formato MNIST: 28x28, numero blanco sobre fondo negro, centrado.
    invertir: True/False para forzar; None = detectar solo (fondo claro -> invertir)."""
    arr = np.array(img.convert("L"), dtype=np.float32)      # a escala de grises

    if invertir is None:
        invertir = arr.mean() > 127                         # fondo claro -> invertir
    if invertir:
        arr = 255 - arr

    rango = arr.max() - arr.min()
    if rango < 1e-6:                                        # imagen vacia
        return None
    arr = (arr - arr.min()) / rango                         # normalizar a 0-1
    arr[arr < 0.2] = 0                                      # quitar ruido del fondo

    ys, xs = np.nonzero(arr)
    recorte = arr[ys.min():ys.max() + 1, xs.min():xs.max() + 1]   # recortar al numero
    h, w = recorte.shape

    escala = 20.0 / max(h, w)                               # lado mayor = 20 px (como MNIST)
    nuevo_w, nuevo_h = max(1, round(w * escala)), max(1, round(h * escala))
    pequeno = Image.fromarray((recorte * 255).astype(np.uint8)).resize(
        (nuevo_w, nuevo_h), Image.LANCZOS)
    pequeno = np.array(pequeno, dtype=np.float32) / 255.0

    lienzo = np.zeros((28, 28), dtype=np.float32)           # pegar en lienzo 28x28
    y0, x0 = (28 - nuevo_h) // 2, (28 - nuevo_w) // 2
    lienzo[y0:y0 + nuevo_h, x0:x0 + nuevo_w] = pequeno

    total = lienzo.sum()                                    # centrar por centro de masa
    yy, xx = np.indices(lienzo.shape)
    cy, cx = (yy * lienzo).sum() / total, (xx * lienzo).sum() / total
    lienzo = np.roll(lienzo, (round(13.5 - cy), round(13.5 - cx)), axis=(0, 1))

    return lienzo


#Interfaz grafica

TAM = 280          # tamano del recuadro de dibujo
GROSOR = 20        # grosor del pincel

# Imagen "paralela" donde se guarda lo que dibujas (negro de fondo, trazo blanco)
img_dibujo = Image.new("L", (TAM, TAM), 0)
draw = ImageDraw.Draw(img_dibujo)
ultimo = None                  # ultimo punto del trazo
mostrando_archivo = False      # True si el recuadro muestra una imagen cargada
pendiente = None               # prediccion programada (para no saturar mientras dibujas)

ventana = tk.Tk()
ventana.title("Reconocedor de numeros - MLP")
ventana.resizable(False, False)

tk.Label(ventana, text="Reconocedor de numeros", font=("Arial", 18, "bold")).grid(
    row=0, column=0, columnspan=2, pady=(10, 0))
tk.Label(ventana, text="Dibuja un numero").grid(
    row=1, column=0, columnspan=2, pady=(0, 5))

# Recuadro donde se dibuja (o se muestra la imagen cargada)
canvas_img = tk.Canvas(ventana, width=TAM, height=TAM, bg="black",
                       highlightthickness=1, cursor="cross")
canvas_img.grid(row=2, column=0, padx=15, pady=10)

panel = tk.Frame(ventana)
panel.grid(row=2, column=1, padx=15, pady=10, sticky="n")

lbl_resultado = tk.Label(panel, text="El numero es: -", font=("Arial", 20, "bold"))
lbl_resultado.pack(pady=(15, 5))

lbl_detalle = tk.Label(panel, text="", font=("Consolas", 11), justify="left")
lbl_detalle.pack()


def predecir(imagen, invertir):
    """Prepara la imagen, la pasa por la red y muestra el resultado."""
    x = preparar(imagen, invertir)                          # dejar la imagen como MNIST
    if x is None:
        lbl_resultado.config(text="El numero es: -")
        lbl_detalle.config(text="")
        return

    # Prediccion: aqui la imagen pasa por las capas de la red (forward propagation)
    probs = model(x[None, ...], training=False).numpy()[0]
    digito = int(probs.argmax())                           
       
    # Mostrar resultado
    lbl_resultado.config(text=f"El numero es: {digito}")


def limpiar():
    global img_dibujo, draw, ultimo, mostrando_archivo
    canvas_img.delete("all")
    img_dibujo = Image.new("L", (TAM, TAM), 0)
    draw = ImageDraw.Draw(img_dibujo)
    ultimo = None
    mostrando_archivo = False
    lbl_resultado.config(text="El numero es: -")
    lbl_detalle.config(text="")


# ----- Dibujar con el mouse (prediccion en tiempo real) -----
def predecir_dibujo():
    global pendiente
    pendiente = None
    predecir(img_dibujo, invertir=False)                    # fondo negro, trazo blanco: no invertir


def pintar(evento):
    global ultimo, pendiente
    if mostrando_archivo:                                   # si habia una imagen cargada, empezar de cero
        limpiar()
    r = GROSOR // 2
    if ultimo is not None:
        canvas_img.create_line(ultimo[0], ultimo[1], evento.x, evento.y,
                               fill="white", width=GROSOR, capstyle=tk.ROUND)
        draw.line([ultimo[0], ultimo[1], evento.x, evento.y], fill=255, width=GROSOR)
    canvas_img.create_oval(evento.x - r, evento.y - r, evento.x + r, evento.y + r,
                           fill="white", outline="white")
    draw.ellipse([evento.x - r, evento.y - r, evento.x + r, evento.y + r], fill=255)
    ultimo = (evento.x, evento.y)

    # Tiempo para predecir
    if pendiente is None:
        pendiente = ventana.after(200, predecir_dibujo)


def soltar(evento):
    global ultimo
    ultimo = None
    predecir_dibujo()


canvas_img.bind("<B1-Motion>", pintar)
canvas_img.bind("<ButtonRelease-1>", soltar)


botones = tk.Frame(ventana)
botones.grid(row=3, column=0, columnspan=2, pady=(0, 15))
tk.Button(botones, text="Limpiar", font=("Arial", 12), width=14,
          command=limpiar).pack(side="left", padx=5)

ventana.mainloop()