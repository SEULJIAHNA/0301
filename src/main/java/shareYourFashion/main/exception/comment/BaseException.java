package shareYourFashion.main.exception.comment;

public abstract class BaseException extends RuntimeException{
    public abstract BaseExceptionType getExceptionType();
}
