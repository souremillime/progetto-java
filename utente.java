
public class Utente{
    protected String username;
    protected static final CsvHandler fileStd = new CsvHandler("users.txt");

    public Utente(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
    

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