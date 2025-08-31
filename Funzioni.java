import java.util.InputMismatchException;
import java.util.Scanner;

public class Funzioni{

    static Utente utente;
    static Categoria categoria;
    static Scanner sc = new Scanner(System.console().reader());

    static String stato = "\nnessun utente : nessuna categoria\n";


    //stampa lo stato 
    public static void printStato(){
        System.out.println(stato);
    }

    //stampa il menu e restituisce l'attività selezionata dall'utente o -1 se non esiste
    static int printMenu(){
        int selezione = 1;//definisce la quantita massima di opzioni
        int uscita;//l'opzioni richiesta dall'utente
        //menu senza utente

        System.out.println("[-1] Esci dal programma");
        System.out.println("[0] Cerca un oggetto per nome");
        //menu con utente base
        if(utente instanceof Utente){
            selezione = 4;
            System.out.println("[1] Cambia utente");
            System.out.println("[2] Seleziona una categoria");
            System.out.println("[3] Prendi in prestito un oggetto");
            System.out.println("[4] Restituisci un oggetto");
            
        }else{
            System.out.println("[1] Effettua il log in");
        }
        //menu per un utente superiore
        if(utente instanceof UtenteSuperiore){
                selezione = 10;
                System.out.println("[5] Crea un oggetto");
                System.out.println("[6] Elimina un oggetto");
                System.out.println("[7] Crea una categoria");
                System.out.println("[8] Elimina una categoria");
                System.out.println("[9] Crea un utente");
                System.out.println("[10] Cancella un utente");
        }
        System.out.print("Digita il numero dell'operazione da eseguire: ");
        try{
            uscita = sc.nextInt();
            uscita = uscita > selezione ? -2 : uscita;
        }catch(InputMismatchException e){
            System.out.println("\nInput non valido");
            sc.nextLine();//svuota il buffer dello scanner
            return -2;
        }

        return uscita;
    }
    
    //funzione che gestisce il login di un utente
    static void logIn(){
        String username;
        String password;
        //verifica l'assenza di utent nel file utenti
        if(Utente.zeroUtenti()){
            //se non soo presenti viene creat un utente superiore
            System.out.println("non esistono utenti voui creare un nuovo utente superiore?[S o N]");
            if(sc.next().toUpperCase().equals("S")){
                System.out.println("Inserisci nome utente: ");
                username = sc.next();
                System.out.println("Inserisci nome password: ");
                password = sc.next();

                UtenteSuperiore.creaUtente(username, password);
                utente = new UtenteSuperiore(username);
                //modifica lo stato e verifica se è stata selezionata una categoria
                stato = (categoria == null) ? "\n" + utente.stampaStato() + "nessuna categoria\n" : "\n" + utente.stampaStato() + " : " + categoria.getNome() + "\n";

            }
        }else{
            //se sono presenti utenti vengono richieste le credenziali di accesso
            System.out.print("Inserisci nome utente: ");
            username = sc.next();
            //verfifica la presenza dell'utente selezionato
            if(Utente.esisteUtente(username) > -1){

                System.out.print("Inserisci password: ");
                password = sc.next();
                String[] credenziali = Utente.getCredenziali(username);
                //verifica la password
                if(Integer.parseInt(credenziali[0]) == password.hashCode()){
                    if(credenziali[1].equals("sup")){
                        //accesso come utente superiore
                        utente = new UtenteSuperiore(username);
                        System.out.println("Accesso eseguito con successo come utente superiore " + username  + "\n"); 
                        
                    }else{ 
                        //accesso come utente base
                        utente = new Utente(username);
                        System.out.println("Accesso eseguito con successo come utente base " + username);
                    }
                    //modifica lo stato e verifica se è stata selezionata una categoria
                    stato = (categoria == null) ? "\n" + utente.stampaStato() + " : nessuna categoria\n" : "\n" + utente.stampaStato() + " : " + categoria.getNome() + "\n";
                }else{
                        System.out.println("Credenziali sbagliate\n");
                }

            }else{
                System.out.print("L'utente non esiste\n");
            }
        }
    }

