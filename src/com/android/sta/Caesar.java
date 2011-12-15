package com.android.sta;

/**
 * Класс, реализующий шифрование и дешифрование
 * методом Цезаря
 * @author GrayBelt
 */
public class Caesar {
    private static String alphabet = "";      // Алфавит, заданный пользователем
    
    static {
    	alphabet+=Alphabet.getEverything();
    }

    /**
     * Конструктор класса
     * @param Алфавит шифрования
     */
    private Caesar(){
    	
    }

    /**
     * Метод Цезаря шифрование текста
     * @param Исходный текст
     * @param Количество позиций для сдвига
     * @return Зашифрованный текст
     */
    public static String getEncryption(String text, String key_str){
		int position = hash_key( key_str);
		String cipher ="";
        for(int i=0;i<text.length();i++){
            cipher+=alphabet.charAt((alphabet.indexOf(text.charAt(i))+position)%alphabet.length());
        }
        return cipher;
    }
    
    //Android
	private static int hash_key( String str){
		int res = 0;
		for (int i=0;i<str.length();i++){
			res = (res + alphabet.indexOf(str.charAt(i)))%alphabet.length();
		}
		return res;
	}
	
    /**
     * Метод Цезаря де шифрование текста
     * @param Зашифрованный текст
     * @param Количество позиций для сдвига
     * @return Исходный текст
     */
    public static String getDecryption(String cipher, String key_str){
		int position = hash_key( key_str);
		//System.out.println(position);
        String text ="";
        for(int i=0;i<cipher.length();i++){
            if(alphabet.indexOf(cipher.charAt(i))-position<0){
                text+=alphabet.charAt(alphabet.length() - 1 +
                        (alphabet.indexOf(cipher.charAt(i))-position + 1)%alphabet.length());
            }else{
                text+=alphabet.charAt((alphabet.indexOf(cipher.charAt(i))-position)%alphabet.length());
            }
        }
        return text;
    }
}
