package memoria;

import java.text.DecimalFormat;

public class Memoria {

    private Double memoria_em_uso;

    private Double memoria_Disponivel;

    private Double Memoria_total;


    public Memoria(Double memoria_em_uso, Double memoria_Disponivel, Double memoria_total) {
        this.memoria_em_uso = memoria_em_uso;
        this.memoria_Disponivel = memoria_Disponivel;
        Memoria_total = memoria_total;
    }

    public Memoria(Long memoriaEmUso, Long memoriaDiponivel, Long memoriaTotal) {
    }


    public Double calcular_memoria_em_uso(Long memoria_uso) {
        Double memoria_uso_gigabytes = (double) memoria_uso / (1024 * 1024 * 1024);
        DecimalFormat df = new DecimalFormat("#.##");
        String memoria_formatada = df.format(memoria_uso_gigabytes);
        memoria_formatada = memoria_formatada.replace("," , ".");
        return Double.parseDouble(memoria_formatada);
    }

    public Double calcular_memoria_disponivel(Long memoria_diponivel){
        Double memoria_disponivel_gigabytes = (double) memoria_diponivel / (1024 * 1024 * 1024);
        DecimalFormat df = new DecimalFormat("#.##");
        String memoria_formatada = df.format(memoria_disponivel_gigabytes);
        memoria_formatada = memoria_formatada.replace(",", ".");

        return Double.parseDouble(memoria_formatada);
    }

    public Double calcular_memoria_total(Long memoria_total){
        Double memoria_total_gigabytes = (double) memoria_total / (1024 * 1024 * 1024);
        DecimalFormat df = new DecimalFormat("#.##");
        String memoria_formatada = df.format(memoria_total_gigabytes);
        memoria_formatada = memoria_formatada.replace("," , ".");

        return Double.parseDouble(memoria_formatada);
    }



    public Double getMemoria_em_uso() {
        return memoria_em_uso;
    }

    public void setMemoria_em_uso(Double memoria_em_uso) {
        this.memoria_em_uso = memoria_em_uso;
    }

    public Double getMemoria_Disponivel() {
        return memoria_Disponivel;
    }

    public void setMemoria_Disponivel(Double memoria_Disponivel) {
        this.memoria_Disponivel = memoria_Disponivel;
    }

    public Double getMemoria_total() {
        return Memoria_total;
    }

    public void setMemoria_total(Double memoria_total) {
        Memoria_total = memoria_total;
    }
    public String toString() {

        return String.format("Dados da Memoria\n" +
                        "Memoria em Uso: %.2f GB\n" +
                        "Memoria Disponivel: %.2f GB\n" +
                        "Total da Memória: %.2f GB\n",
                memoria_em_uso, memoria_Disponivel, Memoria_total);
    }



}
