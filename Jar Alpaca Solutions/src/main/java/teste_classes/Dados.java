package teste_classes;

import cliente.Cliente;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.sistema.Sistema;
import conexao_banco_dados.Conexao;
import conexao_banco_dados.QueryBanco;
import disco.Disco;
import maquina.Maquina;
import mensageria.Alertas;
import org.springframework.jdbc.core.JdbcTemplate;
import rede.Rede;

import java.io.IOException;
import java.util.*;

public class Dados {
    public static void main(String[] args) {
        // Inicializa a conexão com o banco de dados
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        Scanner leitor = new Scanner(System.in);
        String continuar;
        Timer tempo = new Timer();
        Alertas alerta = new Alertas();



        System.out.println("Olá Bem Vindo ao Sistema de Monitoramento Alpaca Solutions");

        System.out.println("Digite seu Email");
        String email = leitor.nextLine();

        System.out.println("Agora digite sua senha:");
        String senha = leitor.nextLine();

        Cliente cliente = new Cliente(email , senha);


        QueryBanco busca_cliente = new QueryBanco();

        Integer resultado_select_cliente = busca_cliente.Busca_Cliente(cliente);

        if(resultado_select_cliente == 200) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Looca looca = new Looca();


                    // Funcão para capturar dados do processador e mandar para as classes
                    Memoria memoria = new Memoria();
                    Long memoria_em_uso = memoria.getEmUso();
                    Long memoria_diponivel = memoria.getDisponivel();
                    Long memoria_total = memoria.getTotal();
                    memoria.Memoria dados_memoria = new memoria.Memoria(memoria_em_uso, memoria_diponivel, memoria_total);

                    dados_memoria.setMemoria_em_uso(dados_memoria.calcular_memoria_em_uso(memoria_em_uso));
                    dados_memoria.setMemoria_Disponivel(dados_memoria.calcular_memoria_disponivel(memoria_diponivel));
                    dados_memoria.setMemoria_total(dados_memoria.calcular_memoria_em_uso(memoria_total));

                    System.out.println(dados_memoria);
                    // Ram não é preciso pq a api ja junta todos os dados alocados de memoria do sistema e mostra junto;

                    // Função Disco

                    Disco disco01 = new Disco();

                    disco01.setTamanho_Total_do_Disco(disco01.total_tamanho_disco());
                    disco01.setPorcentagem_de_Uso_do_Disco(disco01.percentual_uso_do_disco());
                    disco01.setTamanho_disponivel_do_disco(disco01.tamanho_disponivel_do_disco());

                    System.out.println(disco01);

                    // Disco Completo:)

                    // Função Rede


                    Rede rede = new Rede();


                    rede.setQuantidade_bytes_enviados(rede.quantidade_bytes_enviados_total());
                    rede.setQuantidade_bytes_recebidos(rede.quantidade_bytes_recebidos_total());

                    System.out.println(rede);

                    // função rede completa :)


                    // Dados Maquina

                    Sistema sisitema01 = new Sistema();
                    Maquina maquina01 = new Maquina(sisitema01.getSistemaOperacional());

                    System.out.println(maquina01);

                    // Cpu

                    Processador processador01 = new Processador();

                    processador.Processador processador = new processador.Processador(processador01.getUso());

                    List<processador.Processador> lista_processador = new ArrayList<>();

                    lista_processador.add(processador);

                    try {
                        alerta.alerta_prcessador(lista_processador);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(processador);


                    // aqui ele vai fazer a inserção nas 3 tabelas , espero que de certo

                    QueryBanco inserts_dados_maquina = new QueryBanco();


                    inserts_dados_maquina.inserir_Componentes();

                }
            };

            long delay_de_tempo = 0;
            long tempo_carregar = 3000;

            tempo.scheduleAtFixedRate(task, delay_de_tempo, tempo_carregar);

        }
        else{
            System.out.println("Você ainda não fez cadastro no nosso sistema :(");
        }


    }
}
