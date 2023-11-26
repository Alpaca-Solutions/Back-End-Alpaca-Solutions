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

    public void Alerta(String mensagem) throws IOException, InterruptedException{
        JSONObject json = new JSONObject();


        json.put("text", mensagem + ":llama: :computer:");

        Slack.sendMessage(json);
    }

}
