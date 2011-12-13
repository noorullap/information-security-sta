#include <assert.h>
#include <QVector>
#include <QString>

/**
 * It is simple temporary encryption used in our project for keeping data encrypted
 * It is block cipher, size of each block is defined by length of key
 * E_key(x) = x ^ key, where x - initial message
 * D_key(S) = S ^ key
 * Encryption mode - ECB
 * @author Robert Khasanov
 *
 */
class XorEncryption {
	private:
	static const int MIN_KEY_LENGTH = 9;
	static const int MAX_KEY_LENGTH = 29;
	long int key = 0;
	
	public:
	XorEncryption(int key) {
		this.key = key;
	}

	private:
	static int getKeyPeriod( long int key){
		int i;
		assert (1 << MIN_KEY_LENGTH <= key);
		for( i = MIN_KEY_LENGTH; 
			 (i <= MAX_KEY_LENGTH) && ( (1 << i) < key);
			 i++ );
		assert (1<<i > key);
		return i;
	}
	
	private:
	char getbyteFromKeyShifted( long int key, int pos){
		int period = getKeyPeriod( key);
		if ( pos > period - 8){
			char res = (char) (key << (pos + 8 - period));
			res += (key >> (2*period - pos - 8 ) ) & ( (1 << (pos+8-period))-1);
			return res;
		} else{
			return (char) (key >> (period - 8 - pos)); 
		}
			
	}
	
	private:
	QVector<char> expandKey( long int key, int size){
		assert(size > 0);
		int period = getKeyPeriod( key);
		QVector<char> keyArray = new QVector(size);
		int pos = 0;
		for (int i = 0; i < size; i++){
			keyArray[i] = getbyteFromKeyShifted( key, pos);
			pos = (pos + 8 )%period;
		}
		
		return keyArray;
	}

	public:
	QVector<char> Encrypt(QVector<char> str){
		int size = str.size();
		
		QVector<char> keyArray = expandKey( key, size);
		QVector<char> resArray = new QVector(size);
		for ( int i = 0; i < size; i++ ){
			resArray[i] =  (char) (keyArray[i] ^ str[i]);
		}
		
		return resArray;
	}

	public:
	QVector<char> Decrypt( QVector<char> str){
		return Encrypt( str);
	}
	
	public:
	static QVector<char> Encrypt( QVector<char> str, long int key){
		XorEncryption m = new XorEncryption( key);
		return m.Encrypt(str);
	}
	
	public:
	static QVector<char> Decrypt( QVector<char> str, long int key){
		XorEncryption m = new XorEncryption( key);
		return m.Decrypt(str);
	}
}

	/** debug function */
	void printbinary( QVector<char> str){
		QString binary[] = { "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", 
                "1001", "1010", "1011", "1100", "1101", 
                "1110", "1111" };
		for (int i=0;i<str.length;i++){
			cout << ( binary[(str[i] & ((1<<8) -1))>>> 4]);
			cout << ( binary[str[i] & ((1<<4) -1)]+" ");
		}
		cout << endl;
	}
	
	/** debug function */
	void main(){
		long int key = 1234;
		QString mess = new QString("I love you!");
		printbinary(mess.getBytes());
		QVector<char> smess = Encrypt(mess.getBytes(),key);
		printbinary(smess);
		QVector<char> cmess = Decrypt( smess,key);
		printbinary(cmess);
	}
