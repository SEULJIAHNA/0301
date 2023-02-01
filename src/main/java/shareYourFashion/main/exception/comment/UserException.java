package shareYourFashion.main.exception.comment;

public class UserException extends BaseException {
    private BaseExceptionType exceptionType;


    public UserException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