    static void selezionaCategoria(){
        String[] listaCategorie = Categoria.getListaCategorie();
        int numeroSelezionato;
        //verifica che ci sia almeno una categoria
        if(listaCategorie == null){
            System.out.println("il tuo catalogo è vuoto\n");
        }else{
            while(true){
                //da la lista delle categorie trovate fino a che non viene selezionata una
                //con -1 non si seleziona nessuna
                System.out.println("[-1] nessuna categoria");
                for (int i = 0; i<listaCategorie.length; i++) {
                    System.out.printf("[%d] %s\n", i, listaCategorie[i]);
                }
                System.out.print("\ndigita il numero dalla categoria che vuoi selezionare: ");
                try{
                    //verifica che in input venga inserito un numero
                    numeroSelezionato = sc.nextInt();
                    if(numeroSelezionato >= listaCategorie.length || numeroSelezionato < -1){
                        System.out.println("il numero non rientra nella selezione, ritenta\n");
                    }else{
                        break;
                    }
                }catch(InputMismatchException e){
                    System.out.println("Input non valido\n");
                }
            }
            if(numeroSelezionato == -1){
                categoria = null;
                stato = "\n" + utente.stampaStato() + " : nessuna categoria\n";
            }else{
                categoria = new Categoria(listaCategorie[numeroSelezionato]);
                stato = "\n" + utente.stampaStato() + " : " + categoria.getNome() + "\n";
            }
        }
    }
    //cerca un oggetto per nome
    public static void cercaPerNome(){
        System.out.print("inserisci il nome dell'oggetto da cercare: ");
        String nomeRicerca = sc.next();
        //verifica che sia stata selezionata una categoria
        //se non è selezinata cerca in tutte le categorie altrimenti cerca solo in quella selezionata
        if(!(categoria instanceof Categoria)){
            String[] listaCategorie = Categoria.getListaCategorie();
            if(listaCategorie == null){
                System.out.println("il tuo catalogo è vuoto\n");
            }else{
                for(int i = 0; i<listaCategorie.length; i++){
                    categoria = new Categoria(listaCategorie[i]);
                    categoria.cercaPerNome(nomeRicerca);
                }
                categoria = null;
            }
        }else{
            categoria.cercaPerNome(nomeRicerca);
        }
    }

    public static void prestitoOggetto(){
        String nomeOggetto;
        int quantitaOggetti;
        //verifica che sia stata selezionata una categoria
        if(!(categoria instanceof Categoria)){
            System.out.println("per prendere un oggetto devi prima selezionare una categoria. Vuoi selezionare una categoria? [S o N]");
            if(sc.next().toUpperCase().equals("S")){
                //se l'utente vuole viene selezionata una categoria
                selezionaCategoria();
            }
        }
        if(categoria instanceof Categoria){
            System.out.print("Inserisci il nome dell'oggetto: ");
            nomeOggetto = sc.next();
            //se l'oggetto esiste viene presa la quantità di oggetti da prendere
            if(categoria.esisteOggetto(nomeOggetto) > -1){
                System.out.print("Inserisci la quantita di oggetti che vuoi prendere: ");
                quantitaOggetti = sc.nextInt();
                //viene creato un nuovo prestito
                utente.nuovoPrestito(categoria, nomeOggetto, quantitaOggetti);
            }else{
                System.out.println("L'oggetto non esiste in questa categoria\n");
            }
        }
    }


    public static void restituisciOggetto(){
        String nomeOggetto;
        //verifica che sia stata selezionata una categoria
        if(!(categoria instanceof Categoria)){
            System.out.println("per restituire un oggetto devi prima selezionare una categoria. Vuoi selezionare una categoria? [S o N]");
            if(sc.next().toUpperCase().equals("S")){
                //se l'utente vuole viene selezionata una categoria
                selezionaCategoria();
            }
        }
        if(categoria instanceof Categoria){
            System.out.print("Inserisci il nome dell'oggetto: ");
            nomeOggetto = sc.next();
            utente.restituisciPrestito(categoria, nomeOggetto);
        }
    }

    public static void creaOggetto(){
        String nomeOggetto;
        //verifica che sia stata selezionata una categoria
        if(!(categoria instanceof Categoria)){
            System.out.println("Per creare un oggetto devi prima selezionare la categoria in cui vuoi crearlo. Vuoi selezionare una categoria? [S o N]");
            if(sc.next().toUpperCase().equals("S")){
                //se l'utente vuole viene selezionata una categoria
                selezionaCategoria();
            }
        }
        if(categoria instanceof Categoria){
            String[] nuovoOggetto = new String[3];
            String nuovaDescrizione = "";

            System.out.print("Inserisci il nome dell'oggetto: ");
            nuovoOggetto[0] = sc.next();//nome
            //verifico l'esistenza dell'oggetto
            if(categoria.esisteOggetto(nuovoOggetto[0]) == -1){
                System.out.print("Inserisci la quantita dell'oggetto: ");
                nuovoOggetto[1] = sc.next(); //quantità
            
                System.out.println("Inserisci la descrizione (per concludere scrivi *END*): ");
                //la descrizione puo essere multi riga quindi vine letta fino a che non viene terminata con *END*
                while (true) { 
                    nuovaDescrizione += sc.nextLine() + "\n";
                    if(nuovaDescrizione.endsWith("*END*\n")){

                        nuovoOggetto[2] = nuovaDescrizione.replace("*END*\n", "");
                        break;
                    }

                }
                categoria.aggiungiOggetto(nuovoOggetto);
            
            }else{
                System.out.print("l'oggetto è già presente in questa categoria\n");

            }
        }
    }

