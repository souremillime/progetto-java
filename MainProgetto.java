import java.util.Scanner;

public class MainProgetto{
    static Utente utente;
    static Categoria categoria;
    static Scanner sc = new Scanner(System.console().reader());

    static String stato = "\nnessun utente\n";


    public static void main(String[] args){

        System.out.println(stato);
        switch(printMenu()){
            case 0:
                System.out.println("inserisci il nome dell'oggetto da cercare: ");
                String nomeRicerca = sc.next();
                if(!(categoria instanceof Categoria)){
                    String[] listaCategorie = Categoria.getListaCategorie();
                    if(listaCategorie == null){
                        System.out.println("il tuo catalogo è vuoto");
                        break;
                    }
                    for(int i = 0; i<listaCategorie.length; i++){
                        categoria = new Categoria(listaCategorie[i]);
                        categoria.cercaPerNome(nomeRicerca);
                    }
                    categoria = null;
                }else{
                    categoria.cercaPerNome(nomeRicerca);
                }
                break;
            case 1:
                logIn();
                break;

            case 2:
                

            default:
                System.out.println("l'operoazione selezionata non esiste");

        }
    }

    //stampa il menu e restituisce l'attività selezionata dall'utente o -1 se non esiste
    static int printMenu(){
        int selezione = 1;//definisce la quantita massima di opzioni
        int uscita;//l'opzioni richiesta dall'utente
        //menu senza utente
        System.out.println("[0] cerca un oggetto per nome");
        //menu con utente base
        if(utente instanceof Utente){
            System.out.println("[1] cambia utente");
            //menu in presenza di una categoria selezionata
            if(categoria instanceof Categoria){
                selezione = 3;
                System.out.println("[2] prendi in prestito un oggetto");
                System.out.println("[3] restituisci un oggetto");
            }else{
                //menu in assenza di una categoria selezionata
                selezione = 2;
                System.out.println("[2] seleziona una categoria");
            }
        }else{
            System.out.println("[1] effettua il log in");
        }
        //menu per un utente superiore
        if(utente instanceof UtenteSuperiore){
            if(categoria instanceof Categoria){
                selezione = 7;
                System.out.println("[4] crea un oggetto");
                System.out.println("[5] crea una categoria");
                System.out.println("[6] crea un utente");
                System.out.println("[7] cancella un utente");
            }else{
                selezione = 5;
                System.out.println("[3] crea una categoria");
                System.out.println("[4] crea un utente");
                System.out.println("[5] cancella un utente");

            }
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