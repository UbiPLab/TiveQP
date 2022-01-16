package AdvanceTest;

import static MyUtil.HashFounction.CreateSecretKey;

public class CreateKey {
    public static void main(String[] args) {
        System.out.print("KeyList[]:  ");
        String[] Keylist = CreateSecretKey(6);
        MyUtil.Show.showString_list(Keylist);

        //  KeyList[] = { 2938879577741549,8729598049525437,8418086888563864,0128636306393258,2942091695121238,6518873307787549}
        System.out.print("Random Number:  ");
        int randNumber = 235648;
        System.out.println(randNumber);
    }
}
