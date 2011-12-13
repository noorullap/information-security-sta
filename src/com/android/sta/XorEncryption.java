package com.android.sta;




/**
 * It is simple temporary encryption used in our project for keeping data encrypted
 * It is block cipher, size of each block is defined by length of key
 * E_key(x) = x ^ key, where x - initial message
 * D_key(S) = S ^ key
 * Encryption mode - ECB
 * @author Robert Khasanov
 *
 */
public class XorEncryption {
	
	private static final int MIN_KEY_LENGTH = 9;
	private static final int MAX_KEY_LENGTH = 29;
	private long key = 0; 
	
	public XorEncryption(long key) {
		this.key = key;
	}

	private static int getKeyPeriod( long key){
		int i;
		assert 1 << MIN_KEY_LENGTH <= key;
		for( i = MIN_KEY_LENGTH; 
			 (i <= MAX_KEY_LENGTH) && ( (1 << i) < key);
			 i++ );
		assert 1<<i > key;
		return i;
	}
	
	private byte getbyteFromKeyShifted( long key, int pos){
		int period = getKeyPeriod( key);
		if ( pos > period - 8){
			byte res = (byte) (key << (pos + 8 - period));
			res += (key >> (2*period - pos - 8 ) ) & ( (1 << (pos+8-period))-1);
			return res;
		} else{
			return (byte) (key >> (period - 8 - pos)); 
		}
			
	}
	
	private byte[] expandKey( long key, int size){
		assert size > 0;
		int period = getKeyPeriod( key);
		byte[] keyArray = new byte[size];
		int pos = 0;
		for (int i = 0; i < size; i++){
			keyArray[i] = getbyteFromKeyShifted( key, pos);
			pos = (pos + 8 )%period;
		}
		
		return keyArray;
	}

	public byte[] Encrypt(byte[] str){
		int size = str.length;
		
		byte[] keyArray = expandKey( key, size);
		byte[] resArray = new byte[size];
		for ( int i = 0; i < size; i++ ){
			resArray[i] =  (byte) (keyArray[i] ^ str[i]);
		}
		
		return resArray;
	}

	public byte[] Decrypt( byte[] str){
		return Encrypt( str);
	}
	
	public static byte[] Encrypt( byte[] str, long key){
		XorEncryption m = new XorEncryption( key);
		return m.Encrypt(str);
	}
	
	public static byte[] Decrypt( byte[] str, long key){
		XorEncryption m = new XorEncryption( key);
		return m.Decrypt(str);
	}
	/** debug function */
	private static void printbinary( byte[] str){
		String binary[] = { "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", 
                "1001", "1010", "1011", "1100", "1101", 
                "1110", "1111" };
		for (int i=0;i<str.length;i++){
			System.out.print( binary[(str[i] & ((1<<8) -1))>>> 4]);
			System.out.print( binary[str[i] & ((1<<4) -1)]+" ");
		}
		System.out.println();
	}
	
	/** debug function */
//	public static void main(String args[]){
//		int key = 1234;
//		String mess = new String("I love you!");
//		printbinary(mess.getBytes());
//		byte[] smess = Encrypt(mess.getBytes(),key);
//		printbinary(smess);
//		byte[] cmess = Decrypt( smess,key);
//		printbinary(cmess);
//
//	}
}
