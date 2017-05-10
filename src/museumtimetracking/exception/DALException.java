/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.exception;

import java.sql.SQLException;

public class DALException extends SQLException {

    public DALException(String message, Throwable cause) {
        super(message, cause);
    }

    public DALException(Throwable cause) {
        super(cause);
    }

    public DALException(String message) {
        super(message);
    }

}
