import java.util.Comparator;

public class main {
    public static void main(String[] args) {
        ObligSBinTre<Character> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        char[] verdier = "IATBHJCRSOFELKGDMPQN".toCharArray();
        for(char c : verdier) tre.leggInn(c);
        System.out.println(tre.høyreGren() + ""+ tre.lengstGren());
        String[] s = tre.grener();for(String gren : s) System.out.println(gren);
        System.out.println(tre.bladnodeverdier());
        System.out.println(tre.postString());
        for(Character c : tre) System.out.print(c + "");
        while(!tre.tom()){System.out.println(tre);tre.fjernHvis(x -> true);}

    }
}
