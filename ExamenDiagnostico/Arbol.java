/*

*/
public class Arbol{
    Nodo raiz;

public boolean vacio(){
   return raiz ==null;
}

public Nodo buscarNodo(String nombre){
    return buscarNodo(nombre, raiz);
}


public Nodo buscarNodo(String nombre, Nodo actual){
    if( actual == null){
        return actual;
    }
    if(nombre.equals((actual.nombre))){
        return actual;
    }
    Nodo resultado=  buscarNodo(nombre, actual.izquierda);

    if(resultado == null){
        return buscarNodo(nombre, actual.derecha);
    }
    else{
        return resultado;
    }
}



}