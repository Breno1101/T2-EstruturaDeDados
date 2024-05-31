import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

abstract class CapacidadeEstacionamento {
    protected final int capacidade;
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public CapacidadeEstacionamento(int capacidade) {
        this.capacidade = capacidade;
    }

    public abstract void entrarEstacionamento(String placa);
    public abstract void sairEstacionamento(String placa);
    public abstract void consultarCarro(String placa);
    public abstract void statusEstacionamento();
}

class PilhaEstacionamento extends CapacidadeEstacionamento {
    private final Stack<Carro> pilhaEstacionamento;

    public PilhaEstacionamento(int capacidade) {
        super(capacidade);
        this.pilhaEstacionamento = new Stack<>();
    }

    @Override
    public void entrarEstacionamento(String placa) {
        if (pilhaEstacionamento.size() >= capacidade) {
            System.out.println("Estacionamento cheio!");
            return;
        }
        Carro carro = new Carro(placa, LocalDateTime.now());
        pilhaEstacionamento.push(carro);
        System.out.println("Carro " + placa + " entrou no estacionamento às " + carro.tempoDeEntrada().format(formatter));
    }

    @Override
    public void sairEstacionamento(String placa) {
        Stack<Carro> tempPilha = new Stack<>();
        boolean encontrado = false;
        int manobras = 0;

        while (!pilhaEstacionamento.isEmpty()) {
            Carro carro = pilhaEstacionamento.pop();
            if (carro.placa().equals(placa)) {
                encontrado = true;
                LocalDateTime tempoDeSaida = LocalDateTime.now();
                Duration duracao = Duration.between(carro.tempoDeEntrada(), tempoDeSaida);
                System.out.println("Carro " + placa + " saiu do estacionamento às " + tempoDeSaida.format(formatter));
                System.out.println("Tempo total de permanência: " + duracao.toMinutes() + " minutos");
                System.out.println("Número de manobras: " + manobras);
                break;
            } else {
                tempPilha.push(carro);
                manobras++;
            }
        }

        while (!tempPilha.isEmpty()) {
            pilhaEstacionamento.push(tempPilha.pop());
        }

        if (!encontrado) {
            System.out.println("Carro com placa " + placa + " não encontrado no estacionamento.");
        }
    }

    @Override
    public void consultarCarro(String placa) {
        int posicao = pilhaEstacionamento.size();
        for (Carro carro : pilhaEstacionamento) {
            if (carro.placa().equals(placa)) {
                System.out.println("Carro " + placa + " está na posição " + posicao + " da pilha, entrou às " + carro.tempoDeEntrada().format(formatter));
                return;
            }
            posicao--;
        }
        System.out.println("Carro com placa " + placa + " não está no estacionamento.");
    }

    @Override
    public void statusEstacionamento() {
        if (pilhaEstacionamento.isEmpty()) {
            System.out.println("O estacionamento está vazio.");
        } else {
            System.out.println("Carros atualmente no estacionamento:");
            for (Carro carro : pilhaEstacionamento) {
                System.out.println(carro);
            }
        }
    }
}

class FilaEstacionamento extends CapacidadeEstacionamento {
    private final Queue<Carro> filaEstacionamento;

    public FilaEstacionamento(int capacidade) {
        super(capacidade);
        this.filaEstacionamento = new LinkedList<>();
    }

    @Override
    public void entrarEstacionamento(String placa) {
        if (filaEstacionamento.size() >= capacidade) {
            System.out.println("Estacionamento cheio!");
            return;
        }
        Carro carro = new Carro(placa, LocalDateTime.now());
        filaEstacionamento.add(carro);
        System.out.println("Carro " + placa + " entrou no estacionamento às " + carro.tempoDeEntrada().format(formatter));
    }

