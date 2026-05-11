import java.math.BigDecimal;
import java.util.*;

class PaymentRequest {
    String id;
    BigDecimal amount;
    String user;

    public PaymentRequest(String id, BigDecimal amount, String user) {
        this.id = id;
        this.amount = amount;
        this.user = user;
    }
}

class PaymentProcessor {

    private Map<String, BigDecimal> userBalance = new HashMap<>();
    private Map<String, PaymentRequest> processed = new HashMap<>();
    private Set<String> refunded = new HashSet<>();

    public PaymentProcessor() {
        userBalance.put("alice", new BigDecimal("1000"));
        userBalance.put("bob", new BigDecimal("500"));
    }

    public String process(PaymentRequest request) {

        if (Objects.isNull(request)) return "Invalid request";

        if (request.amount.compareTo(BigDecimal.ZERO) < 0) {
            return "Invalid amount";
        }

        if (processed.containsKey(request.id)) {
            return "Success";
        }

        if (!userBalance.containsKey(request.user)) return "User Invalid";

        BigDecimal balance = userBalance.getOrDefault(request.user, BigDecimal.ZERO);

        if (balance.compareTo(request.amount) < 0) {
            return "Insufficient funds";
        }

        BigDecimal newBalance = balance.subtract(request.amount);

        userBalance.put(request.user, newBalance);

        processed.put(request.id, request);

        return "Success";
    }

    public String refund(String id) {

        if (Objects.isNull(id) || id.isBlank()) return "Invalid id";

        if (refunded.contains(id)) return "Refund already processed";

        PaymentRequest request = processed.get(id);

        if (Objects.isNull(request)) return "Incorrect id";

        BigDecimal balance = userBalance.getOrDefault(request.user, BigDecimal.ZERO);

        balance = balance.add(request.amount);

        userBalance.put(request.user, balance);

        refunded.add(id);

        return "Refunded";
    }

    public BigDecimal getBalance(String user) {
        return userBalance.get(user);
    }
}