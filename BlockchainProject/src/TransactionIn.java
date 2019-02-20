
public class TransactionIn {
	public String toId; //Reference to TransactionOutputs -> transactionId
	public TransactionOut tout; //Contains the Unspent transaction output
	
	public TransactionIn(String toId) {
		this.toId = toId;
	}
}
