package strategy;

public class TransactionStrategyFactory {
    public static TransactionStrategy getStrategy(String type) {
        if (type == null)
            return null;

        switch (type.toLowerCase()) {
            case "withdraw":
                return new WithdrawStrategy();
            case "deposit":
                return new DepositStrategy();
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
    }
}
