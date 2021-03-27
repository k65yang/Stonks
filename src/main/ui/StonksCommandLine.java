package ui;

import exceptions.IncompatibleStockMarketException;
import exceptions.InvalidClassTypeException;
import model.Investor;
import model.Portfolio;
import model.Stock;
import model.StockMarket;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Stonks (stock market simulator) application
public class StonksCommandLine {
    private Scanner input;
    private Investor investor;
    private StockMarket sm;
    private Portfolio activePortfolio;
    boolean keepGoing;

    // This is an integer that keeps track of the display menu
    // 1 = investor menu; 2 = portfolio menu; 3 = stock market menu
    private Integer activeMenu;

    // MODIFIES: this
    // EFFECTS: runs the Stonks application
    public StonksCommandLine(String name, Double startingFunds) {
        investor = new Investor(name, startingFunds);
        runStonks();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runStonks() {
        keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu(activeMenu);
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
                System.out.println("Goodbye!");
            } else if (activeMenu == 1) {
                processCommandInvestor(command);
            } else if (activeMenu == 2) {
                processCommandPortfolio(command);
            } else if (activeMenu == 3) {
                processCommandStockMarket(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the stock market
    private void init() {
        input = new Scanner(System.in);
        activeMenu = 1;
        sm = new StockMarket();
    }

    // EFFECTS: displays menu of options to the user depending on where the user has navigated
    //          to in the application
    private void displayMenu(Integer activeMenu) {
        if (activeMenu == 1) {
            menuInvestor();
        }  else if (activeMenu == 2) {
            menuPortfolio();
        } else if (activeMenu == 3) {
            menuStockMarket();
        }
    }

    // EFFECTS: displays the investor statistics and investor options for the user
    private void menuInvestor() {
        System.out.println("\nThis is the main investor menu");

        String formatted = String.format("%.2f", investor.getInvestorFunds());
        System.out.println("You have $" + formatted + " in funds");
        System.out.println("\nExisting portfolios and their total funds:");

        for (String p : investor.getPortfolioMap().keySet()) {
            formatted = String.format("%.2f", investor.getPortfolioMap().get(p).getPortfolioFunds());
            System.out.println(p + " --- $" + formatted);
        }

        System.out.println("\nSelect an option:");
        System.out.println("\ta -> add a new portfolio");
        System.out.println("\tm -> add funds to an existing portfolio");
        System.out.println("\tr -> remove a portfolio");
        System.out.println("\tp -> view an exiting portfolio");
        System.out.println("\ts -> go to the stock market menu");
        System.out.println("\tu -> update the day in game time");
        System.out.println("\tf -> save to file");
        System.out.println("\tl -> load from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays the portfolio statistics and portfolio options for the user
    private void menuPortfolio() {
        if (activePortfolio == null) {
            System.out.println("Sorry, an error as occurred :(");
            System.out.println("Taking you back to the investor menu:");
            activeMenu = 1;
        }

        String formattedFunds = String.format("%.2f", activePortfolio.getPortfolioFunds());
        String formattedNetWorth = String.format("%.2f", activePortfolio.getPortfolioNetWorth());

        System.out.println("\nThis is the portfolio menu for " + activePortfolio.getPortfolioName());
        System.out.println("Available funds: " + formattedFunds);
        System.out.println("Net Worth of Stock: " + formattedNetWorth);

        System.out.println("\nList, quantity, and share price of stocks available in this portfolio:");
        for (Stock s : activePortfolio.getPortfolioMap().values()) {
            String formattedPrice = String.format("%.2f", sm.getStockValue(s.getStockName()));
            System.out.println(s.getStockName() + ": " + s.getQuantityOfStock() + " shares at " + formattedPrice);
        }

        System.out.println("\nSelect an option:");
        System.out.println("\tb -> buy stock");
        System.out.println("\ts -> sell stock");
        System.out.println("\tt -> transfer stock to another portfolio");
        System.out.println("\ti -> go back to the investor menu");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays the stock market and its menu for the user
    private void menuStockMarket() {
        System.out.println("This is the current selection of stocks and their prices:\n");
        for (String stock : sm.getStockList()) {
            String formatted = String.format("%.2f", sm.getStockValue(stock));
            System.out.println(stock + ": " + formatted);
        }
        System.out.println("\nSelect an option:");
        System.out.println("\tp -> see the price history of a stock");
        System.out.println("\ti -> return to the investor menu");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: processes user inputs when on the investor menu
    private void processCommandInvestor(String command) {
        if (command.equals("a")) {
            keyEventAInvestorMenu();
        } else if (command.equals("m")) {
            keyEventMInvestorMenu();
        } else if (command.equals("r")) {
            keyEventRInvestorMenu();
        } else if (command.equals("p")) {
            keyEventPInvestorMenu();
        } else if (command.equals("s")) {
            keyEventSInvestorMenu();
        } else if (command.equals("u")) {
            keyEventUInvestorMenu();
        } else if (command.equals("f")) {
            keyEventFInvestorMenu();
        } else if (command.equals("l")) {
            keyEventLInvestorMenu();
        } else if (command.equals("q")) {
            keyEventQ();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new portfolio
    private void keyEventAInvestorMenu() {
        System.out.println("Enter the name of your new portfolio:");
        String newPortfolio = input.next();

        while (investor.getPortfolioMap().containsKey(newPortfolio)) {
            System.out.println("A portfolio with that name already exists!");
            System.out.println("Please enter a unique name:");
            newPortfolio = input.next();
        }

        System.out.println("Enter the amount of funds you want to transfer into portfolio " + newPortfolio + ":");
        double amount = input.nextDouble();

        while (amount > investor.getInvestorFunds() || amount < 0.0) {
            System.out.println("Error depositing the money.");
            System.out.println("You have $" + investor.getInvestorFunds() + " in funds");
            System.out.println("Please enter a valid amount:");
            amount = input.nextDouble();
        }

        investor.addPortfolio(newPortfolio, amount);
    }

    // MODIFIES: this
    // EFFECTS: adds money to a specific portfolio
    private void keyEventMInvestorMenu() {
        System.out.println("Enter the name of the portfolio you wish to add money to:");
        String toAddMoney = input.next();

        while (!investor.getPortfolioMap().containsKey(toAddMoney)) {
            System.out.println(toAddMoney + " is not an existing portfolio!");
            System.out.println("Double check the spelling and re-enter the portfolio name:");
            toAddMoney = input.next();
        }

        System.out.println("Enter the amount you wish to add to the portfolio:");
        double amount = input.nextDouble();

        while (amount < 0 || amount > investor.getInvestorFunds()) {
            if (amount < 0) {
                System.out.println("Please enter a positive value:");
            } else if (amount > investor.getInvestorFunds()) {
                System.out.println("You do not have that much money :(");
                System.out.println("You only have " + investor.getInvestorFunds() + ". Please enter valid amount:");
            }
            amount = input.nextDouble();
        }

        investor.addFundsToPortfolio(toAddMoney, amount);
    }

    // MODIFIES: this
    // EFFECTS: removes a portfolio
    private void keyEventRInvestorMenu() {
        System.out.println("Enter the name of the portfolio you wish to remove (case-sensitive):");
        String toRemove = input.next();

        while (!investor.getPortfolioMap().containsKey(toRemove)) {
            System.out.println(toRemove + " is not an existing portfolio!");
            System.out.println("Double check the spelling and re-enter the portfolio name:");
            toRemove = input.next();
        }

        investor.removePortfolio(toRemove);
    }

    // EFFECTS: brings the user to a specific portfolio menu
    private void keyEventPInvestorMenu() {
        System.out.println("Enter the name of the portfolio you wish to view:");
        String toView = input.next();

        while (!investor.getPortfolioMap().containsKey(toView)) {
            System.out.println("That portfolio does not exist.");
            System.out.println("Double check the spelling and re-enter the portfolio name:");
            toView = input.next();
        }
        activePortfolio = investor.setActivePortfolio(toView);
        investor.unsetActivePortfolio();

        activeMenu = 2;
    }

    // EFFECTS: brings the user to the stock market menu
    private void keyEventSInvestorMenu() {
        System.out.println("let's take a look at the stock market!");
        activeMenu = 3;
    }

    // EFFECTS: updates the day in game time
    private void keyEventUInvestorMenu() {
        System.out.println("Enter the number of days you which to increment:");
        int days = input.nextInt();

        while (days < 1) {
            System.out.println("Invalid value. Please enter an integer larger than 0:");
            days = input.nextInt();
        }

        sm.updateStockPrice(days);
        investor.updateAllPortfolios(sm);
    }

    // EFFECTS: save the current game state to file
    private void keyEventFInvestorMenu() {
        System.out.println("Please type the name of this save:");
        String name = input.next();

        String saveInvestorName = "Investor-" + name;
        String saveStockMarketName = "StockMarket-" + name;

        String investorDestination = "./data/" + saveInvestorName + ".json";
        String stockMarketDestination = "./data/" + saveStockMarketName + ".json";

        try {
            JsonWriter writerInvestor = new JsonWriter(investorDestination);
            JsonWriter writerStockMarket = new JsonWriter(stockMarketDestination);

            writerInvestor.open();
            writerInvestor.write(investor);
            writerInvestor.close();

            writerStockMarket.open();
            writerStockMarket.write(sm);
            writerStockMarket.close();

            System.out.println("Investor file saved as: " + saveInvestorName);
            System.out.println("Stock market file saved as: " + saveStockMarketName);
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, an error has occurred. Bringing you back to the main menu.");
        }
    }

    private void keyEventLInvestorMenu() {
        System.out.println("Please type the name of the save to load (the name after the hyphen, ie. Investor-***):");
        String name = input.next();

        String investorDestination = "./data/Investor-" + name + ".json";
        String stockMarketDestination = "./data/StockMarket-" + name + ".json";

        try {
            JsonReader readerInvestor = new JsonReader("investor", investorDestination);
            JsonReader readerStockMarket = new JsonReader("stockmarket", stockMarketDestination);

            investor = (Investor) readerInvestor.read();
            sm = (StockMarket) readerStockMarket.read();
        } catch (Exception e) {
            System.out.println("Sorry, an error has occurred. Bringing you back to the main menu.");
        }




    }

    // EFFECTS: stops the application
    private void keyEventQ() {
        keepGoing = false;
    }

    // EFFECTS: processes user inputs when on the portfolio menu
    private void processCommandPortfolio(String command) {
        if (command.equals("b")) {
            keyEventBPortfolio();
        } else if (command.equals("s")) {
            keyEventSPortfolio();
        } else if (command.equals("t")) {
            keyEventTPortfolio();
        } else if (command.equals("i")) {
            keyEventIPortfolio();
        } else if (command.equals("q")) {
            keyEventQ();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: buys a quantity of specific stock
    private void keyEventBPortfolio() {
        System.out.println("Type the name of the stock you wish to buy:");
        String toBuy = input.next();

        while (!sm.containsStock(toBuy)) {
            System.out.println("This stock does not exist! Double check spelling and enter stock name again:");
            toBuy = input.next();
        }

        System.out.println("Enter the amount of the stock you wish to buy:");
        int amount = input.nextInt();

        while (amount < 1 || sm.getStockValue(toBuy) * amount > activePortfolio.getPortfolioFunds()) {
            if (amount < 1) {
                System.out.println("Please enter an integer larger than 1:");
            } else {
                System.out.println("You cannot afford this!");
                String formatted = String.format("%.2f", sm.getStockValue(toBuy) * amount);
                System.out.println(amount + " shares of " + toBuy + " costs " + formatted);
                System.out.println("You only have " + activePortfolio.getPortfolioFunds() + " in this portfolio");
                System.out.println("Please enter a new quantity:");
            }
            amount = input.nextInt();
        }

        activePortfolio.buyStock(sm, toBuy, amount);
        System.out.println("Successfully bought " + amount + " shares of " + toBuy);
    }

    // MODIFIES: this
    // EFFECTS: sells a quantity of specific stock
    private void keyEventSPortfolio() {
        System.out.println("Type the name of the stock you wish to sell:");
        String toSell = input.next();
        toSell = toSell.toUpperCase();

        while (!activePortfolio.isStockInPortfolio(toSell)) {
            System.out.println("This stock does not exist!");
            System.out.println("Double check spelling and enter stock name again:");
            toSell = input.next();
            toSell = toSell.toUpperCase();
        }

        System.out.println("Enter the amount of the stock you wish to sell:");
        int amount = input.nextInt();

        Stock stock = activePortfolio.getStockInPortfolio(toSell);
        int total = stock.getQuantityOfStock();

        if (amount > total) {
            System.out.println("You do not have that much " + toSell + " to sell! You only own " + total + " shares");
            System.out.println("Please enter a valid quantity:");
            amount = input.nextInt();
        }

        activePortfolio.sellStock(sm,toSell,amount);
        System.out.println("Successfully sold " + amount + " shares of " + toSell);
    }

    // MODIFIES: this
    // EFFECTS: transfers a quantity of specific stock to another existing portfolio
    private void keyEventTPortfolio() {
        System.out.println("Enter the name of the portfolio you wish to transfer to:");
        String other = input.next();

        while (!investor.getPortfolioMap().containsKey(other)) {
            System.out.println("Please enter a valid portfolio name:");
            other = input.next();
        }

        System.out.println("Please enter the name of the stock you wish to transfer:");
        String toTransfer = input.next();

        while (!activePortfolio.isStockInPortfolio(toTransfer)) {
            System.out.println("This stock does not exist!");
            System.out.println("Double check spelling and enter stock name again:");
            toTransfer = input.next();
        }

        System.out.println("Enter the amount of the stock you wish to transfer:");
        int amount = input.nextInt();

        Stock stock = activePortfolio.getStockInPortfolio(toTransfer);
        int total = stock.getQuantityOfStock();

        while (amount > total) {
            System.out.println("You only own" + total + " shares! Please enter a valid quantity");
            amount = input.nextInt();
        }

        activePortfolio.transferStock(sm, investor.getPortfolioMap().get(other), toTransfer, amount);
        System.out.println("Successfully transferred " + amount + " shares of " + toTransfer + " to " + other);
    }

    // EFFECTS: returns the user to the investor menu
    private void keyEventIPortfolio() {
        System.out.println("Taking you back to the investor menu:");
        activeMenu = 1;
    }

    // EFFECTS: processes user inputs when on the stock market menu
    private void processCommandStockMarket(String command) {
        if (command.equals("p")) {
            keyEventPStockMarket();
        } else if (command.equals("i")) {
            keyEventIStockMarket();
        } else if (command.equals("q")) {
            keyEventQ();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: displays the stock price history of a particular stock
    private void keyEventPStockMarket() {
        System.out.println("Enter the name of the stock you wish to view:");
        String toView = input.next();
        toView = toView.toUpperCase();

        while (!sm.containsStock(toView)) {
            System.out.println("This stock does not exist! Double check spelling and try again:");
            toView = input.next();
            toView = toView.toUpperCase();
        }

        List<Double> history = sm.getAllStockHistory(toView);
        System.out.println("\nThis is the price history of " + toView);
        System.out.println("Day" + "\tPrice");
        for (int i = 0; i < history.size(); i++) {
            String formatted = String.format("%.2f", history.get(i));
            System.out.println("Day " + i + "\t" + formatted);
        }
        System.out.println("");
    }

    // EFFECTS: returns the user to the investor menu
    private void keyEventIStockMarket() {
        System.out.println("Taking you back to the investor menu");
        activeMenu = 1;
    }
}
