import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.util.Scanner;

public class HashingDigest {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		Scanner input = new Scanner(System.in);
		File path = null;
		String f = null;
		Scanner reader = null;
		System.out.println("Type file names to digest or Enter to stop:");
		while(!(f = input.nextLine()).isEmpty()) {
			path = new File(f);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String str = "";
			byte[] digest = null;
			
			try {
				reader = new Scanner(path);
				while(reader.hasNextLine()) {
					str = reader.nextLine();
					md.update(str.getBytes());
				}
				digest = md.digest();
				System.out.println("Length = " + digest.length);
				System.out.println("Digest: " + digest.toString());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("DONE");
		reader.close();
		input.close();
	}
}
//[B@42a57993