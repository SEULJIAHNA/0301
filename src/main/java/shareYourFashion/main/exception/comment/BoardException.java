package shareYourFashion.main.exception.comment;

public class BoardException extends BaseException {

    private BaseExceptionType baseExceptionType;


    public BoardException(BaseExceptionType baseExceptionType) {
        this.baseExceptionType = baseExceptionType;
    }
    public BoardException() {
    }
    @Override
    public BaseExceptionType getExceptionType() {
        return this.baseExceptionType;
    }
}
