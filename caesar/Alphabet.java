package caesar_сipher;

/**
 * Класс, предоставляющий методы
 * для получения различных алфавитов
 * @author GrayBelt
 */
public class Alphabet {

    static String alphabet;
    /**
     * Метод, возвращающий алфавит заглавных
     * английских букв
     * @return Алфавит заглавных английских букв
     */
    public static String getEnglishU(){
        alphabet = "";
        for(char ch='A';ch<='Z';ch++){
            alphabet+=ch;
        }
        return alphabet;
    }

    /**
     * Метод, возвращающий алфавит строчных
     * английских букв
     * @return Алфавит строчных английских букв
     */
    public static String getEnglishL(){
        alphabet = "";
        for(char ch='a';ch<='z';ch++){
            alphabet+=ch;
        }
        return alphabet;
    }

    /**
     * Метод, возвращающий строку цифр
     * @return строку цифр
     */
    public static String getNumbers(){
        alphabet = "";
        for(char ch='0';ch<='9';ch++){
            alphabet+=ch;
        }
        return alphabet;
    }

    /**
     * Метод, возвращающий скобки и знаки препинания
     * @return строка скобок и знаков препинания
     */
    public static String getSpecialCharacters(){
        alphabet = "";
        for(char ch=' ';ch<='/';ch++){
            alphabet+=ch;
        }
        for(char ch=':';ch<='?';ch++){
            alphabet+=ch;
        }
        return alphabet;
    }

    /**
     * Метод, возвращает набор всех возможных символов
     * @return строка всех символов
     */
    public static String getEverything(){
        alphabet="";
        alphabet+=Alphabet.getEnglishL();
        alphabet+=Alphabet.getEnglishU();
        alphabet+=Alphabet.getNumbers();
        alphabet+=Alphabet.getSpecialCharacters();
        return alphabet;
    }
}
