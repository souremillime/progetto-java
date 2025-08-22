
public class Utente{
    protected String username;
    protected static final GestoreCSV fileStd = new GestoreCSV("users.txt");
    protected final GestoreCSV filePrestiti;

    public Utente(String username){
        this.username = username;
        filePrestiti = new GestoreCSV(username+".csv");
    }
/*get*/
    public String getUsername(){
        return username;
    }

    
    public String stampaStato(){
        return "utente base " + username;
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


/*gestione utenti "static"*/
    static int esisteUtente(String username){
        System.out.println(fileStd.getVuoto());
        if(!fileStd.getVuoto()){
            for (int i = 0; i<fileStd.getNumeroRighe(); i++) {
                if(fileStd.getAtMappaFile(i, 0).equals(username)){
                    return i;
                }
            }
        }
        return -1;
    }
    //restituisce la password ed il grado 
    static String[] getCredenziali(String username){
        if(!fileStd.getVuoto()){
            for (int i = 0; i< fileStd.getNumeroRighe(); i++) {
                if(fileStd.getAtMappaFile(i, 0).equals(username)){
                    String[] outString = {fileStd.getAtMappaFile(i, 1),fileStd.getAtMappaFile(i, 2)};
                    return outString;
                }
            }
        }
        return null;
    }

    static void creaUtente(String username, String password){
        String[] nuovoUtente = new String[3];
        nuovoUtente[0] = username;
        nuovoUtente[1] = Integer.toString(password.hashCode());
        nuovoUtente[2] = "std";
        fileStd.nuovaRigaCSV(nuovoUtente);
        fileStd.salvaModifiche();
    }

    static boolean zeroUtenti(){
        return fileStd.getVuoto();
    }
}