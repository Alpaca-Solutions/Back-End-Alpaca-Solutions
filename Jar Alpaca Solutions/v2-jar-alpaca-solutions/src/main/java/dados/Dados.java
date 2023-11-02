package dados;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.group.temperatura.Temperatura;
import org.springframework.jdbc.core.JdbcTemplate;
import oshi.software.os.OSProcess;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Dados {
    public static void main(String[] args) {
        // Inicializa a conexão com o banco de dados
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        Scanner leitor = new Scanner(System.in);
        String continuar;
        Timer tempo = new Timer();



        System.out.println("Olá Bem Vindo ao Sistema de Monitoramento Alpaca Solutions");

        System.out.println("Digite seu Email");
        String email = leitor.nextLine();

        System.out.println("Agora digite sua senha:");
        String senha = leitor.nextLine();

        Cliente cliente = new Cliente(email , senha);


        con.update("  select 'cliente' as tipo_cliente ,  cliente.idcliente as id,cliente.nome  as nome,  cliente.email as email, cliente.senha as senha,endereco.idendereco from cliente \n" +
                "  join endereco on idendereco = fkendereco_cliente\n" +
                "  where cliente.email = ? and cliente.senha = ?\n" +
                "  union\n" +
                "  select 'usuario' as tipo_cliente , usuario.idusuario as id ,usuario.nome as nome , usuario.email as email, usuario.senha as senha, endereco.idendereco  from usuario \n" +
                "  join cliente  on idcliente = fkusuario_cliente\n" +
                "  join endereco on endereco.idendereco = cliente.fkendereco_cliente \n" +
                "  where usuario.email = ? and usuario.senha = ?;", cliente.getEmail() , cliente.getSenha(), cliente.getEmail(), cliente.getSenha());

        // Cria a tabela 'servidor' se ela não existir
        con.update("create table IF NOT EXISTS servidor (\n" +
                "idservidor int primary key auto_increment,\n" +
                "porcentagem_uso_disco decimal,\n" +
                "porcentagem_uso_memoria decimal,\n" +
                "quantidade_de_ram decimal,\n" +
                "memoria_disponivel decimal,\n" +
                "tamanho_total_disco decimal,\n" +
                "porcentagem_uso_cpu decimal,\n" +
                "tamanho_disponivel_do_disco decimal,\n" +
                "memoria_total decimal,\n" +
                "quantidade_de_bytes_recebidos decimal,\n" +
                "quantidade_de_bytes_enviados decimal,\n" +
                "dthora datetime default current_timestamp\n" +
                ");");
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


                // so vai faltar memoria ram;


                // Função Disco



                // Função Rede

                // Funcão Memória
//                Sistema sistema = new Sistema();
//                Processador processador = new Processador();
//                Memoria memoria = new Memoria();
//                double tamanhoTotalGiB = 0;
//                double tot_disco = 0.0;
//                int total_disco = 0;
//                double tamanho_disco = 0.0;
//
//
//
//                File dados_disco =   new File("C:\\");
//                long total_espaco_livre_disco_giga_bites = 0;
//                Double percentual_de_espaco_livre_disco = 0.0;
//                long espaco_livre_do_disco = 0;
//                long Uso = 0;
//
//                if(dados_disco.exists()){
//
//                    // conta para porcentagem de uso do disco = uso / total do disco
//                    // pegando o tamanho total do disco
//
//
//                    long total_espaco = dados_disco.getTotalSpace();
//                    long livre = dados_disco.getUsableSpace();
//
//                    Uso = total_espaco - livre;
//
//
//                }
//                // Coleta informações sobre os discos
//                for (Disco disco : looca.getGrupoDeDiscos().getDiscos()) {
//                    // tranformando em gigabytes
//
//                    // espaco livre do disco =  uso - total do disco
//
//                    tamanho_disco = disco.getTamanho();
//                    total_disco = (int) Math.round(tamanhoTotalGiB * 100);
//
//                    percentual_de_espaco_livre_disco = (double) (disco.getTamanho() / Uso);
//                    espaco_livre_do_disco =  (total_disco - total_espaco_livre_disco_giga_bites);
//                }
//
//                System.out.println(percentual_de_espaco_livre_disco);
//
//                System.out.println(Uso);
//
//                Double total_pro = processador.getUso();
//                Double porcentagem_uso_memoria = (double) memoria.getEmUso() / memoria.getTotal() * 100;
//                Double quantidade_de_ram = (double) memoria.getDisponivel() / (1024 * 1024 * 1024);
//                Double porcentagem_de_uso_da_cpu = percentual_de_espaco_livre_disco;
//
//                Double tamanho_disponivel_do_disco = tamanho_disco / (1024 * 1024 * 1024);
//                Double memoria_total = (double) memoria.getTotal() / (1024 * 1024 * 1024.0);
//
//                List<RedeInterface> dados_de_rede = looca.getRede().getGrupoDeInterfaces().getInterfaces();
//                RedeInterface adaptador_com_dados = null;
//                long maiorBytesrecebidos = 0;
//                long total_bytes_recebidos = 0;
//                long total_bytes_enviados = 0;
//
//                // Coleta informações de rede
//                for (RedeInterface interfaceRede : dados_de_rede) {
//                    long bytes_recebidos = interfaceRede.getBytesRecebidos();
//                    if (bytes_recebidos > maiorBytesrecebidos) {
//                        adaptador_com_dados = interfaceRede;
//                        maiorBytesrecebidos = bytes_recebidos;
//                    }
//                }
//
//
//
//
//                long memoria_disponivel = memoria.getDisponivel();
//                long total_memoria = memoria.getTotal();
//                total_bytes_enviados = adaptador_com_dados.getBytesEnviados();
//                total_bytes_recebidos = adaptador_com_dados.getBytesRecebidos();
//                long qtd_nucleos_cpu = processador.getNumeroCpusFisicas() + processador.getNumeroCpusLogicas();
//
//                // Cria objetos com os dados coletados
//                Rede rede01 = new Rede(total_bytes_recebidos, total_bytes_enviados);
//                dados.Memoria memoria01 = new dados.Memoria(porcentagem_uso_memoria, memoria_disponivel, total_memoria, quantidade_de_ram);
//                dados.CPU cpu01 = new dados.CPU(porcentagem_de_uso_da_cpu, qtd_nucleos_cpu);
//                dados.Disco disco01 = new dados.Disco(total_pro, tot_disco, tamanho_disponivel_do_disco);
//
//                // Exibe os dados coletados
//                System.out.println(memoria01);
//                System.out.println(rede01);
//                System.out.println(disco01);
//                System.out.println(cpu01);
//                System.out.println();
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
