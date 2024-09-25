import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientApp {

    public static void main(String[] args) {

        System.out.println("Coloque o seu nome: ");
        Scanner scanner = new Scanner(System.in);
        String usuario = scanner.nextLine();

        
        try (Socket socket = new Socket("localhost", 1234);
                BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true)) {

            // Enviar o nome do usuário
            escritor.println(usuario);
            escritor.flush();

            
            Thread receptor = new Thread(() -> {
                try {
                    String mensagemRecebida;
                    while ((mensagemRecebida = leitor.readLine()) != null) {
                        System.out.println(mensagemRecebida);
                    }
                } catch (IOException ioException) {
                    System.out.println("Socket já finalizou!");
                }
            });
            receptor.start();

            
            while (true) {
                Thread.sleep(300);
                System.out.println("Escreva uma mensagem: ");
                String mensagem = scanner.nextLine();

                if (mensagem.equalsIgnoreCase("sair")) {
                    socket.close(); 
                    break;
                }

                escritor.println(mensagem);
                escritor.flush(); 
            }

            System.out.println("Saindo do chat...");
            receptor.interrupt();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
