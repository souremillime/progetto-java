import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestoreCSV{
    private String csvPath;
    private int numCol, numRighe;
    private String[][] mappaFile;
    private BufferedReader reader;
    private boolean vuoto;

//inizializzazione oggetto

    //costruttore - inizializza la mappa, definisce le dimensioni ed eventualmente crea il file se non esiste
    public GestoreCSV(String csvPath) {
        this.csvPath = csvPath;

        try{ 
            File file = new File(csvPath);
            File parent = file.getParentFile();
            //verifico l'esistenza di eventuali cartelle contenenti il file (come la cartella Categorie)
            if (parent != null && !parent.exists()) {
                if(!parent.mkdirs()){ // crea la cartella se non esiste
                    System.out.println("Errore durante la creazione delle cartelle contenenti il file");
                }
            }
            //se il file del csv non esiste viene creato
            if (!file.exists()) {
                if (!file.createNewFile()) {//creo il file se non esiste
                    System.out.println("errore durante la creazione del file");
                }
            }
            reader = new BufferedReader(new FileReader(csvPath));//preparo il reader
            leggiFile();

            if(numCol == 0){
                vuoto = true;
            }else{
                vuoto = false;
            }


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //definisce il numero di colonne e il numero di righe che ha un file CSV. Il reader viene anche resettato
    private void definisciDimensioni() throws IOException{
        //resetto il reader
        reader.close();
        reader = new BufferedReader(new FileReader(csvPath));
        //leggo la prima riga del file
        String csvString = reader.readLine();
        //verifico che non sia vuoto
        if(csvString == null){
            numCol = 0;
            numRighe = 0;
        }else{
            int count = 0;
            //inizio a contare il numero di colonne
            for (int i = 0; i < csvString.length(); i++) {
                if (csvString.charAt(i) == ',') {
                    count++;
                } else if (csvString.charAt(i) == '"') { 
                    //se viene trovata una virgoletta si salta il contenuto della stringa fino alla prossima
                    do{
                        //se si arriva a fine riga si concatena la riga seguante
                        if(i == csvString.length()-1){
                            csvString += reader.readLine();
                        }
                        i++;
                    }while (csvString.charAt(i) != '"');// do while perchè altrimenti si leggerebbe anche la prima virgoletta che terminerebbe il ciclo
                }
            }
            //salvo il numero di colonne
            numCol = count+1;
            count = 0; //resetto il contatore per le righe
            int numVirgolette = 0; //avvio il contatore per le virgolette

            //leggo il file fino a che non finisce
            while(csvString != null){
                //verifico la presenza di virolette
                if(csvString.contains("\"")){
                    int i = 0;
                    //conto il numero di virgolette presenti nella stringa letta
                    while(i < csvString.length()){
                        if(csvString.charAt(i) == '"'){
                            numVirgolette++;
                        }
                        i++;

                    }
                }
                //se le virgolette sono dispari, vuol dire che probabilmente la seguente si trova nella riga successiva
                if(numVirgolette%2 == 1){

                    csvString = reader.readLine();
                    continue;

                }else{//se non ci sono virgolette o le virgolette sono pari, vul dire che la riga è finita

                    count++;
                    numVirgolette = 0;
                }
                csvString = reader.readLine(); // inizio la lettura della riga segunete

            }

            numRighe = count; // salvo il numero di righe
        }

        reader.close();//chiusura del file
        reader = new BufferedReader(new FileReader(csvPath));//riavvio reader

    }

    //legge l'intero file e lo salva in memoria (Il reader viene resettato)
    private void leggiFile() throws IOException{
        String rigaLetta = "";
        definisciDimensioni();
        String[][] outString = new String[numRighe][numCol];
        int i = 0;                     
        int numVirgolette = 0;
        //legge riga per riga
        while(i < numRighe){
            rigaLetta += reader.readLine();
            numVirgolette = 0;
            //verifico la presenza di virolette
            if(rigaLetta.contains("\"")){

                for(int j = 0; j< rigaLetta.length(); j++){
                    if(rigaLetta.charAt(j) == '"'){
                        numVirgolette++;
                    }

                }
            }
                if(numVirgolette%2 == 1){     
                    rigaLetta += "\n";
                    continue;
                }else{
                    outString[i] = csvToString(rigaLetta);//conversione da codifica csv ad array di stringhe
                    rigaLetta = "";
                    i++;
                }
                
        }
        reader.close();
        reader = new BufferedReader(new FileReader(csvPath));
        this.mappaFile = outString;
    }

/*lettura*/

    //restituisce il valore della mappa del file alla posizione richiesta
    public String getAtMappaFile(int riga, int colonna){
    
        return mappaFile[riga][colonna];
    }
    //restituisce la riga selezionata della mappa del file
    public String[] getAtMappaFile(int riga){

        return mappaFile[riga];

    }
    //restituisce il numero di rigne del file
    public int getNumeroRighe(){
        return numRighe;
    }
    //restituisce le colonne della tabella csv
    public int getNumeroColonne(){
        return numCol;
    }

    //restituisce falso solo se il file non ha caratteri
    boolean getVuoto(){
        return vuoto;
    }

/*scrittura*/

    //aggiunge una riga alla mappa del file
    public void nuovaRigaCSV(String riga[]){
        vuoto = false;
        numRighe++;
        String[][] nuovaMappa = new String[numRighe][numCol]; //nuovo array con le nuove dimensioni
        //copio le informazioni del vecchio nel nuovo
        for (int i = 0; i < numRighe - 1; i++) {
            nuovaMappa[i] = mappaFile[i];
        }
        //aggiungo la nuova riga
        nuovaMappa[numRighe-1] = riga;
        mappaFile = nuovaMappa; //faccio puntare la mappa del file al nuovo array
        System.gc();//pulisco eventuali residui della vecchia mappa
        
    } 
    //cancella una riga della mappa del file
    public void cancellaRigaCSV(int posi){
        numRighe--;
        if(numRighe == 0){
            vuoto = true;
        }
        String [][] nuovaMappa = new String[numRighe][numCol];
        for (int i = 0; i < numRighe; i++) {
                if(posi == i){
                    continue;
                }
                nuovaMappa[i] = mappaFile[i];
            }
        mappaFile = nuovaMappa;
        System.gc();//pulisco eventuali residui della vecchia mappa

    }  
    

    //riscrive una riga della mappa del file
    public void riscriviRigaCSV(String riga[], int posi){

        mappaFile[posi] = riga;

    }

    //riscrive un elemento della mappa del file
    public void riscriviElementoCSV(String elemento, int posix, int posiy){
        
        mappaFile[posiy][posix] = elemento;

    }

    //scive su file le modifiche apportate alla mappa
    public void salvaModifiche(){
        try{
            //cancello dati nel file
            FileWriter writer = new FileWriter(csvPath);//modalità sovrascrittura
            writer.write("");
            writer.close();
            
            //inizio scrittura
            writer = new FileWriter(csvPath, true);//modalità senza sovrascrittura
            if(numRighe > 0){
                writer.write(stringToCSV(mappaFile[0]));//inizia senza capoverso

                for(int i = 1; i<numRighe;i++ ){
                    if(!(mappaFile[i] == null)){
                        writer.write("\n" + stringToCSV(mappaFile[i]));//con capoverso
                    }
                }
            }
            writer.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

/*sintassi CSV*/

    //conversione da codifica CSV a un array di stringhe
    private String[] csvToString(String csvString){
        int columnNum = 0, lastComma = -1; //inizia da -1 visto che nel substring viene sommato 1, questo per fare laggere il primo elemento dalla riga 0
        String outString[] = new String[numCol];
        
        //inizio la lettura carattere per carattere della stringa
        for(int i = 0; i< (csvString.length()); i++){
            //ricerca virgole
            if (csvString.charAt(i) == ',') {
                //una volta trovata una virgoletta si crea una sottostringa dalla virgoletta precedente a quella nuova
                outString[columnNum] = csvString.substring(lastComma +1, i);
                outString[columnNum] = outString[columnNum].trim();

                //pulizia dalle eventuali virgolette "", per la sintassi csv
                if (outString[columnNum].startsWith("\"") && outString[columnNum].endsWith("\"")) {
                    outString[columnNum] = outString[columnNum].substring(1, outString[columnNum].length()-1);
                    if (outString[columnNum].contains("\"\"")) {
                        outString[columnNum] = outString[columnNum].replaceAll("\"\"", "\"");
                    }
                }

                //riposizionamento degli indici 
                lastComma = i;
                columnNum++;

                //si ignorano i contenuti delle virgolette, questo per non interferire con eventuali virgole
            } else if ((csvString.charAt(i) == '"')) {
                do {
                    i++;
                }while(csvString.charAt(i) != '"');
            }
        }
        //lettura dell'ultima colonna
        outString[columnNum] = csvString.substring(lastComma+1, csvString.length());
        if (outString[columnNum].startsWith("\"") && outString[columnNum].endsWith("\"")) {
                    outString[columnNum] = outString[columnNum].substring(1, outString[columnNum].length()-1);
                    if (outString[columnNum].contains("\"\"")) {
                        outString[columnNum] = outString[columnNum].replaceAll("\"\"", "\"");
                    }
                }

        return outString;
    }

    //prende un array di stringhe e restituisce una stringa nel formato csv
    private String stringToCSV(String[] inString){
        String outString = "";
        String nuovaStringa = ""; 

        for(int i = 0; i< inString.length; i++){
            nuovaStringa = inString[i].trim();//copio il valore della stringa su una nuova stringa così da non influenzare per riferimento l'array
            //verifica della corretta sintassi per il csv, raddoppio eventuali virgolette o le aggiungo in caso di \n o di virgole
            if(nuovaStringa.contains("\"")){
                nuovaStringa = nuovaStringa.replaceAll("\"", "\"\"");
            }
            if(nuovaStringa.contains("\n") || nuovaStringa.contains(",")){
                nuovaStringa = "\"" + nuovaStringa + "\"";
            }
            
            outString += nuovaStringa + ",";
        }
        //elimino l'ultima virgola visto che creerebbe un'altra colonna
        outString = outString.substring(0,outString.length()-1);

        return outString;

    }

}