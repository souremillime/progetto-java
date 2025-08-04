public class MainProgetto{
    public static void main(String[] args){
        CsvHandler bob = new CsvHandler("/Users/EMIL/Library/CloudStorage/OneDrive-UniversitàdegliStudidiTrieste/UNI/programmazione-java/progetto/bobTest.csv");
        String[] list = bob.getFirstRow();
        for (int i = 0; i< list.length ;i++) {
            System.out.println(list[i]);
        }
        bob.scriviLineaCSV(list);
    }
}