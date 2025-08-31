import java.io.File;

public class UtenteSuperiore extends Utente{

    public UtenteSuperiore(String username) {
        super(username);
    }

    //restituisce il livello di accesso e il nome utente
    @Override
    public String stampaStato(){
        return "utente superiore " + username;
    }

    //aggiunge un utente di livello superiore nel file degli utenti
    static void creaUtente(String username, String password){
            String[] nuovoUtente = new String[3];
            //inizializzo l'utente
            nuovoUtente[0] = username;
            nuovoUtente[1] = Integer.toString(password.hashCode());
            nuovoUtente[2] = "sup";
            //lo aggiungo al file
            fileStd.nuovaRigaCSV(nuovoUtente);
            fileStd.salvaModifiche();
    }
    //cancella l'utente selezionato
    static void cancellaUtente(String utente){
        //verrfica l'esistenza
        int posizione = esisteUtente(utente);
        //se esisiste lo cancella
        fileStd.cancellaRigaCSV(posizione);
        fileStd.salvaModifiche();
        //cancella anche il file dei prestiti
        File cancellaFile = new File("Utenti/"+utente + ".csv");
        if(cancellaFile.isFile()){
            cancellaFile.delete();
        }
    }


}