    public static void eliminaOggetto(){
        String nomeOggetto;
        int quantita;
        //verifica che sia stata selezionata una categoria
        if(!(categoria instanceof Categoria)){
            System.out.println("Per creare un oggetto devi prima selezionare la categoria in cui vuoi crearlo. Vuoi selezionare una categoria? [S o N]");
            if(sc.next().toUpperCase().equals("S")){
                //se l'utente vuole viene selezionata una categoria
                selezionaCategoria();
            }
        }
        //se è stata selezionata una categoria l'egge il nome dell'oggetto
        if(categoria instanceof Categoria){
            System.out.print("Inserisci il nome dell'oggetto: ");
            nomeOggetto = sc.next();
            if(categoria.esisteOggetto(nomeOggetto) > -1){
                System.out.print("Inserisci la quantita di oggetti da eliminare (per cancellarlo completamente " + categoria.getQuantita(nomeOggetto) +"): ");
                quantita = sc.nextInt();

                categoria.eliminaOggetto(nomeOggetto, quantita);
            }else{
                System.out.println("L'oggetto non esiste\n");
            }
        }
    }

    public static void creaCategoria(){
        //legge il nome della nuova categoria
        System.out.println("Inserisci il nome della nuova categoria");
        String nomeCategoria = sc.next();
        //verifica che non esista già
        if(Categoria.getPresenzaCategoria(nomeCategoria)){
            System.out.println("La categoria è già esistente");
        }else{
            //costruisce l'oggetto, selezionando la ategoria
            categoria = new Categoria(nomeCategoria);
        }
    }

    public static void eliminaCategoria(){
        String[] listaCategorie = Categoria.getListaCategorie();
        int numeroSelezionato;
        //verifica che ci sia almeno una categoria
        if(listaCategorie == null){
            System.out.println("il tuo catalogo è vuoto\n");
        }else{
            while(true){
                //da la lista delle categorie trovate fino a che non viene selezionata una
                //con -1 non si seleziona nessuna
                System.out.println("[-1] nessuna categoria");
                for (int i = 0; i<listaCategorie.length; i++) {
                    System.out.printf("[%d] %s\n", i, listaCategorie[i]);
                }
                System.out.print("\ndigita il numero dalla categoria che vuoi selezionare: ");
                try{
                    //verifica che in input venga inserito un numero
                    numeroSelezionato = sc.nextInt();
                    if(numeroSelezionato >= listaCategorie.length || numeroSelezionato < -1){
                        System.out.println("il numero non rientra nella selezione, ritenta\n");
                    }else{
                        break;
                    }
                }catch(InputMismatchException e){
                    System.out.println("Input non valido\n");
                }
            }
        //se la categoria selezionata è anche quella da eliminare, esce dalla categoria
        if(categoria instanceof Categoria){
            if(categoria.getNome().equals(listaCategorie[numeroSelezionato])){
                categoria = null;
            }
        }
        //verifico che l'utente non abbia scelto di non selezionare nessuna categoria
        if(numeroSelezionato > -1){
            //cancello la categoria
            Categoria.eliminaCategoria(listaCategorie[numeroSelezionato]);
            System.out.println("La categoria " + listaCategorie[numeroSelezionato] + " è stata cancellata con successo");
        }
        
    }
    }

    public static void creaUtente(){
        String nomeUtente, password, grado;
        //ottiene le nuove credenziali
        System.out.print("Inserisci il nome utente: ");
        nomeUtente = sc.next();
        System.out.print("Inserisci la password: ");
        password = sc.next();
        //richiede il livello di accesso. Se non riconosce il livello ripete l'operazione
        while(true){
            System.out.println("Vuoi creare un utente superiore [sup] o un utente base [std]: ");
            grado = sc.next();
            if(grado.toUpperCase().equals("SUP")){
                UtenteSuperiore.creaUtente(nomeUtente, password);
                break;
            }else if(grado.toUpperCase().equals("STD")){
                Utente.creaUtente(nomeUtente, password);
                break;
            }else{
                System.out.println("Grado non riconosciuto, ritenta.");
            }
        }
    }

    public static void eliminaUtente(){
        String nomeUtente, password;
        //ottengo il nome utente da eliminare e la password dell'utente
        System.out.print("Inserisci il nome utente da cancellare: ");
        nomeUtente = sc.next();
        System.out.print("Inserisci la tua password per confermare l'operazione: ");
        password = sc.next();
        //verifica che esista l'utente selezionato
        if(Utente.esisteUtente(nomeUtente) > -1){
            //se esiste verifica le credenziali
            if(password.hashCode() == Integer.parseInt(Utente.getCredenziali(utente.getUsername())[0])){
                //se sono corrette lo cancella
                UtenteSuperiore.cancellaUtente(nomeUtente);
            }else{
                System.out.println("password errata");
            }
        }else{
            System.out.println("L'utente selezionato non esiste");
        }
    }


}