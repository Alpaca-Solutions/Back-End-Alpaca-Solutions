package dados;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Disco {
    private Double Porcentagem_de_Uso_do_Disco;
    private Double Tamanho_Total_do_Disco;

    private Double tamanho_disponivel_do_disco;

    public Disco() {
        Porcentagem_de_Uso_do_Disco = 0.0;
        Tamanho_Total_do_Disco = 0.0;
        this.tamanho_disponivel_do_disco = 0.0;
    }

    public Double total_tamanho_disco(){
        File disk = File.listRoots()[0];
        long total_disco = disk.getTotalSpace();
        Double total_disco_em_GB = total_disco / 1073741824.0;
        DecimalFormat df = new DecimalFormat("#.##");
        String total_tamanho_disco_formatado = df.format(total_disco_em_GB);
        total_tamanho_disco_formatado = total_tamanho_disco_formatado.replace("," , ".");

        this.tamanho_disponivel_do_disco = Double.parseDouble(total_tamanho_disco_formatado);
        return Double.parseDouble(total_tamanho_disco_formatado);
    }

    public Double percentual_uso_do_disco(){
        File disco = File.listRoots()[0];

        long total_disco = disco.getTotalSpace();
        long total_usado = disco.getUsableSpace();

        Double percentual_uso = (100.0 * (total_disco - total_usado)) / total_disco;

        DecimalFormat df = new DecimalFormat("#.##");
        String total_percentual_uso_disco_formatado = df.format(percentual_uso);
        total_percentual_uso_disco_formatado = total_percentual_uso_disco_formatado.replace("," , ".");

        this.Porcentagem_de_Uso_do_Disco = Double.parseDouble(total_percentual_uso_disco_formatado);
        return Double.parseDouble(total_percentual_uso_disco_formatado);

    }


    public Double tamanho_disponivel_do_disco(){
        File disco = File.listRoots()[0];
        long tamanho_disponivel_do_disco = disco.getFreeSpace();
        double tamanho_disponivel_do_disco_formatado_gb = tamanho_disponivel_do_disco / 1073741824.0;
        DecimalFormat df = new DecimalFormat("#.0");
        String tamanho_disponivel_do_disco_formatado = df.format(tamanho_disponivel_do_disco_formatado_gb);
        tamanho_disponivel_do_disco_formatado = tamanho_disponivel_do_disco_formatado.replace("," , ".");
        return Double.parseDouble(tamanho_disponivel_do_disco_formatado);
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
