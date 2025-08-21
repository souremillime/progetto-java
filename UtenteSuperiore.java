import java.io.File;

public class UtenteSuperiore extends Utente{

    public UtenteSuperiore(String username) {
        super(username);
    }

    @Override
    public String toString(){
        return "utente superiore " + username;
    }


    static void creaUtente(String username, String password){
        if(esisteUtente(username) == -1){
            String[] nuovoUtente = new String[3];
            nuovoUtente[0] = username;
            nuovoUtente[1] = Integer.toString(password.hashCode());
            nuovoUtente[2] = "sup";
            fileStd.nuovaRigaCSV(nuovoUtente);
            fileStd.salvaModifiche();
        }else{
            System.out.println("L'utente esiste già");
        }
    }

    static void cancellaUtente(String utente){
        int posizione = esisteUtente(utente);
        if(posizione > -1){
            fileStd.cancellaRigaCSV(posizione);
            fileStd.salvaModifiche();
            File cancellaFile = new File(utente + ".csv");
            if(cancellaFile.isFile()){
                cancellaFile.delete();
            }
        }else{
            System.out.println("Nessun utente trovato");
        }
    }


}