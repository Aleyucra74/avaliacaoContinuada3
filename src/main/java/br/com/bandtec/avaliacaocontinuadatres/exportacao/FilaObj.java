package br.com.bandtec.avaliacaocontinuadatres.exportacao;

public class FilaObj<T> {

    int tamanho;
    T[] fila;

    public FilaObj(int tamanho) {
        this.tamanho = 0;
        this.fila = (T[]) new Object[tamanho];
    }

    public boolean isEmpty(){ return tamanho == 0; }

    public boolean isFull(){ return fila.length == tamanho; }

    public void insert(T info ){
        if (!isFull())
            this.fila[this.tamanho++] = info;
    }

    public T peek(){ return fila[0]; }

    public T poll(){
        T primeiro = peek();

        if (!isEmpty()){
            for (int i = 0; i < tamanho-0; i++) {
                fila[i] = fila[i+1];
            }
            fila[tamanho-1]=null;
            tamanho--;
        }
        return primeiro;
    }

    public void exibe() {
        for (int i = 0; i < tamanho; i++) {
            System.out.println(fila[i]);
        }
    }

}

