/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

public class APersonManager {

    public static final String ONLY_NUMBERS = "[0-9]+";

    /**
     * Validate the phone number
     *
     * @param phone
     * @return
     */
    public static boolean validatePhone(String phone) {
        return phone.matches(ONLY_NUMBERS);
    }

    /**
     * Check that all parameters pass
     *
     * @param firstName
     * @param lastName
     * @param phone
     * @return
     */
    public static boolean checkAllValidation(boolean firstName, boolean lastName, boolean phone) {
        return firstName == true && lastName == true && phone == true;
    }

}
