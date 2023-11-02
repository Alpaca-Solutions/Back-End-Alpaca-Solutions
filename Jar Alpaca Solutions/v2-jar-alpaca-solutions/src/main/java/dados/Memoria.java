package dados;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;

public class Memoria {

    private long memoria_em_uso;

    private long memoria_Disponivel;

    private long Memoria_total;


    public Memoria(long memoria_em_uso, long memoria_Disponivel, long memoria_total) {
        this.memoria_em_uso = memoria_em_uso;
        this.memoria_Disponivel = memoria_Disponivel;
        Memoria_total = memoria_total;
    }

    public Double calcular_memoria_ram(Double memoria_total, Double memoria_em_uso) {


    Double memoria_ram_total = (memoria_total / (1024 * 1024));

    return memoria_ram_total;
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


    public long getMemoria_em_uso() {
        return memoria_em_uso;
    }

    public void setMemoria_em_uso(long memoria_em_uso) {
        this.memoria_em_uso = memoria_em_uso;
    }

    public long getMemoria_Disponivel() {
        return memoria_Disponivel;
    }

    public void setMemoria_Disponivel(long memoria_Disponivel) {
        this.memoria_Disponivel = memoria_Disponivel;
    }

    public long getMemoria_total() {
        return Memoria_total;
    }

    public void setMemoria_total(long memoria_total) {
        Memoria_total = memoria_total;
    }
//    public String toString() {
//        BigDecimal memoriaDisponivelGB_arrendodada = BigDecimal.valueOf(qtd_memoria_disponivel).divide(BigDecimal.valueOf(1e9), 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal tamanhoMemoriaGB_arrendodada = BigDecimal.valueOf(tamanho_memoria).divide(BigDecimal.valueOf(1e9), 2, BigDecimal.ROUND_HALF_UP);
//
//        return String.format("Dados da Memoria\n" +
//                        "Porcentagem de uso da memoria: %.2f%%\n" +
//                        "Quantidade de memoria Disponivel: %s GB\n" +
//                        "Tamanho da Memoria: %s GB\n" +
//                        "Quantidade de ram: %.2f GB\n",
//                porcentagem_uso_da_memoria_arrendodada, memoriaDisponivelGB_arrendodada, tamanhoMemoriaGB_arrendodada, quantidadeDeRAMGB_arrendodada);
//    }



}
