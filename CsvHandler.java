import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CsvHandler{
    String csvPath;
    int numCol, numRighe;
    String[][] mappaFile;
    BufferedReader reader;

//inizializzazione oggetto

    //costruttore
    public CsvHandler(String csvPath) {
        this.csvPath = csvPath;
        try{
            reader = new BufferedReader(new FileReader(csvPath));
            leggiFile();

            if(this.numCol == 1){
                System.out.println("file inizializzato");//debug
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //definisce il numero di colonne e il numero di righe che ha un file CSV. Il reader viene anche resettato
    private void definisciDimensioni() throws IOException{
        //resetto il reder
        this.reader.close();
        this.reader = new BufferedReader(new FileReader(csvPath));
        //leggo la prima riga del file
        String csvString = reader.readLine();
        //verifico che non sia vuoto
        if(csvString == null){
            System.err.println("file vuoto");//debug
            this.numCol = 0;
            this.numRighe = 0;
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
        this.numCol = count+1;
        count = 0; //resetto il contatore per le righe
        int numVirgolette = 0; //avvio il contatore per le virgolette

        //leggo il file fino a che non finisce
        while(csvString != null){
            //verifico la presenza di virolette
            if(csvString.contains("\"")){
                System.out.println("trovata \"");//debug
                int i = 0;
                //conto il numero di virgolette presenti nella stringa letta
                while(i < csvString.length()){
                    if(csvString.charAt(i) == '"'){
                        numVirgolette++;
                    }
                    i++;

                }
            }
            //se le virgolette sono dispari, vuol dire probabilmente che la seguente si trova nella riga seguente
            if(numVirgolette%2 == 1){

                System.out.println("continue: " + numVirgolette + csvString);//debug
                csvString = reader.readLine();
                continue;// ripeto il ciclo 
            }else{//se non ci sono virgolette o le virgolette sono pari, vul dire che la riga è finita

                count++;
                numVirgolette = 0;
                System.out.println("++");//debug
            }
            csvString = reader.readLine(); // inizio la lettura della riga segunete
            System.out.println("stringa: " + csvString);

        }

        this.numRighe = count; // salvo il numero di righe
        }
        System.out.println("righe: " + numRighe + "\n colonne: " + numCol); //debug
        this.reader.close();//chiusura del file
        this.reader = new BufferedReader(new FileReader(csvPath));//riavvio reader

    }

    //viene letta la prima riga della mappa del file
    public String[] getFirstLine(){
        String[] firsLine = new String[numCol];
        for(int i = 0; i<numCol;i++){
            firsLine[i] = this.mappaFile[0][i];
        }
        return firsLine;
    }

/*lettura*/

    //conversione da codifica CSV a un array di stringe contenenti le informazioni delle singole colonne
    public String[] csvToString(String csvString){
        int columnNum = 0, lastComma = -1; //inizia da -1 visto che nel substring viene sommato 1, questo per fare laggere il primo elemento dalla riga 0
        String outString[] = new String[numCol];
        
        for(int i = 0; i< (csvString.length()); i++){
            //ricerca virgole
            if (csvString.charAt(i) == ',') {
                System.out.println("trovato ,");//debug

                outString[columnNum] = csvString.substring(lastComma +1, i);
                outString[columnNum] = outString[columnNum].trim();
                System.out.println(outString[columnNum] + i);//debug

                //pulizia dalle eventuali virgolette "", per la sintassi csv
                if (outString[columnNum].startsWith("\"") && outString[columnNum].endsWith("\"")) {
                    outString[columnNum] = outString[columnNum].substring(1, outString[columnNum].length()-1);
                    if (outString[columnNum].contains("\"\"")) {
                        outString[columnNum] = outString[columnNum].replaceAll("\"\"", "\"");
                    }
                    System.out.println("virgolette: " + outString[columnNum]);
                }

                //riposizionamento degli indici 
                lastComma = i;
                columnNum++;

                //si ignorano i contenuti delle virgolette, questo per non interferire con eventuali virgole
            } else if ((csvString.charAt(i) == '"')) {
                System.out.println("trovato \"");
                do {
                    i++;
                    System.out.println("index: " + i);
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
                    System.out.println("virgolette: " + outString[columnNum]);
                }

        return outString;
    }

    //legge in continuazione dalla riga precedentemente letta. <- fine riga non gestito!!!
    public String[] letturaConsecutiva() throws IOException{
        return csvToString(reader.readLine());
    }

    //legge l'intero file e lo salva in memoria. Il reader viene resettato
    public void leggiFile() throws IOException{
        String rigaLetta = "";
        definisciDimensioni();
        String[][] outString = new String[this.numRighe][this.numCol];
        int i = 0;                     
        int numVirgolette = 0;
        while(i < this.numRighe){
            rigaLetta += this.reader.readLine();
            System.out.println("riga letta2: "+ rigaLetta);//debug
            numVirgolette = 0;
            if(rigaLetta.contains("\"")){

                for(int j = 0; j< rigaLetta.length(); j++){
                    if(rigaLetta.charAt(j) == '"'){
                        numVirgolette++;
                        System.out.println("virgolette: " + numVirgolette);
                    }

                }
            }
                if(numVirgolette%2 == 1){     
                    rigaLetta += "\n";
                    System.out.println("continue: " + numVirgolette);//debug
                    continue;
                }else{
                    outString[i] = csvToString(rigaLetta);
                    System.out.println("nuova Riga");
                    rigaLetta = "";
                    i++;
                }
                
        }
        reader.close();
        reader = new BufferedReader(new FileReader(csvPath));
        this.mappaFile = outString;
    }

/*scrittura*/

    //scrittura di una riga CSV
    void nuovaRigaCSV(String riga[]){
        if (riga.length > numCol){
            System.err.println("oggetto troppo lungo");//debug
        }else{
            numRighe++;
            String[][] nuovaMappa = new String[numRighe][numCol];

            for (int i = 0; i < numRighe - 1; i++) {
                nuovaMappa[i] = mappaFile[i];
            }

            nuovaMappa[numRighe-1] = riga;
            mappaFile = nuovaMappa;
            System.gc();
        }
    }

    void riscriviRigaCSV(String riga[], int posi){
        if (riga.length > numCol){
            System.err.println("oggetto troppo lungo");//debug
        }else if(posi > numRighe){
            System.err.println("coordinate sbagliate");//debug
        }else{
            this.mappaFile[posi] = riga;
        
        }

    }

    String stringToCSV(String[] inString){
        String outString = "";

        for(int i = 0; i< inString.length; i++){
            inString[i] = inString[i].trim();
                System.out.println("begin " + inString[i]);//debug
            //verifica della corretta sintassi per il csv, raddoppio eventuali virgolette o le aggiungo in caso di \n o di virgole
            if(inString[i].contains("\"")){
                inString[i] = inString[i].replaceAll("\"", "\"\"");
                System.out.println("replaced \"\": " + i);//debug
            }
            if(inString[i].contains("\n") || inString[i].contains(",")){
                System.out.println("fine a: " + inString[i]); //debug
                inString[i] = "\"" + inString[i] + "\"";


                System.out.println("fine b: " + inString[i] + i); //debug
            }
            
            outString += inString[i] + ",";
            System.out.println("out: " + outString);
        }
        outString = outString.substring(0,outString.length()-1);

        return outString;

    }

    public void salvaModifiche(){
        try{
            //cancello dati nel file
            FileWriter writer = new FileWriter(csvPath);//modalità sovrascrittura
            writer.write("");
            writer.close();
            
            //inizio scrittura
            writer = new FileWriter(csvPath, true);//modalità sovrascrittura
                
            writer.write(stringToCSV(mappaFile[0]));//inizia senza capoverso

                System.out.println("mappa 1: "+ mappaFile[0][0]);

            for(int i = 1; i<numRighe;i++ ){
                System.out.println("mappa: "+ mappaFile[i][0]);
                writer.write("\n" + stringToCSV(mappaFile[i]));//con capoverso
            }
            writer.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}