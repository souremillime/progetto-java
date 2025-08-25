import java.io.File;

public class Categoria extends GestoreCSV{
    String nomeCategoria;
    static String cartellaStd = "Categorie"; //path relativo della cartella standard 

/*costruttore*/

    public Categoria(String pathCategoria) {
        super(cartellaStd + "/" + pathCategoria + ".csv"); //gestione del file csv
        File cartella = new File(cartellaStd);
        //creo l cartella standard se non presente
        if(!cartella.isDirectory()){
            if(cartella.mkdir()){
                System.out.println("attenzione, la creazione della cartella contenente le ctegorie non è avvenuta con successo");
            } 
        }
        //inizializo il nome della categoria con il nome del file 
        nomeCategoria = new File(pathCategoria).getName().replace(".csv", "");

    }

/*get o lettura*/
    //restituisce il nome della categoria
    public String getNome(){
        return nomeCategoria;
    }
    //stampa gli oggetti che contengono la stringa nome
    public void cercaPerNome(String nome) {
        String sottoStringa;
        //stampa il nome della categoria
        System.out.println("\nCategoria: " + nomeCategoria + "\n");
        //verifica l'esistenza di almeno un oggetto
        if(esisteOggetto(nome)<0){
            System.out.println("nessun oggetto trovato");
        }else{
            System.out.println("nome, quantita, descrizione");
            for (int i = 0; i<getNumeroRighe(); i++) {
                if(getAtMappaFile(i,0).contains(nome)){
                    sottoStringa = getAtMappaFile(i,2);
                    //taglio la stringa se troppo lunga e aggiungo ...
                    if(sottoStringa.length()>19){
                        sottoStringa = sottoStringa.substring(0, 20);
                        if(getAtMappaFile(i, 2).length() >20){
                            sottoStringa += "...";
                        } 
                    }
                    System.out.printf("\t|  %s  |  %s  |  %s  |\n", getAtMappaFile(i,0), getAtMappaFile(i,1), sottoStringa);
                }
            }
        }
    }
    //restituisce la quantita di un oggetto specifio
    public int getQuantita(String nome){
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");
            return -1;

        }

        return Integer.parseInt(getAtMappaFile(posizioneOggetto, 1)); 
       
    }

    //verifica l'esistenza di un oggetto e ne restituisce la riga nella mappa, altrimenti restituisce -1
    public int esisteOggetto(String nomeOggetto){
        for (int i = 0; i<getNumeroRighe(); i++) {
            if(getAtMappaFile(i,0).equals(nomeOggetto)){
                return i;
            }
        }
        return -1;
    }



/*set*/

    //aggiunge un oggetto alla categoria o se già presente ne incrementa la quantità
    public void aggiungiOggetto(String[] oggetto){
        //verifico l'esistenza dell'oggetto
        int posizioneOggetto = esisteOggetto(oggetto[0]);
        //se non esiste creo un nuovo oggetto
        if(posizioneOggetto == -1){
            nuovaRigaCSV(oggetto);//creo una nuova riga nel file
            salvaModifiche();

        }else{
            //se esiste aumento la quantita dell'oggetto
            System.out.println("l'oggetto è stato aggiunto a un oggetto già esistente");
            String[] nuovoOggetto = getAtMappaFile(posizioneOggetto);
            nuovoOggetto[1] = String.valueOf(Integer.parseInt(nuovoOggetto[1]) + Integer.parseInt(oggetto[1]));// incremento la quantità
            riscriviRigaCSV(nuovoOggetto, posizioneOggetto);//riscrivo la riga
            salvaModifiche();
        }
        
    }

    //riduce la quantità di un oggetto o lo cancella definitivamente 
    public void eliminaOggetto(String nome, int quantita){
        //verifico la sua esistenza
        int posizioneOggetto = esisteOggetto(nome);
            //leggo la sua quantità
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, 1)); 
            //se la quantita richiesta è maggiore di quella dell'oggetto viene cancellato
            if(quantita >= quantitaOggetto){
                cancellaRigaCSV(posizioneOggetto);
                salvaModifiche();
            }else{
                //altrimenti la sua quantità viene ridotta
                String[] nuovoOggetto = getAtMappaFile(posizioneOggetto);
                nuovoOggetto[1] = String.valueOf(Integer.parseInt(nuovoOggetto[1]) - quantita);
                riscriviRigaCSV(nuovoOggetto, posizioneOggetto);
                salvaModifiche();
            }
        
    }
    //riduce la quantità di un oggetto specifico
    public void riduciQuantita(String nome, int riduci){
        //verifico la sua esistenza
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
            //leggo la quantità dell'oggetto
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, 1));
            //verifico che la quantità sia maggiore della quantità da ridurre
            if(riduci <= quantitaOggetto){
                //riduco la quantità
                quantitaOggetto = quantitaOggetto - riduci;
                //riscrivo la riga
                riscriviElementoCSV(String.valueOf(quantitaOggetto), 1, posizioneOggetto);
                salvaModifiche();
            }
        }
    }

    //aumenta la quantita di un oggetto specifico
     public void aumentaQuantita(String nome, int aumenta){
        //verifico la sua esistenza
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
            //leggo la quantità dell'oggetto
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, 1));
                //incremento la quantità
                quantitaOggetto = quantitaOggetto + aumenta;
                //riscrivo la riga
                riscriviElementoCSV(String.valueOf(quantitaOggetto), 1, posizioneOggetto);
                salvaModifiche();
        }
    }

