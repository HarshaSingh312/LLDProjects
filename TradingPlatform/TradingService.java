import java.util.*;

public class TradingService {

    HashMap<String, User> users;
    Stocks stockList;
    OrderManager orderManager;
    List<Order> orders;

    public TradingService() {
        users = new HashMap<>();
        stockList = new Stocks();
        orders = new ArrayList<>();
        orderManager = new OrderManager(users, stockList);
    }

    public void addUser(String userId, int openingCash) {
        if (userId.isBlank() || openingCash <= 0) return;
        users.putIfAbsent(userId, new User(userId, openingCash));
    }

    public int getAccountBalance(String userId) {
        if (!users.containsKey(userId)) return -1;
        return users.get(userId).getAmount();
    }

    public void addStock(String stockName, int stockPrice) {
        if (stockPrice <= 0) return;
        stockList.addStock(stockName, stockPrice);
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.OPEN && order.getStockName().equals(stockName)) {
                orderManager.compute(order);
            }
        }
    }

    public List<String> showStocks() {
        return stockList.getStocks();
    }

    public String submitOrder(String userId, String stockName, String orderKind, String action, int qty, int limitPrice) {
        if (qty <= 0 || !stockList.isPresent(stockName)) return OrderStatus.REJECTED.name();
        Order order = new Order(userId, stockName, OrderKind.valueOf(orderKind), OrderAction.valueOf(action), qty, limitPrice);
        OrderStatus status = orderManager.compute(order);
        order.setStatus(status);
        orders.add(order);
        return status.name();
    }

    public List<String> viewStockHoldings(String userId) {
        if (!users.containsKey(userId)) return new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (StockHolding holding : users.get(userId).getStockHoldings()) {
            result.add(String.format("%s %d", holding.getStockName(), holding.getQuantity()));
        }
        return result;
    }

    public List<String> viewOrders(String userId) {
        if (!users.containsKey(userId) || orders == null) return new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getUserId().equals(userId)) {
                result.add(String.format("%s %s %d %s %s", order.getStockName(), order.getAction(), order.getQuantity(), order.getOrderKind(), order.getStatus()));
            }
        }
        return result;
    }
}

enum OrderKind { MARKET, LIMIT }
enum OrderAction { BUY, SELL }
enum OrderStatus { OPEN, FILLED, REJECTED }

class StockHolding {
    private String stockName;
    private int quantity;

    StockHolding(String stockName, int qty) {
        this.stockName = stockName;
        this.quantity = qty;
    }

    String getStockName() { return stockName; }
    int getQuantity() { return quantity; }
    void addQuantity(int qty) { this.quantity += qty; }
}

class User {
    private String id;
    private int amount;
    private List<StockHolding> stockHoldings = new ArrayList<>();

    User(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    int getAmount() { return amount; }
    void reduceAmount(int amount) { this.amount -= amount; }
    void addAmount(int amount) { this.amount += amount; }

    StockHolding getStockHolding(String stockName) {
        for (StockHolding h : stockHoldings) {
            if (h.getStockName().equals(stockName)) return h;
        }
        return null;
    }

    List<StockHolding> getStockHoldings() { return stockHoldings; }

    void addStock(String stockName, int qty) {
        for (StockHolding h : stockHoldings) {
            if (h.getStockName().equals(stockName)) {
                h.addQuantity(qty);
                return;
            }
        }
        stockHoldings.add(new StockHolding(stockName, qty));
    }

    void removeStock(String stockName, int qty) {
        for (StockHolding h : stockHoldings) {
            if (h.getStockName().equals(stockName)) {
                if (h.getQuantity() <= qty) {
                    stockHoldings.remove(h);
                } else {
                    h.addQuantity(-qty);
                }
                return;
            }
        }
    }
}

class Stocks {
    private final HashMap<String, Integer> stocks = new HashMap<>();
    private final List<String> stockList = new ArrayList<>();

    void addStock(String stockName, int stockPrice) {
        if (!stocks.containsKey(stockName)) stockList.add(stockName);
        stocks.put(stockName, stockPrice);
    }

    List<String> getStocks() {
        List<String> result = new ArrayList<>();
        for (String name : stockList) {
            result.add(String.format("%s %d", name, stocks.get(name)));
        }
        return result;
    }

    int getStockAmount(String name) { return stocks.get(name); }
    boolean isPresent(String name) { return stocks.containsKey(name); }
}

class Order {
    private final String id;
    private final String userId;
    private final String stockName;
    private final OrderKind orderKind;
    private final OrderAction action;
    private final int quantity;
    private final int limitPrice;
    private OrderStatus status = OrderStatus.OPEN;

    Order(String userId, String stockName, OrderKind orderKind, OrderAction action, int qty, int limitPrice) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.stockName = stockName;
        this.orderKind = orderKind;
        this.action = action;
        this.quantity = qty;
        this.limitPrice = limitPrice;
    }

    String getUserId() { return userId; }
    String getStockName() { return stockName; }
    OrderKind getOrderKind() { return orderKind; }
    OrderAction getAction() { return action; }
    int getQuantity() { return quantity; }
    int getLimitPrice() { return limitPrice; }
    OrderStatus getStatus() { return status; }
    void setStatus(OrderStatus status) { this.status = status; }
}

class OrderManager {
    HashMap<String, User> users;
    Stocks stockList;

    OrderManager(HashMap<String, User> users, Stocks stockList) {
        this.users = users;
        this.stockList = stockList;
    }

    OrderStatus compute(Order order) {
        if (OrderAction.BUY.equals(order.getAction())) return buy(order);
        else return sell(order);
    }

    OrderStatus sell(Order order) {
        User user = users.get(order.getUserId());
        StockHolding holding = user.getStockHolding(order.getStockName());

        if (holding == null || holding.getQuantity() < order.getQuantity()) return OrderStatus.REJECTED;

        int stockPrice = stockList.getStockAmount(order.getStockName());

        if (OrderKind.MARKET.equals(order.getOrderKind())) {
            return placeSellOrder(user, order, stockPrice);
        } else if (OrderKind.LIMIT.equals(order.getOrderKind())) {
            if (stockPrice >= order.getLimitPrice()) {
                return placeSellOrder(user, order, stockPrice);
            } else {
                return OrderStatus.OPEN;
            }
        }
        return OrderStatus.REJECTED;
    }

    private OrderStatus placeSellOrder(User user, Order order, int pricePerUnit) {
        user.removeStock(order.getStockName(), order.getQuantity());
        user.addAmount(pricePerUnit * order.getQuantity());
        order.setStatus(OrderStatus.FILLED);
        return OrderStatus.FILLED;
    }

    OrderStatus buy(Order order) {
        User user = users.get(order.getUserId());
        int stockAmount = stockList.getStockAmount(order.getStockName());
        int qty = order.getQuantity();

        if (user.getAmount() < stockAmount * qty) return OrderStatus.REJECTED;

        if (OrderKind.MARKET.equals(order.getOrderKind())) {
            return placeBuyOrder(user, order);
        } else if (OrderKind.LIMIT.equals(order.getOrderKind())) {
            if (stockAmount <= order.getLimitPrice()) {
                return placeBuyOrder(user, order);
            } else {
                return OrderStatus.OPEN;
            }
        }
        return OrderStatus.REJECTED;
    }

    OrderStatus placeBuyOrder(User user, Order order) {
        int stockAmount = stockList.getStockAmount(order.getStockName());
        user.reduceAmount(stockAmount * order.getQuantity());
        user.addStock(order.getStockName(), order.getQuantity());
        order.setStatus(OrderStatus.FILLED);
        return OrderStatus.FILLED;
    }
}
