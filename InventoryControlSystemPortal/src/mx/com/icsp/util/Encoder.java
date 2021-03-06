package mx.com.icsp.util;

import java.util.Calendar;

import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

/**
 * 
 * @author HROJASAL
 */
public class Encoder {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		test();
		// token();
	}

	private static void token() {
		ConfigurablePasswordEncryptor passwordEncryptor4 = new ConfigurablePasswordEncryptor();
		passwordEncryptor4.setAlgorithm("MD5");
		passwordEncryptor4.setPlainDigest(true);

		String staticChain = "ll4v3 3st4t1c4:";
		String dinamicChain = "5555091305";
		String token = "5555091305";

		long time = Calendar.getInstance().getTimeInMillis();

		PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		String encryptedTokenAcegi = passwordEncoder.encodePassword(token + "@"
				+ time, staticChain + dinamicChain);

		System.out.println("time: " + time);
		System.out.println("encryptedPasswordAcegi: " + encryptedTokenAcegi);
		System.out.println("isPasswordValid: "
				+ passwordEncoder.isPasswordValid(encryptedTokenAcegi, token
						+ "@" + time, staticChain + dinamicChain));
	}

	/**
	 * 
	 */
	/**
	 * 
	 */
	private static void test() {
		ConfigurablePasswordEncryptor passwordEncryptor4 = new ConfigurablePasswordEncryptor();
		passwordEncryptor4.setAlgorithm("MD5");
		passwordEncryptor4.setPlainDigest(true);

		String user = "mercado";
		String password = "mercado";

		// Jasypt
		String encryptedPasswordJasypt = passwordEncryptor4
				.encryptPassword(password);
		System.out.println("encryptedPasswordJasypt: "
				+ encryptedPasswordJasypt);
		System.out.println("checkPassword: "
				+ passwordEncryptor4.checkPassword(password,
						encryptedPasswordJasypt));

		// Acegi
		PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		String encryptedPasswordAcegi = passwordEncoder.encodePassword(
				password, user);

		System.out.println("encryptedPasswordAcegi: " + encryptedPasswordAcegi);
		System.out.println("isPasswordValid: "
				+ passwordEncoder.isPasswordValid(encryptedPasswordAcegi,
						password, user));
	}

	public static String encryptedPasswordAcegi(String password, String user) {
		PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		return passwordEncoder.encodePassword(password, user);
	}
}