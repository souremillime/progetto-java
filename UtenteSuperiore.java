public class UtenteSuperiore extends Utente{
    
    public static enum MENU {STANDARD};

    public UtenteSuperiore(String username) {
        super(username);
    }


    static boolean creaUtente(String username, String password){
        String[] nuovoUtente = new String[3];
        nuovoUtente[0] = username;
        nuovoUtente[1] = Integer.toString(password.hashCode());
        nuovoUtente[2] = "sup";
        fileStd.nuovaRigaCSV(nuovoUtente);
        fileStd.salvaModifiche();
        return esisteUtente(username);
    }

    


}