package dados;

import java.math.BigDecimal;

public class Disco {
    private Double Porcentagem_de_Uso_do_Disco;
    private Double Tamanho_Total_do_Disco;

    private Double tamanho_disponivel_do_disco;

    public Disco(Double porcentagem_de_Uso_do_Disco, Double tamanho_Total_do_Disco, Double tamanho_disponivel_do_disco) {
        Porcentagem_de_Uso_do_Disco = porcentagem_de_Uso_do_Disco;
        Tamanho_Total_do_Disco = tamanho_Total_do_Disco;
        this.tamanho_disponivel_do_disco = tamanho_disponivel_do_disco;
    }

    public Double getPorcentagem_de_Uso_do_Disco() {
        return Porcentagem_de_Uso_do_Disco;
    }

    public void setPorcentagem_de_Uso_do_Disco(Double porcentagem_de_Uso_do_Disco) {
        Porcentagem_de_Uso_do_Disco = porcentagem_de_Uso_do_Disco;
    }

    public Double getTamanho_Total_do_Disco() {
        return Tamanho_Total_do_Disco;
    }

    public void setTamanho_Total_do_Disco(Double tamanho_Total_do_Disco) {
        Tamanho_Total_do_Disco = tamanho_Total_do_Disco;
    }

    public Double getTamanho_disponivel_do_disco() {
        return tamanho_disponivel_do_disco;
    }

    public void setTamanho_disponivel_do_disco(Double tamanho_disponivel_do_disco) {
        this.tamanho_disponivel_do_disco = tamanho_disponivel_do_disco;
    }


    @Override
    public String toString() {
        return String.format("Dados do Disco\n" +
                "Porcentagem de Uso do Disco:%.2f%%\n" +
                "Tamanho Total do Disco:%.2f GB\n" +
                "Tamanho disponivel do disco:%.2f GB\n", Porcentagem_de_Uso_do_Disco, Tamanho_Total_do_Disco, tamanho_disponivel_do_disco);
    }
}
