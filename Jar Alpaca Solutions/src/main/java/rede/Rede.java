package rede;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.rede.RedeInterface;

import java.text.DecimalFormat;
import java.util.List;

public class Rede {

    private Double quantidade_bytes_recebidos;

    private Double quantidade_bytes_enviados;


    private Double quantidade_pacotes_enviados;

    private Double quantidade_pacotes_recebidos;

    public Rede(Double quantidade_bytes_recebidos, Double quantidade_bytes_enviados,
                Double quantidade_pacotes_enviados , Double quantidade_pacotes_recebidos) {
        this.quantidade_bytes_recebidos = quantidade_bytes_recebidos;
        this.quantidade_bytes_enviados = quantidade_bytes_enviados;
        this.quantidade_pacotes_enviados = quantidade_pacotes_enviados;
        this.quantidade_pacotes_recebidos = quantidade_pacotes_recebidos;
    }

    public Double getQuantidade_pacotes_enviados() {
        return quantidade_pacotes_enviados;
    }

    public void setQuantidade_pacotes_enviados(Double quantidade_pacotes_enviados) {
        this.quantidade_pacotes_enviados = quantidade_pacotes_enviados;
    }

    public Double getQuantidade_pacotes_recebidos() {
        return quantidade_pacotes_recebidos;
    }

    public void setQuantidade_pacotes_recebidos(Double quantidade_pacotes_recebidos) {
        this.quantidade_pacotes_recebidos = quantidade_pacotes_recebidos;
    }

    public Rede() {

    }

    public Double quantidade_bytes_recebidos_total(){
        Looca looca = new Looca();
        List<RedeInterface> dados_de_rede = looca.getRede().getGrupoDeInterfaces().getInterfaces();
        RedeInterface adaptador_com_dados = null;
        long maiorBytesrecebidos = 0;
        for (RedeInterface interfaceRede : dados_de_rede) {
            long bytes_recebidos = interfaceRede.getBytesRecebidos();
            if (bytes_recebidos > maiorBytesrecebidos) {
                adaptador_com_dados = interfaceRede;
                maiorBytesrecebidos = bytes_recebidos;
            }

        }

        long bytes_recebidos = adaptador_com_dados.getBytesRecebidos();

        Double bytes_em_mb = (double) bytes_recebidos / 1048576;
        DecimalFormat df = new DecimalFormat("#.##");
        String bytes_em_mb_formatado = df.format(bytes_em_mb);
        bytes_em_mb_formatado = bytes_em_mb_formatado.replace("," , ".");

        return Double.parseDouble(bytes_em_mb_formatado);
    }


    public Double quantidade_pacotes_recebidos_total(){
        Looca looca = new Looca();
        List<RedeInterface> dados_de_rede = looca.getRede().getGrupoDeInterfaces().getInterfaces();
        RedeInterface adaptador_com_dados = null;
        long maiorBytesrecebidos = 0;
        for (RedeInterface interfaceRede : dados_de_rede) {
            long bytes_recebidos = interfaceRede.getBytesRecebidos();
            if (bytes_recebidos > maiorBytesrecebidos) {
                adaptador_com_dados = interfaceRede;
                maiorBytesrecebidos = bytes_recebidos;
            }

        }

        long pacotes_recebidos = adaptador_com_dados.getPacotesRecebidos();

        Double pacotes_em_mb = (double) pacotes_recebidos / 1048576;
        DecimalFormat df = new DecimalFormat("#.##");
        String pacotes_em_mb_formatado = df.format(pacotes_em_mb);
        pacotes_em_mb_formatado = pacotes_em_mb_formatado.replace("," , ".");

        return Double.parseDouble(pacotes_em_mb_formatado);
    }

    public Double quantidade_pacotes_enviados_total(){
        Looca looca = new Looca();
        List<RedeInterface> dados_de_rede = looca.getRede().getGrupoDeInterfaces().getInterfaces();
        RedeInterface adaptador_com_dados = null;
        long maiorBytesrecebidos = 0;
        for (RedeInterface interfaceRede : dados_de_rede) {
            long bytes_recebidos = interfaceRede.getBytesRecebidos();
            if (bytes_recebidos > maiorBytesrecebidos) {
                adaptador_com_dados = interfaceRede;
                maiorBytesrecebidos = bytes_recebidos;
            }

        }

        long pacotes_recebidos = adaptador_com_dados.getPacotesEnviados();

        Double pacotes_em_mb = (double) pacotes_recebidos / 1048576;
        DecimalFormat df = new DecimalFormat("#.##");
        String pacotes_em_mb_formatado = df.format(pacotes_em_mb);
        pacotes_em_mb_formatado = pacotes_em_mb_formatado.replace("," , ".");

        return Double.parseDouble(pacotes_em_mb_formatado);
    }

    public Double quantidade_bytes_enviados_total(){
        Looca looca = new Looca();
        List<RedeInterface> dados_de_rede = looca.getRede().getGrupoDeInterfaces().getInterfaces();
        RedeInterface adaptador_com_dados = null;
        long maiorBytesrecebidos = 0;
        for (RedeInterface interfaceRede : dados_de_rede) {
            long bytes_enviados = interfaceRede.getBytesRecebidos();
            if (bytes_enviados > maiorBytesrecebidos) {
                adaptador_com_dados = interfaceRede;
                maiorBytesrecebidos = bytes_enviados;
            }
        }

        long bytes_enviados = adaptador_com_dados.getBytesEnviados();

        Double bytes_em_mb = (double) bytes_enviados / 1048576;
        DecimalFormat df = new DecimalFormat("#.##");
        String bytes_em_mb_formatado = df.format(bytes_em_mb);
        bytes_em_mb_formatado = bytes_em_mb_formatado.replace("," , ".");

        return Double.parseDouble(bytes_em_mb_formatado);
    }

    public String nome_da_rede(){
        Looca looca = new Looca();
        List<RedeInterface> dados_de_rede = looca.getRede().getGrupoDeInterfaces().getInterfaces();
        RedeInterface adaptador_com_dados = null;
        long maiorBytesrecebidos = 0;
        for (RedeInterface interfaceRede : dados_de_rede) {
            long bytes_enviados = interfaceRede.getBytesRecebidos();
            if (bytes_enviados > maiorBytesrecebidos) {
                adaptador_com_dados = interfaceRede;
                maiorBytesrecebidos = bytes_enviados;
            }
        }

        return adaptador_com_dados.getNome();
    }
    public Double getQuantidade_bytes_recebidos() {
        return quantidade_bytes_recebidos;
    }

    public void setQuantidade_bytes_recebidos(Double quantidade_bytes_recebidos) {
        this.quantidade_bytes_recebidos = quantidade_bytes_recebidos;
    }

    public Double getQuantidade_bytes_enviados() {
        return quantidade_bytes_enviados;
    }

    public void setQuantidade_bytes_enviados(Double quantidade_bytes_enviados) {
        this.quantidade_bytes_enviados = quantidade_bytes_enviados;
    }

    @Override
    public String toString() {
        return String.format("Dados de Rede\n" +
                        "Quantidade de bytes recebidos: %.2f Mb\n" +
                        "Quantidade de bytes enviados: %.2f Mb\n" +
                        "Quantidade de Pacotes enviados: %.2f Mb\n" +
                        "Quantidade de Pacotes recebidos: %.2f Mb\n",
                quantidade_bytes_recebidos, quantidade_bytes_enviados,
                quantidade_pacotes_enviados, quantidade_pacotes_recebidos);
    }

}