    @Override
    public void sairEstacionamento(String placa) {
        boolean encontrado = false;
        int manobras = 0;

        for (Iterator<Carro> iterator = filaEstacionamento.iterator(); iterator.hasNext(); ) {
            Carro carro = iterator.next();
            if (carro.placa().equals(placa)) {
                encontrado = true;
                iterator.remove();
                LocalDateTime tempoDeSaida = LocalDateTime.now();
                Duration duracao = Duration.between(carro.tempoDeEntrada(), tempoDeSaida);
                System.out.println("Carro " + carro.placa() + " saiu do estacionamento às " + tempoDeSaida.format(formatter));
                System.out.println("Tempo total de permanência: " + duracao.toMinutes() + " minutos");
                System.out.println("Número de manobras realizadas: " + manobras);
                break;
            } else {
                manobras++;
            }
        }

        if (!encontrado) {
            System.out.println("Carro com placa " + placa + " não encontrado no estacionamento.");
        }
    }

    @Override
    public void consultarCarro(String placa) {
        int posicao = 1;
        for (Carro carro : filaEstacionamento) {
            if (carro.placa().equals(placa)) {
                System.out.println("Carro " + placa + " está na posição " + posicao + " da fila, entrou  às " + carro.tempoDeEntrada().format(formatter));
                return;
            }
            posicao++;
        }
        System.out.println("Carro com placa " + placa + " não está no estacionamento.");
    }

    @Override
    public void statusEstacionamento() {
        if (filaEstacionamento.isEmpty()) {
            System.out.println("O estacionamento está vazio.");
        } else {
            System.out.println("Carros atualmente no estacionamento:");
            for (Carro carro : filaEstacionamento) {
                System.out.println(carro);
            }
        }
    }
}

record Carro(String placa, LocalDateTime tempoDeEntrada) {
    @Override
    public String toString() {
        return "Carro: placa='" + placa + "', tempo de entrada=" + tempoDeEntrada.format(CapacidadeEstacionamento.formatter);
    }
}

public class Estacionamento {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nTrabalho T2 de Estrutura de Dados");
        System.out.println(" ");
        System.out.println("Integrantes da tarefa: ");
        System.out.println(" ");
        System.out.println("Breno Coutinho 23.01297-8");
        System.out.println("Yuri Drapack 23.00243-3");
        System.out.println("João Pedro Galhardo 23.01378-8");
        System.out.println("Lucca de Souza 23.01053-3");
        System.out.println(" ");
        
        System.out.println("Selecione o tipo de estacionamento:");
        System.out.println("1. Estacionamento com Entrada Única (Pilha)");
        System.out.println("2. Estacionamento com Entrada e Saída Separadas (Fila)");
        int escolha = scanner.nextInt();

        CapacidadeEstacionamento capacidadeEstacionamento;

        if (escolha == 1) {
            capacidadeEstacionamento = new PilhaEstacionamento(5);
        } else if (escolha == 2) {
            capacidadeEstacionamento = new FilaEstacionamento(5);
        } else {
            System.out.println("Opção inválida.");
            return;
        }

        while (true) {
            System.out.println("\nEscolha uma ação:");
            System.out.println("1. Entrada de Carro");
            System.out.println("2. Saída de Carro");
            System.out.println("3. Consulta de Carro");
            System.out.println("4. Status do Estacionamento");
            System.out.println("5. Sair");
            int leitor = scanner.nextInt();
            scanner.nextLine();

            switch (leitor) {
                case 1:
                    System.out.println("Digite a placa do carro:");
                    String placaDaEntrada = scanner.nextLine();
                    capacidadeEstacionamento.entrarEstacionamento(placaDaEntrada);
                    break;
                case 2:
                    System.out.println("Digite a placa do carro para saída:");
                    String placaDaSaida = scanner.nextLine();
                    capacidadeEstacionamento.sairEstacionamento(placaDaSaida);
                    break;
                case 3:
                    System.out.println("Digite a placa do carro para consulta:");
                    String placaDaConsulta = scanner.nextLine();
                    capacidadeEstacionamento.consultarCarro(placaDaConsulta);
                    break;
                case 4:
                    capacidadeEstacionamento.statusEstacionamento();
                    break;
                case 5:
                    System.out.println("Saindo");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }
}
