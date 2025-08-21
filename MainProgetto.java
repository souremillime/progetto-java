import java.util.InputMismatchException;
import java.util.Scanner;

public class MainProgetto{
    static Utente utente;
    static Categoria categoria;
    static Scanner sc = new Scanner(System.console().reader());

    static String stato = "\nnessun utente\n";


    static String nomeOggetto;
    static int quantitaOggetti;


    public static void main(String[] args){
        while(true){
            System.out.println(stato);
            switch(printMenu()){
                //cerca un oggetto per nome
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
                //cambia utente o esegui il login
                case 1:
                    logIn();
                    break;

                //seleziona una categoria
                case 2:
                    selezionaCategoria();
                    break;
                //prend in prestito un oggetto
                case 3:
                    if(!(categoria instanceof Categoria)){
                        System.out.println("per prendere un oggetto devi prima selezionare una categoria. Vuoi selezionare una categoria? [S o N]");
                        if(sc.next().toUpperCase().equals("S")){
                            selezionaCategoria();
                        }else{
                            break;
                        }
                    }
                    System.out.print("Inserisci il nome dell'oggetto: ");
                    nomeOggetto = sc.next();
                    System.out.print("Inserisci la quantita di oggetti che vuoi prendere: ");
                    quantitaOggetti = sc.nextInt();
                    utente.nuovoPrestito(categoria, nomeOggetto, quantitaOggetti);
                    break;
                //restituisci un oggetto
                case 4:
                    if(!(categoria instanceof Categoria)){
                        System.out.println("per restituire un oggetto devi prima selezionare una categoria. Vuoi selezionare una categoria? [S o N]");
                        if(sc.next().toUpperCase().equals("S")){
                            selezionaCategoria();
                        }else{
                            break;
                        }
                    }
                    System.out.print("Inserisci il nome dell'oggetto: ");
                    nomeOggetto = sc.next();
                    utente.restituisciPrestito(categoria, nomeOggetto);
                    break;
                //crea un oggetto
                case 5:
                    if(!(categoria instanceof Categoria)){
                        System.out.println("Per creare un oggetto devi prima selezionare la categoria in cui vuoi crearlo. Vuoi selezionare una categoria? [S o N]");
                        if(sc.next().toUpperCase().equals("S")){
                            selezionaCategoria();
                        }else{
                            break;
                        }
                    }
                    String[] nuovoOggetto = new String[3];
                    String nuovaDescrizione = "";

                    System.out.print("Inserisci il nome dell'oggetto: ");
                    nuovoOggetto[0] = sc.next();
                    if(categoria.esisteOggetto(nuovoOggetto[0]) == -1){
                        System.out.print("Inserisci la quantita dell'oggetto: ");
                        nuovoOggetto[1] = sc.next();
                    
                        System.out.println("Inserisci la descrizione (per concludere scrivi *END*): ");
                        while (true) { 
                            nuovaDescrizione += sc.nextLine() + "\n";
                            if(nuovaDescrizione.endsWith("*END*")){

                                nuovoOggetto[2] = nuovaDescrizione.replace("*END*", "");
                                break;
                            }

                        }
                        categoria.aggiungiOggetto(nuovoOggetto);
                    
                    }
                    break;

                case 6:
                    System.out.println("Inserisci il nome della nuova categoria");
                    String nomeCategoria = sc.next();
                    if(Categoria.esisteCategoria(nomeCategoria)){
                        System.out.println("La categoria è già esistente");
                    }else{
                        categoria = new Categoria(nomeCategoria);
                    }
                case 7: 
                case 8:
                
                //esci dal programma
                case -1:
                    System.out.println("Esco dal programma...");
                    System.exit(0);

                default:
                    System.out.println("l'operoazione selezionata non esiste");

            }
        }
    }

    //stampa il menu e restituisce l'attività selezionata dall'utente o -1 se non esiste
    static int printMenu(){
        int selezione = 1;//definisce la quantita massima di opzioni
        int uscita;//l'opzioni richiesta dall'utente
        //menu senza utente

        System.out.println("[-1] Esci dal programma");
        System.out.println("[0] cerca un oggetto per nome");
        //menu con utente base
        if(utente instanceof Utente){
            selezione = 4;
            System.out.println("[1] cambia utente");
            System.out.println("[2] seleziona una categoria");
            System.out.println("[3] prendi in prestito un oggetto");
            System.out.println("[4] restituisci un oggetto");
            
        }else{
            System.out.println("[1] effettua il log in");
        }
        //menu per un utente superiore
        if(utente instanceof UtenteSuperiore){
                selezione = 8;
                System.out.println("[5] crea un oggetto");
                System.out.println("[6] crea una categoria");
                System.out.println("[7] crea un utente");
                System.out.println("[8] cancella un utente");
        }
        System.out.print("digita il numero dell'operazione da eseguire: ");
        try{
            uscita = sc.nextInt();
            uscita = uscita > selezione ? -2 : uscita;
        }catch(InputMismatchException e){
            System.out.println("Input non valido");
            return -2;
        }

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
                        System.out.println("Accesso eseguito con successo come utente base " + username);  
                    }else if(credenziali[1].equals("sup")){
                        utente = new UtenteSuperiore(username);
                        System.out.println("Accesso eseguito con successo come utente superiore " + username);  
                    }else{
                        System.out.println("Credenziali sbagliate");
                    }
                }
            }else{
                System.out.print("Credenziali sbagliate");
            }
        }
    }

    static void selezionaCategoria(){
        String[] listaCategorie = Categoria.getListaCategorie();
        int numeroSelezionato;
        if(listaCategorie == null){
            System.out.println("il tuo catalogo è vuoto");
        }else{
            while(true){
                for (int i = 0; i<listaCategorie.length; i++) {
                    System.out.printf("[%d] %s", i, listaCategorie[i]);
                }
                System.out.print("\ndigita il numero dalla categoria che vuoi selezionare: ");
                try{
                    numeroSelezionato = sc.nextInt();
                    if(numeroSelezionato >= listaCategorie.length || numeroSelezionato < 0){
                        System.out.println("il numero non rientra nella selezione ritenta\n");
                    }else{
                        break;
                    }
                }catch(InputMismatchException e){
                    System.out.println("Input non valido");
                }
            }

            categoria = new Categoria(listaCategorie[numeroSelezionato]);
        }
    }
}