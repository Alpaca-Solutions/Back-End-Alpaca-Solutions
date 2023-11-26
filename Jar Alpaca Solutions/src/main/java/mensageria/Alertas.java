package mensageria;

import java.io.IOException;
import java.util.List;

import disco.Disco;
import memoria.Memoria;
import processador.Processador;
import rede.Rede;
import org.json.JSONObject;

public class Alertas {

    List<Processador> processador;
    List<Rede> rede;

    List<Disco> disco;

    List<Memoria> memoria;
    public void alerta_prcessador(List<Processador> processadores) throws IOException, InterruptedException {
        JSONObject json = new JSONObject();

        for(Processador processador01 : processadores){
            if(processador01.getPercentual_uso_do_processador() > 25){
                json.put("text" , "Processador excedeu o limite :llama: :fire:");
            }
        }
        Slack.sendMessage(json);
    }

    public void AlertaMemoria(String mensagem) throws IOException, InterruptedException{
        JSONObject json = new JSONObject();


        json.put("text", mensagem + ":llama: :computer:");

        Slack.sendMessage(json);
    }

}
