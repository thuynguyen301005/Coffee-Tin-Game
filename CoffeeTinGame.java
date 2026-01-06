package a1_2301040174;

import java.util.Arrays;
import java.util.Random;

/**
 * CoffeeTinGame: Simulates a game with a tin of coffee beans.
 * 
 * Game Rules: - Take out two beans from the tin: + If they are the same color:
 * Remove both and add one blue bean (B). + If they are different colors: Remove
 * both and add one green bean (G). - Repeat the process until only one bean
 * remains.
 * 
 * Approach: - Use an array `tin` to store the beans in the tin. - Use
 * `BeansBag` to retrieve additional beans when needed. - Implement Fisher-Yates
 * shuffle to randomize `BeansBag`.
 */

public class CoffeeTinGame {
	private static final char GREEN = 'G'; // symbol for green bean
	private static final char BLUE = 'B'; // symbol for blue bean
	private static final char REMOVED = '-'; // symbol for removed beans
	private static final char NULL = '\u0000'; // symbol for null beans
	private static final char[] BeansBag = new char[30];
	    
	    static {
	   Random random = new Random();
	   int totalBeans = BeansBag.length;
       int[] counts = {totalBeans / 3, totalBeans / 3, totalBeans - 2 * (totalBeans / 3)};
       char[] types = {GREEN, BLUE, '-'};

       for (int i = 0; i < totalBeans; i++) {
           int type;
           do {
               type = random.nextInt(3);
           } while (counts[type] == 0);
           BeansBag[i] = types[type];
           counts[type]--;
       }

		// fisher-yates shuffle
		for (int i = BeansBag.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			char temp = BeansBag[i];
			BeansBag[i] = BeansBag[index];
			BeansBag[index] = temp;
		}
	}

	public static void main(String[] args) {
		// Create a list of multiple bean boxes to test
		char[][] tins = { { BLUE, BLUE, BLUE, GREEN, GREEN }, // 2 Green Beans
				{ BLUE, BLUE, BLUE, GREEN, GREEN, GREEN }, // 3 Green Beans
				{ GREEN }, // 1 Green Bean
				{ BLUE }, // 0 Green Bean
				{ BLUE, GREEN } // 1 Green Bean
		};

        for (int i = 0; i < tins.length; i++) {
            char[] tin = tins[i];

			// expected last bean
			// p0 = green parity /\
			// (p0=1 -> last=GREEN) /\ (p0=0 -> last=BLUE)
			// count number of greens

			int greens = 0;
			for (char bean : tin) {
				if (bean == GREEN) {
					greens++;
				}
			}
			// expected last bean
			final char last = (greens % 2 == 1) ? GREEN : BLUE;
			// print the content of tin before the game
			System.out.printf("%nTIN (%d Gs): %s %n", greens, Arrays.toString(tin));

			// perform the game , get actual last bean
			char lastBean = tinGame(tin);

			// lastBean = last \/ lastBean != last
			// print the content of tin and last bean
			System.out.printf("tin after: %s %n", Arrays.toString(tin));

			// Compare result
			if (lastBean == last) {
				System.out.printf("Last bean: %c%n", lastBean);
			} else {
				System.out.printf("Oops, wrong last bean: %c (expected: %c)%n", lastBean, last);
			}
		}
	}

	// perform game use updateTin for tinGame

	public static char tinGame(char[] tin) {
		while (hasAtLeastTwoBeans(tin)) {
			char[] twoBeans = takeTwo(tin);
			updateTin(tin, twoBeans[0], twoBeans[1]);
		}
		return anyBean(tin);
	}

	/**
	 * @effects if tin has at least two beans return true else return false
	 */

	public static boolean hasAtLeastTwoBeans(char[] tin) {
		int count = 0;
		for (char bean : tin) {
			if (bean != REMOVED) {
                count++;    // only increment count if the bean is not REMOVED
            }
            if (count >= 2) // enough beans
                return true;
        }
			return false;
	}
	
	
	/**
	 * @effects if there are beans in tin then return any such bean else return '\u0000'
	 *          (null character)
	 */
	public static char anyBean(char[] tin) {
		for (char bean : tin) {
			if (bean != REMOVED) {
				return bean;
			}
		}
		return NULL;
	}


	/**
	 * @requires tin has at least 2 beans left
	 * @modifies tin
	 * @effects remove any two beans from tin and return them
	 */
	private static char[] takeTwo(char[] tin) {
		// take 2 beans from tin randomly
		char first = takeOne(tin);
        char second = takeOne(tin);
        return new char[]{first, second};
	}

	
	public static char takeOne(char[] tin) {
		int index;
		do {
			index = randInt(tin.length);
		} while (tin[index] == REMOVED);
		char bean = tin[index];
		tin[index] = REMOVED;
		return bean;
	}
	
	
	// Generate a random integer from 0 to n-1
	 public static int randInt(int n) {
         Random random = new Random();
        return  random.nextInt(n);
    
     }

	 /**
		 * Updates the tin after taking out two beans.
		 *
		 * @param tin     The array representing the tin.
		 * @param beanOne The first bean taken out.
		 * @param beanTwo The second bean taken out.
		 */
		// Update tin after each play round
		public static void updateTin(char[] tin, char beanOne, char beanTwo) {
			 if (beanOne == beanTwo) {
	             putIn(tin, getBean(BeansBag, BLUE));    // get one BLUE bean from extra bag when 2 beans from tin have the same color
	         } else {
	             putIn(tin, GREEN);     // put the GREEN bean back to the tin when 2 beans have different color
	         }
	     }


		/**
		 * @requires tin has vacant positions for new beans
		 * @modifies tin
		 * @effects place bean into any vacant position in tin
		 */

		// Simulated method to put a bean into the tin
		public static void putIn(char[] tin, char bean) {
			for (int i = 0; i < tin.length; i++) {
				if (tin[i] == REMOVED) {
					tin[i] = bean;
					return;
				}
			}
		}
	// Retrieve a random bean of the specified type from the extra beans bag

	public static char getBean(char[] beansBag, char beanType) {
	    // Check if there is at least one bean of the requested type in the bag
	    boolean found = false;
	    for (char bean : beansBag) {
	        if (bean == beanType) {
	            found = true;
	            break;
	        }
	    }

	    // If no bean of the requested type is found, return NULL to avoid an infinite loop
	    if (!found) {
	        return NULL;
	    }

	    // If the bean type exists in the bag, randomly select one
	    int index;
	    do {
	        index = randInt(beansBag.length);
	    } while (beansBag[index] != beanType);

	    // Remove the selected bean from the bag and return it
	    char bean = beansBag[index];
	    beansBag[index] = REMOVED;
	    return bean;
	}		
}
