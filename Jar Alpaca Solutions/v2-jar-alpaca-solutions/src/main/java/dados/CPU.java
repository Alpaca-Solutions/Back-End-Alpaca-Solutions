package dados;

import java.math.BigDecimal;

public class CPU {

    private Double uso_da_cpu;

    private long qtd_nucleos;

    public CPU(Double uso_da_cpu, long qtd_nucleos) {
        this.uso_da_cpu = uso_da_cpu;
        this.qtd_nucleos = qtd_nucleos;
    }

    public Double getUso_da_cpu() {
        return uso_da_cpu;
    }

    public void setUso_da_cpu(Double uso_da_cpu) {
        this.uso_da_cpu = uso_da_cpu;
    }

    public long getQtd_nucleos() {
        return qtd_nucleos;
    }

    public void setQtd_nucleos(long qtd_nucleos) {
        this.qtd_nucleos = qtd_nucleos;
    }

    @Override
    public String toString() {
        return String.format("Dados da CPU\n" +
                "Uso da CPU:%.2f%%\n" +
                "Quantidade de Núcleos=%d", uso_da_cpu, qtd_nucleos);
    }
}


