package sk.fri.uniza.mocks;

public class InvalidPostingSheetStub extends PostingSheetDummy {
    @Override
    public void validate() throws InvalidPostingSheetException {
        throw new InvalidPostingSheetException();
    }
}
