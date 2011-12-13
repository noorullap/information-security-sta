package caesar_сipher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Данный проект описывает работу с шифром Цезаря
 * @author MaxsBelt
 */
public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException {

        String alphabet = "";                   // Определить алфавит
        alphabet+=Alphabet.getEverything();     // Задать алфавит из всех используемых символов

        String charsetName = "Cp1251";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in,charsetName));
        try{
            System.out.println("Введите текст послания:");
            String text = in.readLine();
            System.out.println("Введите величину сдвига:");
            int K = Integer.parseInt(in.readLine());

            //Создать объект класса шифр Цезаря над данным алфавитом
            Caesar caesar = new Caesar(alphabet);

            // Получить шифр Цезаря для заданной строки cipher со сдвигом 10
            String cipher = caesar.getEncryption(text, K);

            // Распечатать полученный шифр полученный шифр
            System.out.println(cipher);

            // Распечатать дешифровку для cipher со сдвигом 10
            System.out.println(caesar.getDecryption(cipher, K));

        }catch(Exception ex){
            System.out.println(ex);}
    }

}
