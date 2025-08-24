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

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
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
        
    }
    //riduce la quantità di un oggetto specifico
    public void riduciQuantita(String nome, int riduci){
        //verifico la sua esistenza
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, 1));
            if(riduci <= quantitaOggetto){
                quantitaOggetto = quantitaOggetto - riduci;
                riscriviElementoCSV(String.valueOf(quantitaOggetto), 1, posizioneOggetto);
                salvaModifiche();
            }else{
                System.out.println("La quantità degli oggetti è inferiore");
            }
        }
    }

    //aumenta la quantita di un oggetto specifico
     public void aumentaQuantita(String nome, int aumenta){
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, 1));
                quantitaOggetto = quantitaOggetto + aumenta;
                riscriviElementoCSV(String.valueOf(quantitaOggetto), 1, posizioneOggetto);
                salvaModifiche();
        }
    }

/*static*/

    static boolean getPresenzaCategoria(){
        File cartella = new File(cartellaStd);
        if(cartella.isDirectory()){
            String[] lista = cartella.list();
            for (String file : lista) {
                if( file.endsWith(".csv"))
                    return true;
            }
            
        }
        return false;

    }

    static boolean esisteCategoria(String nomeCategoria){
        File cartella = new File(cartellaStd);
        if(cartella.isDirectory()){
            String[] lista = cartella.list();
            for (String file : lista) {
                if( file.contains(nomeCategoria))
                    return true;
            }
            
        }
        return false;

    }

    public static String[] getListaCategorie(){
        File cartella = new File(cartellaStd);
        String[] lista = null;
        int riduci = 0;
        if(cartella.isDirectory()){
            lista = cartella.list();
            for (int i = 0; i<lista.length; i++) {
                System.out.println(lista[i]);
                if(!lista[i].endsWith(".csv")){
                    lista[i] = null;
                    riduci++;
                }else{
                    lista[i] = lista[i].replace(".csv", "");
                }
            }
            if(riduci>0){
                String[] riduzione = new String[lista.length - riduci];
                int i = 0,j = 0;
                while (i < riduzione.length) {
                    if(lista[i] == null){
                        continue;
                    }else{
                        riduzione[j] = lista[i];
                        j++;
                    }
                    i++;
                }
                lista = riduzione;
            }
            
        }
        return lista;

    }

    public static void eliminaCategoria(String nomeCategoria){
        File eliminaFile = new File(cartellaStd + nomeCategoria + ".csv");
        if(eliminaFile.isFile()){
            eliminaFile.delete();
        }
    }

}