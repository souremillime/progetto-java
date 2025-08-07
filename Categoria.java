import java.io.File;

public class Categoria extends CsvHandler{
    //caratteristiche
    String nomeCategoria;
    static String cartellaStd = "Categorie"; //path relativo della cartella standard 
    //dettagli

/*costruttore*/

    public Categoria(String pathCategoria) {
        super(pathCategoria);
        File cartella = new File(cartellaStd);
        if(!cartella.isDirectory()){
            cartella.mkdir(); //restituisce un valore booleano!!!
            cartellaStd = cartella.getAbsolutePath();
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
        if(b>= numRighe){
            b = numRighe-1;
        }
        System.out.println("\n Categoria: " + this.nomeCategoria + "\n");
        for(int i = a; i<=b; i++){
            for(int j = 0; j<numCol; j++){
                System.out.printf("|%10s",mappaFile[i][j]);
            }
            System.out.println("|");
        }

    }



/*set*/

    // il nome della categoria viene definito
    public void setNomeCategoria(String nomeCategoria){
        this.nomeCategoria = nomeCategoria;
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