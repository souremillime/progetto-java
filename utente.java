
public class Utente{
    protected String username;
    protected static final GestoreCSV fileStd = new GestoreCSV("users.txt"); //file con tutti gli utenti
    protected final GestoreCSV filePrestiti; //file con la lista dei prestiti

    //costruttore
    public Utente(String username){
        this.username = username;
        filePrestiti = new GestoreCSV(username+".csv");
    }
/*get*/
    //restituisce il nome utente
    public String getUsername(){
        return username;
    }

    //restituisce il livello di accesso e il nome utente
    public String stampaStato(){
        return "utente base " + username;
    }
    

/*gestione prestiti*/
    //crea un nuovo prestioto -> aggiunge un oggetto di una certa quantità ne fil le dei prestiti e lo toglie dalla categoria selezionata
    public void nuovoPrestito(Categoria categoria, String prestito, int quantita){
        boolean trovato = false;
        //riduco la quantità dell'oggettto
        categoria.riduciQuantita(prestito, quantita);
        for(int i = 0; i<filePrestiti.getNumeroRighe(); i++){
            if(filePrestiti.getAtMappaFile(i, 1).equals(prestito)){
                //e viene cancellato dal file prestiti
                quantita = quantita + Integer.parseInt(filePrestiti.getAtMappaFile(i,2));
                filePrestiti.riscriviElementoCSV(String.valueOf(quantita), 2, i);
                filePrestiti.salvaModifiche();
                trovato = true;
                break;
            
            }
        }
        if(!trovato){
            //creao il nuovo oggetto
            //con nome della categoria, nome dell'oggetto e quantità presa
            String[] oggettoPreso = new String[]{categoria.getNome(), prestito, String.valueOf(quantita)};
            //aggiungo la riga
            filePrestiti.nuovaRigaCSV(oggettoPreso);
            filePrestiti.salvaModifiche();

        }

    }
    
    public void restituisciPrestito(Categoria categoria, String nome){
        //cerco l'oggetto preso nel file prestiti
        for(int i = 0; i<filePrestiti.getNumeroRighe(); i++){
            if(filePrestiti.getAtMappaFile(i, 1).equals(nome)){
                //se l'oggetto viene trovato viene aumentata la sua quantità nell'a categoria
                categoria.aumentaQuantita(nome, Integer.parseInt(filePrestiti.getAtMappaFile(i, 2)));
                //e viene cancellato dal file prestiti
                filePrestiti.cancellaRigaCSV(i);
                filePrestiti.salvaModifiche();
                break;
            
            }
        }
    }

/*gestione utenti "static"*/
    //verifica l'esistenza dell'utente e restituisce la posizine nel file o -1 se non esiste
    static int esisteUtente(String username){
        //verifica che il file non sia vuoto
        if(!fileStd.getVuoto()){
            for (int i = 0; i<fileStd.getNumeroRighe(); i++) {
                //se trova l'oggetto restituisce la posizione
                if(fileStd.getAtMappaFile(i, 0).equals(username)){
                    return i;
                }
            }
        }
        return -1;
    }
    //restituisce la password ed il livello di accesso 
    static String[] getCredenziali(String username){
        if(!fileStd.getVuoto()){
            for (int i = 0; i< fileStd.getNumeroRighe(); i++) {
                //se trova l'utente ne file dei utenti restituisce la riga
                if(fileStd.getAtMappaFile(i, 0).equals(username)){
                    String[] outString = {fileStd.getAtMappaFile(i, 1),fileStd.getAtMappaFile(i, 2)};
                    return outString;
                }
            }
        }
        //se non trova niente restituisce null
        return null;
    }
    //aggiunge un utente di livello base nel file degli utenti
    static void creaUtente(String username, String password){
        String[] nuovoUtente = new String[3];
        //inizializzo l'utente
        nuovoUtente[0] = username;
        nuovoUtente[1] = Integer.toString(password.hashCode());
        nuovoUtente[2] = "std";
        //lo aggiungo al file
        fileStd.nuovaRigaCSV(nuovoUtente);
        fileStd.salvaModifiche();
    }
    //verifica l'assenza di utenti, ovvero se il file è vuoto
    static boolean zeroUtenti(){
        return fileStd.getVuoto();
    }
}