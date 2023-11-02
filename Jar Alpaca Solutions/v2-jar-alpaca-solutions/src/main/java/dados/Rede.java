package dados;

public class Rede {

    private long quantidade_bytes_recebidos;

    private long quantidade_bytes_enviados;

    public Rede(long quantidade_bytes_recebidos, long quantidade_bytes_enviados) {
        this.quantidade_bytes_recebidos = quantidade_bytes_recebidos;
        this.quantidade_bytes_enviados = quantidade_bytes_enviados;
    }

    public Rede() {

    }

    public long getQuantidade_bytes_recebidos() {
        return quantidade_bytes_recebidos;
    }

    public void setQuantidade_bytes_recebidos(long quantidade_bytes_recebidos) {
        this.quantidade_bytes_recebidos = quantidade_bytes_recebidos;
    }

    public long getQuantidade_bytes_enviados() {
        return quantidade_bytes_enviados;
    }

    public void setQuantidade_bytes_enviados(long quantidade_bytes_enviados) {
        this.quantidade_bytes_enviados = quantidade_bytes_enviados;
    }

    @Override
    public String toString() {
        return String.format("Dados de Rede\n" +
                "Quantidade de bytes recebidos:%d bytes\n" +
                "Quantidade de bytes enviados:%d bytes\n", quantidade_bytes_recebidos, quantidade_bytes_enviados);
    }
}