/*static*/
    //verifico la presenza di almeno una categoria
    static boolean getPresenzaCategoria(){
        File cartella = new File(cartellaStd);
        //verifico che la cartella standard esista e sia una cartella
        if(cartella.isDirectory()){
            //lista di tutti i file dentro la cartella
            String[] lista = cartella.list();
            //leggo tutti i file fino a trovare uno in formato .csv
            for (String file : lista) {
                if( file.endsWith(".csv"))
                    return true; //se lo trova restituisce vero
            }
            
        }
        return false; //se non trova niente restituisce falso

    }
    //verifico la presenza di una categoria nello specifico
    static boolean getPresenzaCategoria(String nomeCategoria){
        File cartella = new File(cartellaStd);
        //verifico che la cartella standard esista e sia una cartella
        if(cartella.isDirectory()){
            //lista di tutti i file dentro la cartella
            String[] lista = cartella.list();
            //leggo tutti i file fino a trovare uno che ha il nome della categoria
            for (String file : lista) {
                if(file.contains(nomeCategoria))
                    return true; //se lo trova restituisce vero
            }
            
        }
        return false;//altrimenti se non lo trova falso

    }
    //restituisce la lista di tutte le categorie presenti nella cartella
    public static String[] getListaCategorie(){
        File cartella = new File(cartellaStd);
        String[] lista = null;
        int riduci = 0; //valore di riduzione della lunghezza del array
        if(cartella.isDirectory()){
            //lista di tutti i file dentro la cartella
            lista = cartella.list();
            for (int i = 0; i<lista.length; i++) {
                //se la lista non contiene il nome di un fle con .csv ne cancello il contenuto
                if(!lista[i].endsWith(".csv")){
                    lista[i] = null;
                    riduci++;
                }else{
                    //altrimenti cancello il .csv
                    lista[i] = lista[i].replace(".csv", "");
                }
            }
            //se sono stati trvati dei file senza .csv riduco l'array
            if(riduci>0){
                //nuovo array con lunghezza ridotta
                String[] riduzione = new String[lista.length - riduci];
                int i = 0,j = 0;

                while (i < riduzione.length) {
                    //se trovo un oggetto nullo lo salto
                    if(lista[i] == null){
                        continue;
                    }else{
                        //altrimeti lo copio nel nuovo array
                        riduzione[j] = lista[i];
                        j++;
                    }
                    i++;
                }
                //faccio puntare lista al nuovo array ridotto
                lista = riduzione;
            }
            
        }
        return lista;

    }
    //elimina una categoria
    public static void eliminaCategoria(String nomeCategoria){
        File eliminaFile = new File(cartellaStd + nomeCategoria + ".csv");
        //se esise la cancella
        if(eliminaFile.isFile()){
            eliminaFile.delete();
        }
    }

}