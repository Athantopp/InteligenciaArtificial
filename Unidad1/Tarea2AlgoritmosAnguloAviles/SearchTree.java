import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


public class SearchTree {
    Node root;
    String initialState;
    String goalState;

    int ultimoProfundidad;
    int ultimoProcesados;
    int ultimoGenerados;
    int ultimoEspacioMax;
    double ultimoTiempo;
    int ultimoCosto = -1;
    boolean ultimoEncontrado;

    

    public SearchTree(String initialState,   String goalState){
        this.initialState =initialState;
        this.goalState = goalState;
        this.root = new Node(initialState, null);
    }

    public void breadthFirstSearch() {
        long inicio = System.nanoTime();
        int time = 0;
        int nodosGenerados = 1;
        int espacioMaximo = 1;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = queue.poll();
            visited.add(currentNode.getState());
            // System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                long fin = System.nanoTime();
                System.out.println("Goal state found: " + currentNode.getState());
                System.out.println("BreadthFisrtSearch");
                System.out.println("Profundidad: " + currentNode.getDepth());
                System.out.println("time: " + time);
                System.out.println("Estados visitados: " + visited.size());
                System.out.println("Stack:" + queue.size());
 
                ultimoEncontrado = true;
                ultimoProfundidad = currentNode.getDepth();
                ultimoProcesados = visited.size();
                ultimoGenerados = nodosGenerados;
                ultimoEspacioMax = espacioMaximo;
                ultimoTiempo = (fin - inicio) / 1_000_000_000.0;
                return;
                //imprimir el timepo y la complejidad 
                //imprimir desde el estado inicial hasta el estado meta, crear un metodo
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState()))
                    queue.add(child);
                    nodosGenerados++;
            }
            espacioMaximo = Math.max(espacioMaximo, queue.size());
        }
        ultimoEncontrado =false;
    }
   
    public void depthFirstSearch(){
        long inicio = System.nanoTime();
        int time = 0;
        int nodosGenerados = 1;
        int espacioMaximo = 1;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        Deque<Node> stack = new LinkedList<>();
        stack.push(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!stack.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = stack.pop();
            visited.add(currentNode.getState());
            // System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                long fin = System.nanoTime();
                System.out.println("Goal state found: " + currentNode.getState());
                System.out.println("DepthFirstSearch");
                System.out.println("Profundidad: " + currentNode.getDepth());
                System.out.println("time: " + time);
                System.out.println("Estados visitados: " + visited.size());
                System.out.println("Stack:" + stack.size());
 
                ultimoEncontrado = true;
                ultimoProfundidad = currentNode.getDepth();
                ultimoProcesados = visited.size();
                ultimoGenerados = nodosGenerados;
                ultimoEspacioMax = espacioMaximo;
                ultimoTiempo = (fin - inicio) / 1_000_000_000.0;
                return;
                //imprimir el timepo y la complejidad 
                //imprimir desde el estado inicial hasta el estado meta, crear un metodo
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState()))
                    stack.push(child);
                    nodosGenerados++;
            }
            espacioMaximo = Math.max(espacioMaximo, stack.size());
        }  
        ultimoEncontrado =false; 
    }

    public void uniformCostSearch() {
        long inicio = System.nanoTime();
        int time = 0;
        int nodosGenerados = 1;
        int espacioMaximo = 1;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        // PriorityQueue<Node> queue = new PriorityQueue<>((n1,n2)) ->Integer.compare(n1.getCost(), n2.getCost());
        PriorityQueue<Node> queue = new PriorityQueue<>(new NodePriorityComparator()); 
        queue.add(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = queue.poll();
            visited.add(currentNode.getState());
            // System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                long fin = System.nanoTime();
                System.out.println("Goal state found: " + currentNode.getState());
                System.out.println("UniformCost");
                System.out.println("Profundidad: " + currentNode.getDepth());
                System.out.println("Costo final: " + currentNode.getCost());
                System.out.println("time: " + time);
                System.out.println("Estados visitados: " + visited.size());
                System.out.println("Stack:" + queue.size());
 
                ultimoEncontrado = true;
                ultimoProfundidad = currentNode.getDepth();
                ultimoCosto = currentNode.getCost();
                ultimoProcesados = visited.size();
                ultimoGenerados = nodosGenerados;
                ultimoEspacioMax = espacioMaximo;
                ultimoTiempo = (fin - inicio) / 1_000_000_000.0;
                    return;
                //imprimir el timepo y la complejidad 
                //imprimir desde el estado inicial hasta el estado meta, crear un metodo
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState())){
                    child.setCost(child.getParent().getCost() +1 );
                    queue.add(child);
                    nodosGenerados++;
                }
            }
            espacioMaximo = Math.max(espacioMaximo, queue.size());
        }
        ultimoEncontrado = false;
    }    
    
    public boolean depthLimitedSearch(int limite){
        long inicio = System.nanoTime();
        int time = 0;
        int nodosGenerados = 1;
        int espacioMaximo = 1;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        Deque<Node> stack = new LinkedList<>();
        stack.push(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!stack.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la pila y verificar si es el nodo objetivo
            currentNode = stack.pop();
            visited.add(currentNode.getState());
            // System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                //printPath(currentNode);
                // print the path from root to goal
                long fin = System.nanoTime();
                System.out.println("Goal state found: " + currentNode.getState());
                System.out.println("DepthLimitedSearch");
                System.out.println("Profundidad: " + currentNode.getDepth());
                System.out.println("time: " + time);
                System.out.println("Estados visitados: " + visited.size());
                System.out.println("Stack:" + stack.size());
 
                ultimoEncontrado = true;
                ultimoProfundidad = currentNode.getDepth();
                ultimoProcesados = visited.size();
                ultimoGenerados = nodosGenerados;
                ultimoEspacioMax = espacioMaximo;
                ultimoTiempo = (fin - inicio) / 1_000_000_000.0;
                return true;
                //imprimir el timepo y la complejidad 
                //imprimir desde el estado inicial hasta el estado meta, crear un metodo
            }
            if(currentNode.getDepth()<limite){
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState()))
                    stack.push(child);
                    nodosGenerados++;
            }
            }
            espacioMaximo = Math.max(espacioMaximo, stack.size());
        }
        //System.out.println("No se encontro solucion con límite " + limite);
        
        ultimoEncontrado = false;
        ultimoProcesados = visited.size();
        ultimoGenerados = nodosGenerados;
        ultimoEspacioMax = espacioMaximo;
        ultimoTiempo = (System.nanoTime() - inicio) / 1_000_000_000.0;
        return false;
    }

    public boolean depthIterativeSearch(int limite){
        boolean contador;
        for(int i =0; i<=limite; i++){
            contador = depthLimitedSearch(i);
            if(contador){
                return contador;
            }
        }
        return false;
    }

    private void printPath(Node node){
        if(node == null){
            return;
        }
        printPath(node.getParent());
        System.out.println(NodeUtils.formatState(node.getState()));
    }
}