package caesar_сipher;

/**
 * Класс, реализующий шифрование и дешифрование
 * методом Цезаря
 * @author GrayBelt
 */
public class Caesar {
    String alphabet;      // Алфавит, заданный пользователем

    /**
     * Конструктор класса
     * @param Алфавит шифрования
     */
    public Caesar(String _alphabet){
        alphabet=_alphabet;
    }

    /**
     * Метод Цезаря шифрование текста
     * @param Исходный текст
     * @param Количество позиций для сдвига
     * @return Зашифрованный текст
     */
    public String getEncryption(String text, String key_str){
		int position = hash_key( key_str);
		String cipher ="";
        for(int i=0;i<text.length();i++){
            cipher+=alphabet.charAt((alphabet.indexOf(text.charAt(i))+position)%alphabet.length());
        }
        return cipher;
    }

	private int hash_key( String str){
		int res = 0;
		for (int i=0;i<str.length();i++){
			res += alphabet.indexOf(str.charAt(i));
		}
		return res;
	}
	
    /**
     * Метод Цезаря де шифрование текста
     * @param Зашифрованный текст
     * @param Количество позиций для сдвига
     * @return Исходный текст
     */
    public String getDecryption(String cipher, String key_str){
		int position = hash_key( key_str);
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
