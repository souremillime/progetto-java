import java.io.File;

public class Categoria extends CsvHandler{
    //caratteristiche
    String nomeCategoria;
    static String cartellaStd = "Categorie"; //path relativo della cartella standard 
    //dettagli

/*costruttore*/

    public Categoria(String pathCategoria) {
        super(cartellaStd + "/" + pathCategoria + ".csv");
        File cartella = new File(cartellaStd);
        if(!cartella.isDirectory()){
            cartella.mkdir(); //restituisce un valore booleano!!!
        }
        this.nomeCategoria = new File(pathCategoria).getName().replace(".csv", "");

    }

/*get o lettura*/

    public void cercaPerNome(String nome) {

        System.out.println("\nCategoria: " + nomeCategoria + "\n");
        

        for (int i = 0; i<numRighe; i++) {
            if(mappaFile[i][0].contains(nome)){
                System.out.println("\t" + mappaFile[i][0]);
            }
        }

        
    }

    public void print(int a, int b){
        if(b>= getNumeroRighe()){
            b = getNumeroRighe()-1;
        }
        System.out.println("\n Categoria: " + this.nomeCategoria + "\n");
        for(int i = a; i<=b; i++){
            for(int j = 0; j<numCol; j++){
                System.out.printf("|%10s", getAtMappaFile(i,j));
            }
            System.out.println("|");
        }

    }

    //verifica l'esistenza di un oggetto e ne restituisce la riga nella mappa, altrimenti restituisce -1
    public int esisteOggetto(String nomeOggetto){
        for (int i = 0; i<getNumeroRighe(); i++) {
            if(getAtMappaFile(0,i).equals(nomeOggetto)){
                return i;
            }
        }
        return -1;
    }



/*set*/

    // il nome della categoria viene definito
    public void setNomeCategoria(String nomeCategoria){
        this.nomeCategoria = nomeCategoria;
    }

    //aggiunge un oggetto alla categoria o se già presente ne incrementa la quantità
    public void aggiungiOggetto(String[] oggetto){
        int posizioneOggetto = esisteOggetto(oggetto[0]);

        if(posizioneOggetto == -1){
            nuovaRigaCSV(oggetto);

        }else{
            System.out.println("l'oggetto è stato aggiunto a un oggetto già esistente");
            String[] nuovoOggetto = getAtMappaFile(posizioneOggetto);
            nuovoOggetto[1] = String.valueOf(Integer.parseInt(nuovoOggetto[1]) + Integer.parseInt(oggetto[1]));
            riscriviRigaCSV(nuovoOggetto, posizioneOggetto);

        }
        
    }

    //riduce la quantità di uno e scrive l'operazione nelle log
    public void eliminaOggetto(String nome, int quantita){
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, numCol));          
            if(quantita >= quantitaOggetto){
                cancellaRigaCSV(posizioneOggetto);
            }else{

                String[] nuovoOggetto = getAtMappaFile(posizioneOggetto);
                nuovoOggetto[1] = String.valueOf(Integer.parseInt(nuovoOggetto[1]) - quantita);
                riscriviRigaCSV(nuovoOggetto, posizioneOggetto);
            }

        }
        
    }

/*static*/

    static boolean getPresenzaCategoria(){
        File cartella = new File(cartellaStd);
        if(!cartella.isDirectory()){
            String[] lista = cartella.list();
            for (String file : lista) {
                if( file.endsWith(".csv"))
                    return true;
            }
            
        }
        return false;

    }

}