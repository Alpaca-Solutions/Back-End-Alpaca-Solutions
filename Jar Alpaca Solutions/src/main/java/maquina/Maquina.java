package maquina;

import com.github.britooo.looca.api.core.Looca;

import java.io.IOException;
import java.net.*;

public class Maquina {

    private String nomeMaquina;

    private String ipMaquina;

    private String Sistema_Operacional;

    private Boolean statusMaquina;

    public Maquina(String nomeMaquina, String ipMaquina, String sistema_Operacional, Boolean statusMaquina) {
        this.nomeMaquina = nomeMaquina;
        this.ipMaquina = ipMaquina;
        Sistema_Operacional = sistema_Operacional;
        this.statusMaquina = statusMaquina;
    }

    public Maquina() {

    }

    public String obterIP() throws UnknownHostException {
        InetAddress dados = InetAddress.getLocalHost();

        return dados.getHostAddress();
    }

    public String  obterNomeMaquina() throws  UnknownHostException{
        InetAddress dados = InetAddress.getLocalHost();

        return dados.getHostName();
    }

    public Boolean statusMaquina() throws IOException {
        URL url = new URL("https://www.google.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();
        return (responseCode == HttpURLConnection.HTTP_OK);

    }

    public String getNomeMaquina() {
        return nomeMaquina;
    }

    public void setNomeMaquina(String nomeMaquina) {
        this.nomeMaquina = nomeMaquina;
    }

    public String getIpMaquina() {
        return ipMaquina;
    }

    public void setIpMaquina(String ipMaquina) {
        this.ipMaquina = ipMaquina;
    }

    public Boolean getStatusMaquina() {
        return statusMaquina;
    }

    public void setStatusMaquina(Boolean statusMaquina) {
        this.statusMaquina = statusMaquina;
    }

    public String getSistema_Operacional() {
        return Sistema_Operacional;
    }

    public void setSistema_Operacional(String sistema_Operacional) {
        Sistema_Operacional = sistema_Operacional;
    }

    @Override
    public String toString() {

        String statusMaquinaAtual;
        if(getStatusMaquina().equals(true)){
            statusMaquinaAtual = "Máquina com Rede";
        }
        else{
            statusMaquinaAtual = "Máquina não está conectada a internet";
        }
        return String.format("Status Maquina\n" +
                "IP: %s\n" +
                "HostName: %s\n" +
                "Sistema Operacional: %s\n" +
                "Status da Máquina: %s\n", ipMaquina, nomeMaquina, Sistema_Operacional, statusMaquinaAtual);
 }
}
