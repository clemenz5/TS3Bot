import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.Property;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.sun.org.apache.xalan.internal.utils.XMLSecurityPropertyManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class main {
    static TS3Api api;

    public static void main(String[] args) {

        final TS3Config config = new TS3Config();
        config.setHost("88.198.0.116");

        final TS3Query query = new TS3Query(config);
        query.connect();

        api = query.getApi();
        api.login("kronBot", "jeWpsvuX");
        api.selectVirtualServerById(1);
        //api.setNickname("Luis");
        Scanner scanner = new Scanner(System.in);
        String command;
        do {
            print("+");
            command = scanner.next();
            switch (command) {
                default:
                    print("Unknwon Command");
                    break;
                case "help":
                    print("you might try:");
                    print("printCurrentClients");
                    print("quit");
                    print("setNickname");
                    print("pokeClient");
                    break;
                case "printCurrentClients":
                    printCurrentClients();
                    break;
                case "quit":
                    break;
                case "setNickname":
                    setNickname(scanner);
                    break;
                case "pokeClient":
                    pokeClient(scanner);
                    break;
                case "pokeAll":
                    pokeAll(scanner);
                    break;
                case "text":
                    text(scanner);
                    break;
                case "textAll":
                    textAll(scanner);
                    break;
                case "kick":
                    kick(scanner);
                    break;
                case "unban":
                    unban(scanner);
                    break;
                case "unbanAll":
                    unbanAll();
                    break;
            }
            print("-");

        } while (!command.equals("quit"));
        System.exit(0);
    }

    private static void printCurrentClients() {
        List<Client> clientList = api.getClients();
        print("Client List in format of [id] [nickname]");
        for (Client client : clientList) {
            print(client.getId() + " " + client.getNickname());
        }
    }

    private static void setNickname(Scanner scanner){
        String nickname = scanner.next();
        api.setNickname(nickname);
    }

    private static void print(String s){
        System.out.println(s);
    }

    private static void pokeClient(Scanner scanner){
        printCurrentClients();
        print("give the id of the user you want to poke (or 0 to cancel)");
        int clientID = scanner.nextInt();
        if(clientID == 0){
            return;
        }
        print("give now the poke message");
        String message = scanner.nextLine();
        api.pokeClient(clientID, message);
    }

    private static void pokeAll(Scanner scanner){
        List<Integer> clientIDList = getIDList();
        print("give the poke message");
        String message = scanner.nextLine();
        for(Integer id : clientIDList){
            api.pokeClient(id, message);
        }
    }

    private static void text(Scanner scanner){
        printCurrentClients();
        print("give the id of the user you want to text (or 0 to cancel)");
        int clientID = scanner.nextInt();
        if(clientID == 0){
            return;
        }
        print("give now the message");
        String message = scanner.next();
        api.sendPrivateMessage(clientID, message);
    }

    private static void textAll(Scanner scanner){
        List<Integer> clientIDList = getIDList();
        print("give the message");
        String message = scanner.next();
        for(Integer id : clientIDList){
            api.sendPrivateMessage(id, message);
        }
    }

    private static List<Integer> getIDList(){
        List<Client> clientList = api.getClients();
        List<Integer> clientIDList = new LinkedList<>();
        for (Client client : clientList) {
            clientIDList.add(client.getId());
        }
        Integer myID = api.whoAmI().getId();
        clientIDList.remove(myID);
        return clientIDList;
    }

    private static void kick(Scanner scanner){
        printCurrentClients();
        print("which client is to kick (0 to cancel)");
        int clientID = scanner.nextInt();
        if(clientID == 0){
            return;
        }
        api.kickClientFromServer(clientID);
    }

    private static void unban(Scanner scanner){
        printBans();
        print("which client is to unban (0 to cancel)");
        int clientID = scanner.nextInt();
        if(clientID == 0){
            return;
        }
        api.deleteBan(clientID);
    }

    private static void unbanAll(){
        if(api.getBans().size() == 0){
            print("no bans exist");
        }else{
            printBans();
            print("were unbanned");
            api.deleteAllBans();

        }
    }

    private static void printBans(){
        List<Ban> banList = api.getBans();
        for(Ban ban: banList){
            print(ban.getId() + " " + ban.getLastNickname());
        }
    }

}
