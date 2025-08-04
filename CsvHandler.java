import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CsvHandler{
    String csvPath;
    int numCol;

    //costruttore
    public CsvHandler(String csvPath) {
        this.csvPath = csvPath;
        try(BufferedReader reader = new BufferedReader(new FileReader(csvPath))){
            definisciColonne(reader.readLine());
            if(this.numCol == 1){
                System.out.println("file inizializzato");//debug
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //viene letta la prima riga
    public String[] getFirstRow(){
        String[] outStrings = new String[numCol-1];
        try(BufferedReader reader = new BufferedReader(new FileReader(csvPath))){
            outStrings = csvToString(reader.readLine());

        }catch(IOException e){
            e.printStackTrace();
        }
        return outStrings;
    }

//metodi privati

    /*lettura*/
    //conversione da codifica CSV a un array di stringe contenenti le informazioni delle singole colonne
    private String[] csvToString(String csvString){
        int columnNum = 0, lastComma = -1; //inizia da -1 visto che nel substring viene sommato 1, questo per fare laggere il brimo elemento dalla riga 0
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

    //definisce il numero di colonne che ha un file CSV
    private void definisciColonne(String csvString){
        int count = 0;
        for (int i = 0; i < csvString.length(); i++) {
            if (csvString.charAt(i) == ',') {
                count++;
            } else if (csvString.charAt(i) == '"') {
                while (csvString.charAt(i++) != '"');
            }
        }
        this.numCol = count+1;

        System.out.println("numCol: " + numCol);

    }

    /*scrittura*/

    //scrittura di una riga CSV
    void scriviLineaCSV(String linea[]){
        if (linea.length > numCol){
            System.err.println("oggetto troppo lungo");//debug
        }else{
            try(FileWriter writer = new FileWriter(csvPath)){
                for(int i = 0; i<linea.length;i++ ){
                    writer.write(linea[i] + ",");
                }
                writer.write(linea[numCol-1]);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }


}