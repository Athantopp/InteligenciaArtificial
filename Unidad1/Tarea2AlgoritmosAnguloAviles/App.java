

public class App {
    public static void main(String[] args) {
        //Casos de prueba

        // "7621 3458", 1234567 8, 524 73618
        //3X3 = 9
        String initialState = "7621 3458"; //random initial state
        String goalState = "12345678 "; //goal state (ejemplo, ver nota abajo)
        int limite = 26;
        int limiteMaximo = 35;
        System.out.println();
        SearchTree searchTree = new SearchTree(initialState, goalState);
        SearchTree t1 = new SearchTree(initialState, goalState);
        t1.breadthFirstSearch();
 
        SearchTree t2 = new SearchTree(initialState, goalState);
        t2.depthFirstSearch();
 
        SearchTree t3 = new SearchTree(initialState, goalState);
        t3.uniformCostSearch();
 
        SearchTree t4 = new SearchTree(initialState, goalState);
        t4.depthLimitedSearch(limite);
 
        SearchTree t5 = new SearchTree(initialState, goalState);
        t5.depthIterativeSearch(limiteMaximo);
 
        System.out.println();
        System.out.println("Estado inicial: " + initialState);
        System.out.println("=== Tabla comparativa ===");
        System.out.printf("%-22s %-12s %-12s %-12s %-12s %-10s%n",
                "Algoritmo", "Profund.", "Procesados", "Generados", "Esp.Max", "Tiempo(s)");
        imprimirFila("Anchura (BFS)", t1);
        imprimirFila("Profundidad (DFS)", t2);
        imprimirFila("Costo Uniforme (UCS)", t3);
        imprimirFila("Prof. Limitada (DLS)", t4);
        imprimirFila("Iterativa (IDS)", t5);

    }
    private static void imprimirFila(String nombre, SearchTree t) {
        String profundidad = t.ultimoEncontrado ? String.valueOf(t.ultimoProfundidad) : "No encontrado";
        System.out.printf("%-22s %-12s %-12d %-12d %-12d %-10.4f%n",nombre, profundidad, t.ultimoProcesados, t.ultimoGenerados,
        t.ultimoEspacioMax, t.ultimoTiempo);
    } 
}