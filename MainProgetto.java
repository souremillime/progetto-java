public class MainProgetto{
     public static void main(String[] args){
        while(true){
            Funzioni.printStato();
            switch(Funzioni.printMenu()){
                //cerca un oggetto per nome
                case 0:
                    Funzioni.cercaPerNome();
                    break;

                //cambia utente o esegui il login
                case 1:
                    Funzioni.logIn();
                    break;

                //seleziona una categoria
                case 2:
                    Funzioni.selezionaCategoria();
                    break;

                //prend in prestito un oggetto
                case 3:
                    Funzioni.prestitoOggetto();
                    break;

                //restituisci un oggetto
                case 4:
                    Funzioni.restituisciOggetto();
                    break;

                //crea un oggetto
                case 5:
                    Funzioni.creaOggetto();
                    break;

                //elimina oggetto
                case 6:
                    Funzioni.eliminaOggetto();
                    break;

                //crea categoria
                case 7:
                    Funzioni.creaCategoria();
                    break;
                    
                //elimina categoria
                case 8:
                    Funzioni.eliminaCategoria();
                    break;

                //crea nuovo utente
                case 9: 
                    Funzioni.creaUtente();
                    break;
                    
                //cancella utente
                case 10:
                    Funzioni.eliminaUtente();
                    break;
                
                //esci dal programma
                case -1:
                    System.out.println("Esco dal programma...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("L'operoazione selezionata non esiste");

            }
        }
    }
}