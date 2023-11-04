package dados;

import java.io.IOException;
import java.util.List;

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
                json.put("text" , "Processador excedeu o limite :fire:");
            }
        }
        dados.Slack.sendMessage(json);
    }

}
