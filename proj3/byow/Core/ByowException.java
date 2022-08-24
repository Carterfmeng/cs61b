package byow.Core;

/** General exception indicating a Gitlet error.  For fatal errors, the
 *  result of .getMessage() is the error message to be printed.
 *  @author P. N. Hilfinger
 */
class ByowException extends RuntimeException {


    /** A GitletException with no message. */
    ByowException() {
        super();
    }

    /** A GitletException MSG as its message. */
    ByowException(String msg) {
        super(msg);
    }

}
