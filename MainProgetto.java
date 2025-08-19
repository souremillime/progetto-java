import java.util.Scanner;

public class MainProgetto{
    static Utente utente;
    static Scanner sc = new Scanner(System.console().reader());


    public static void main(String[] args){
        Categoria bob = new Categoria("Categorie/bobTest.csv");
        bob.print(0, 3);
        bob.cercaPerNome("a");
        System.out.println(Categoria.getPresenzaCategoria());
        switch(printMenu(utente)){
            case 0 :
                System.out.println("inserisci il nome dell'oggetto da cercare: ");
                bob.cercaPerNome(sc.next());
                break;
            case 1:
                logIn();
            default:
                System.out.println("l'operoazione selezionata non esiste");

        }
    }


    static int printMenu(Utente utente){
        int selezione = 1;
        int uscita;
        System.out.println("[0] cerca un oggetto per nome");
        if(utente instanceof Utente){
            selezione = 2;
            System.out.println("[1] cambia utente");
            System.out.println("[2] prendi un oggetto");
        }else{
            System.out.println("[1] effettua il log in");
        }
        if(utente instanceof UtenteSuperiore){
            selezione = 5;
            System.out.println("[3] aggiungi una categoria");
            System.out.println("[4] aggiungi un oggetto");
            System.out.println("[5] crea un utente");
        }
        System.out.print("digita il numero dell'operazione da eseguire: ");
        uscita = sc.nextInt();
        uscita = uscita > selezione ? -1 : uscita;

        return uscita;
    }
    
    //funzione che gestisce il login di un utente
    static void logIn(){
        String username;
        String password;

        if(Utente.zeroUtenti()){
            System.out.println("non esistono utenti voui crearne un nuovo utente superiore?[S]o[N]");
            if(sc.next().toUpperCase().equals("S")){
                System.out.println("Inserisci nome utente: ");
                username = sc.next();
                System.out.println("Inserisci nome password: ");
                password = sc.next();

                UtenteSuperiore.creaUtente(username, password);
            }
        }else{
            System.out.print("Inserisci nome utente: ");
            username = sc.next();
            if(Utente.esisteUtente(username)){

                System.out.print("Inserisci password: ");
                password = sc.next();
                String[] credenziali = Utente.getCredenziali(username);
                if(Integer.parseInt((credenziali[0])) == password.hashCode()){
                    if(credenziali[1].equals("std")){
                        utente = new Utente(username);
                    }else if(credenziali[1].equals("sup")){
                        utente = new UtenteSuperiore(username);
                    }else{
                        System.out.print("Credenziali sbagliate");
                    }
                    System.out.print("Accesso eseguito con successo" + username);  
                }
            }else{
                System.out.print("Credenziali sbagliate");
            }
        }
    }
}