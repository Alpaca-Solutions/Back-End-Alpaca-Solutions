package dados;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.group.temperatura.Temperatura;
import org.springframework.jdbc.core.JdbcTemplate;
import oshi.software.os.OSFileStore;

import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
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



//        System.out.println("Olá Bem Vindo ao Sistema de Monitoramento Alpaca Solutions");
//
//        System.out.println("Digite seu Email");
//        String email = leitor.nextLine();
//
//        System.out.println("Agora digite sua senha:");
//        String senha = leitor.nextLine();
//
//        Cliente cliente = new Cliente(email , senha);
//
//
//        con.update("  select 'cliente' as tipo_cliente ,  cliente.idcliente as id,cliente.nome  as nome,  cliente.email as email, cliente.senha as senha,endereco.idendereco from cliente \n" +
//                "  join endereco on idendereco = fkendereco_cliente\n" +
//                "  where cliente.email = ? and cliente.senha = ?\n" +
//                "  union\n" +
//                "  select 'usuario' as tipo_cliente , usuario.idusuario as id ,usuario.nome as nome , usuario.email as email, usuario.senha as senha, endereco.idendereco  from usuario \n" +
//                "  join cliente  on idcliente = fkusuario_cliente\n" +
//                "  join endereco on endereco.idendereco = cliente.fkendereco_cliente \n" +
//                "  where usuario.email = ? and usuario.senha = ?;", cliente.getEmail() , cliente.getSenha(), cliente.getEmail(), cliente.getSenha());
//

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Looca looca = new Looca();


                // Funcão para capturar dados do processador e mandar para as classes
                Memoria memoria = new Memoria();
                Long memoria_em_uso = memoria.getEmUso();
                Long memoria_diponivel = memoria.getDisponivel();
                Long memoria_total = memoria.getTotal();
                dados.Memoria  dados_memoria = new dados.Memoria(memoria_em_uso , memoria_diponivel, memoria_total);

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

                dados.Processador processador = new dados.Processador(processador01.getUso());

                List<dados.Processador> lista_processador = new ArrayList<>();

                lista_processador.add(processador);

                try {
                    alerta.alerta_prcessador(lista_processador);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                System.out.println(processador);

//                Double quantidade_de_ram = (double) memoria.getDisponivel() / (1024 * 1024 * 1024);

//                // Insere os dados no banco de dados
//                con.update("insert into servidor (porcentagem_uso_disco, porcentagem_uso_memoria, quantidade_de_ram, memoria_disponivel, tamanho_total_disco, porcentagem_uso_cpu, tamanho_disponivel_do_disco, memoria_total," +
//                                "quantidade_de_bytes_recebidos, quantidade_de_bytes_enviados) values (?, ?, ?, ?, ?, ?, ?, ? , ? , ?)",
//                        disco01.getPorcentagem_de_Uso_do_Disco(), memoria01.getPorcentagem_uso_memoria(), memoria01.getQuantidade_de_ram(),
//                        memoria01.getQtd_memoria_disponivel(), disco01.getTamanho_Total_do_Disco(),
//                        cpu01.getUso_da_cpu(), disco01.getTamanho_Total_do_Disco(), memoria01.getTamanho_memoria(),
//                        rede01.getQuantidade_bytes_recebidos(), rede01.getQuantidade_bytes_enviados());
            }
        };

        long delay_de_tempo = 0;
        long tempo_carregar = 3000;

        tempo.scheduleAtFixedRate(task , delay_de_tempo , tempo_carregar);




    }
}
