import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CsvHandler{
    String csvPath;
    int numCol, numRighe;
    String[][] mappaFile;
    BufferedReader reader;

//inizializazione oggetto

    //costruttore
    public CsvHandler(String csvPath) {
        this.csvPath = csvPath;
        try{
            reader = new BufferedReader(new FileReader(csvPath));
            definisciDimensioni();
            if(this.numCol == 1){
                System.out.println("file inizializzato");//debug
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //definisce il numero di colonne e il numero di righe che ha un file CSV
    private void definisciDimensioni() throws IOException{
        String csvString = reader.readLine();
        int count = 0;
        for (int i = 0; i < csvString.length(); i++) {
            if (csvString.charAt(i) == ',') {
                count++;
            } else if (csvString.charAt(i) == '"') {
                while (csvString.charAt(i++) != '"');
            }
        }
        this.numCol = count+1;
        count = 1;

        while(reader.readLine() != null){
            count++;
        }
        this.numRighe = count;
    //debug
        System.out.println("numCol: " + numCol);
        System.out.println("numLine: " + numRighe);

    }

    //viene letta la prima riga e resettato il reader
    public String[] getFirstLine(){
        return this.mappaFile[0];
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
                System.out.println(outString[columnNum] + i);

                //pulizia dalle eventuali virgolette "", per la sintassi csv
                if (outString[columnNum].contains("\"")) {
                    outString[columnNum].replaceAll("\"", "\0");
                }

                //riposizionamento degli indici 
                lastComma = i;
                columnNum++;

                //si ignorano i contenuti delle virgolette, questo per non interferire con eventuali virgole
            } else if (csvString.charAt(i) == '"') {
                System.out.println("trovato \"");
                while (csvString.charAt(i++) != '"');
            }
        }
        //lettura dell'ultima colonna
        outString[columnNum] = csvString.substring(lastComma+1, csvString.length());
        if (outString[columnNum].contains("\"")) {
            outString[columnNum].replaceAll("\"", "\0");
        }

        return outString;
    }

    //legge in continuazione dalla riga precedentemente letta
    public String[] letturaConsecutiva() throws IOException{
        return csvToString(reader.readLine());
    }

    void leggiFile() throws IOException{
        definisciDimensioni();
        String[][] outString = new String[this.numRighe][this.numCol];
        this.reader.reset();
        for(int i = 0; i < this.numRighe; i++){
            outString[i] = csvToString(this.reader.readLine());
        }
        this.mappaFile = outString;
    }

    /*scrittura*/

    //scrittura di una riga CSV
    void nuovaRigaCSV(String riga[]){
        if (riga.length > numCol){
            System.err.println("oggetto troppo lungo");//debug
        }else{
            try(FileWriter writer = new FileWriter(csvPath, true)){
                writer.write("\n");
                for(int i = 0; i<riga.length-1;i++ ){
                    writer.write(riga[i] + ",");
                }
                writer.write(riga[numCol-1]);
                
                leggiFile(); //aggiorno la mappa del file

            }catch(IOException e){
                e.printStackTrace();
            }
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

        for(int i = 0; i< inString.length-1; i++){
            inString[i] = inString[i].trim();

            if(inString[i].contains("\"")){
                inString[i] = inString[i].replaceAll("\"", "\"\"");
            }
            if(inString[i].contains("\n")){
                inString[i] = "\"" + inString[i] + "\"";
            }
            
            outString += inString[i] + ",";
        }
        outString += inString[numCol-1] + "\n";

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

            for(int i = 0; i<numRighe;i++ ){
                writer.write(stringToCSV(mappaFile[i]));
            }
            writer.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}