import java.io.File;

public class Categoria extends CsvHandler{
    //caratteristiche
    String nomeCategoria;
    static String cartellaStd = "Categorie"; //path relativo della cartella standard 

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

    public String getNome(){
        return nomeCategoria;
    }

    public void cercaPerNome(String nome) {

        System.out.println("\nCategoria: " + nomeCategoria + "\n");
        

        for (int i = 0; i<numRighe; i++) {
            if(mappaFile[i][0].contains(nome)){
                System.out.println("\t" + mappaFile[i][0]);
            }
        }

        
    }

    public void getQuantita(String nome){
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, 1)); 
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

    //riduce la quantità di un oggetto o lo cancella definitivamente 
    public void eliminaOggetto(String nome, int quantita){
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, 1));          
            if(quantita >= quantitaOggetto){
                cancellaRigaCSV(posizioneOggetto);
            }else{

                String[] nuovoOggetto = getAtMappaFile(posizioneOggetto);
                nuovoOggetto[1] = String.valueOf(Integer.parseInt(nuovoOggetto[1]) - quantita);
                riscriviRigaCSV(nuovoOggetto, posizioneOggetto);
            }

        }
        
    }
    //riduce la quantità di un oggetto specifico
    public void riduciQuantita(String nome, int riduci){
        int posizioneOggetto = esisteOggetto(nome);

        if(posizioneOggetto == -1){
            System.out.println("l'oggetto non esiste");

        }else{
            int quantitaOggetto = Integer.parseInt(getAtMappaFile(posizioneOggetto, 1));
            if(riduci <= quantitaOggetto){
                quantitaOggetto = quantitaOggetto - riduci;
                riscriviElementoCSV(String.valueOf(quantitaOggetto), 1, posizioneOggetto);
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

    static boolean esisteCategoria(String nomeCategoria){
        File cartella = new File(cartellaStd);
        if(!cartella.isDirectory()){
            String[] lista = cartella.list();
            for (String file : lista) {
                if( file.contains(nomeCategoria))
                    return true;
            }
            
        }
        return false;

    }

    static String[] getListaCategorie(){
        File cartella = new File(cartellaStd);
        String[] lista = null;
        int riduci = 0;
        if(!cartella.isDirectory()){
            lista = cartella.list();
            for (int i = 0; i<lista.length; i++) {
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

}