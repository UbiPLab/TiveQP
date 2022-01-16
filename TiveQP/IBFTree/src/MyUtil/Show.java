package MyUtil;

public class Show {

    public static void showString_list(String[] str_list){
        for (int i = 0; i < str_list.length; i++) {
            if (i == str_list.length - 1){
                System.out.println(str_list[i]);
            }else {
                System.out.print(str_list[i] + ",");
            }
        }
    }



    public static void ShowMatrix(String[][] mat){
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                if (j == mat[i].length-1) {
                    System.out.println(mat[i][j]);
                }else {
                    System.out.print(mat[i][j]+",");
                }
            }
        }
    }
}
