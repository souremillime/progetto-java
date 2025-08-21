
public class Utente{
    protected String username;
    protected static final CsvHandler fileStd = new CsvHandler("users.txt");
    protected final CsvHandler filePrestiti;

    public Utente(String username){
        this.username = username;
        filePrestiti = new CsvHandler(username+".csv");
    }
/*get*/
    public String getUsername(){
        return username;
    }
    

/*gestione prestiti*/

    public void nuovoPrestito(Categoria categoria, String prestito, int quantita){
        categoria.eliminaOggetto(prestito, quantita);
        String[] oggettoPreso = new String[]{categoria.getNome(), prestito, String.valueOf(quantita)};
        filePrestiti.nuovaRigaCSV(oggettoPreso);
        filePrestiti.salvaModifiche();

    }

    public void restituisciPrestito(Categoria categoria, String nome){
        
        for(int i = 0; i<filePrestiti.getNumeroRighe(); i++){
            if(filePrestiti.getAtMappaFile(i, 1).equals(nome)){
                categoria.aumentaQuantita(nome, Integer.parseInt(filePrestiti.getAtMappaFile(i, 2)));
                filePrestiti.cancellaRigaCSV(i);
                filePrestiti.salvaModifiche();
                break;
            
            }
        }
    }


/*gestione utenti "dinamic"*/

    void cancelaUtente(){

    }

    void creaUtente(){

    }


/*gestione utenti "static"*/
    static boolean esisteUtente(String username){
        System.out.println(fileStd.getVuoto());
        if(!fileStd.getVuoto()){
            for (int i = 0; i<= fileStd.getNumeroRighe(); i++) {
                if(fileStd.getAtMappaFile(i, 0).equals(username)){
                    return true;
                }
            }
        }
        return false;
    }

    static String[] getCredenziali(String username){
        if(!fileStd.getVuoto()){
            for (int i = 0; i<= fileStd.getNumeroRighe(); i++) {
                if(fileStd.getAtMappaFile(i, 0).equals(username)){
                    String[] outString = {fileStd.getAtMappaFile(i, 1),fileStd.getAtMappaFile(i, 2)};
                    return outString;
                }
            }
        }
        return null;
    }

    static boolean creaUtente(String username, String password){
        String[] nuovoUtente = new String[3];
        nuovoUtente[0] = username;
        nuovoUtente[1] = Integer.toString(password.hashCode());
        nuovoUtente[2] = "std";
        fileStd.nuovaRigaCSV(nuovoUtente);
        fileStd.salvaModifiche();
        return esisteUtente(username);
    }

    static boolean zeroUtenti(){
        return fileStd.getVuoto();
    }
}