public class Main {
    public static void main(String[] args) {
        String[] a = new String[3];
        a[0] = "a";
        a[1] = "b";
        a[2] = "c";
        StringBuilder stringBuilder = new StringBuilder();
        for(String string : a){
            stringBuilder.append(string);
            if(!a[a.length-1].equalsIgnoreCase(string)){
                stringBuilder.append(" ");
            }
        }
        System.out.println(stringBuilder);
    }
}
