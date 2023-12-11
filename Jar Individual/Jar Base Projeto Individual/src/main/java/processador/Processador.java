package processador;

public class Processador {

    private Double percentual_uso_do_processador;


    public Processador(Double percentual_uso_do_processador) {
        this.percentual_uso_do_processador = percentual_uso_do_processador;
    }



    public Double getPercentual_uso_do_processador() {
        return percentual_uso_do_processador;
    }

    public void setPercentual_uso_do_processador(Double percentual_uso_do_processador) {
        this.percentual_uso_do_processador = percentual_uso_do_processador;
    }

    @Override
    public String toString() {
        return String.format("Dados do Processador\n" +
                "Percentual de uso do Processador:%.2f%%\n", percentual_uso_do_processador);
    }
}


