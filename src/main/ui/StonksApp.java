package ui;

import model.Investor;
import model.Portfolio;
import model.Stock;
import model.StockMarket;

import java.util.Scanner;

public class StonksApp {
    private Scanner input;
    private Investor investor;
    private StockMarket sm;
    private Portfolio activePortfolio;
    boolean keepGoing;

    // This is an integer that keeps track of the display menu
    // 1 = investor menu; 2 = portfolio menu; 3 = stock market menu
    private Integer activeMenu;

    public StonksApp(String name, Double startingFunds) {
        investor = new Investor(name, startingFunds);
        sm = new StockMarket();
        runStonks();

    }

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
            } else if (activeMenu == 3) {
                processCommandPortfolio(command);
            } else if (activeMenu == 4) {
                processCommandStockMarket(command);
            }
        }
    }

    private void init() {
        input = new Scanner(System.in);
        activeMenu = 1;
    }

    private void displayMenu(Integer activeMenu) {
        if (activeMenu == 1) {
            System.out.println("\nThis is the main investor menu");
            System.out.println("\nSelect an option:");
            System.out.println("\ta -> add a new portfolio");
            System.out.println("\tr -> remove a portfolio");
            System.out.println("\tl -> get list of all existing profiles");
            System.out.println("\tp -> view an exiting portfolio");
            System.out.println("\ts -> go to the stock market menu");
            System.out.println("\tq -> quit");
        }  else if (activeMenu == 2) {
            System.out.println("\nThis is the portfolio menu for " + activePortfolio.getPortfolioName());
            System.out.println("\nSelect an option:");
            System.out.println("\tb -> buy stock");
            System.out.println("\ts -> sell stock");
            System.out.println("\tt -> transfer stock to another portfolio");
            System.out.println("\tf -> get the amount of liquid funds in this portfolio");
            // list of all stock in this portfolio
            System.out.println("\tn -> see the total net worth of the stocks in this portfolio");
            System.out.println("\ti -> go back to the investor menu");
            System.out.println("\tq -> quit");
        } else if (activeMenu == 3) {
            System.out.println("\nThis is the stock market menu:");
            System.out.println("\nSelect an option:");
            System.out.println("\tp -> see the current price of a stock");
            System.out.println("\tq -> quit");
        }
    }

    private void processCommandInvestor(String command) {
        System.out.println("Welcome to the investor menu\n");

        if (command.equals("a")) {
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

        } else if (command.equals("r")) {
            System.out.println("Enter the name of the portfolio you wish to remove (case-sensitive):");
            String toRemove = input.next();

            while (!investor.getPortfolioMap().containsKey(toRemove)) {
                System.out.println(toRemove + " is not an existing portfolio!");
                System.out.println("Double check the spelling and re-enter the portfolio name:");
                toRemove = input.next();
            }

            investor.removePortfolio(toRemove);

        } else if (command.equals("l")) {
            for (String p : investor.getPortfolioMap().keySet()) {
                System.out.println(p);
            }

        } else if (command.equals("p")) {
            System.out.println("Enter the name of the portfolio you wish to view:");
            String toView = input.next();

            while (!investor.getPortfolioMap().containsKey(toView)) {
                System.out.println("That portfolio does not exist.");
                System.out.println("Double check the spelling and re-enter the portfolio name:");
                toView = input.next();
            }
            investor.setActivePortfolio(toView);
            activePortfolio = investor.getActivePortfolio();
            investor.unsetActivePortfolio();

            activeMenu = 2;
        } else if (command.equals("s")) {
            System.out.println("let's take a look at the stock market!");
            activeMenu = 3;
        } else if (command.equals("q")) {
            keepGoing = false;
        } else {
            System.out.println("Selection not valid...");
        }
    }


    private void processCommandPortfolio(String command) {
        if (activePortfolio == null) {
            System.out.println("Sorry, an error as occured :(");
            System.out.println("Taking you back to the investor menu:");
            activeMenu = 1;
        }

        if (command.equals("b")) {
            System.out.println("Type the name of the stock you wish to buy:");
            String toBuy = input.next();
            toBuy = toBuy.toUpperCase();

            while (!sm.containsStock(toBuy)) {
                System.out.println("This stock does not exist!");
                System.out.println("Double check spelling and enter stock name again:");
                toBuy = input.next();
                toBuy = toBuy.toUpperCase();
            }

            System.out.println("Enter the amount of the stock you wish to buy:");
            Integer amount = input.nextInt();

            while (amount < 1 || sm.getStockValue(toBuy) * amount > activePortfolio.getPortfolioFunds()) {
                if (amount < 1) {
                    System.out.println("Please enter an integer larger than 1:");
                } else {
                    System.out.println("You cannot afford this!");
                    System.out.println(amount + " shares of " + toBuy + " costs " + sm.getStockValue(toBuy) * amount);
                    System.out.println("You only have " + activePortfolio.getPortfolioFunds() + " in this portfolio");
                    System.out.println("Please enter a new quantity:");
                }
                amount = input.nextInt();
            }

            activePortfolio.buyStock(sm, toBuy, amount);
            System.out.println("Successfully bought " + amount + " shares of " + toBuy);

        } else if (command.equals("s")) {
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
            Integer amount = input.nextInt();

            Stock stock = activePortfolio.getStockInPortfolio(toSell);
            Integer total = stock.getQuantityOfStock();

            if (amount > total) {
                System.out.println("You do not have that much + " + toSell + " to sell");
                System.out.println("You only own" + total + " shares");
                System.out.println("Please enter a valid quantity:");
                amount = input.nextInt();
            }

            activePortfolio.sellStock(sm,toSell,amount);
            System.out.println("Successfully sold " + amount + " shares of " + toSell);

        } else if (command.equals("t")) {
            System.out.println("Enter the name of the portfolio you wish to transfer to:");
            String other = input.next();

            while (!investor.getPortfolioMap().containsKey(other)) {
                System.out.println("Please enter a valid portfolio name:");
                other = input.next();
            }

            System.out.println("Please enter the name of the stock you wish to transfer:");
            String toTransfer = input.next();
            toTransfer = toTransfer.toUpperCase();

            while (!activePortfolio.isStockInPortfolio(toTransfer)) {
                System.out.println("This stock does not exist!");
                System.out.println("Double check spelling and enter stock name again:");
                toTransfer = input.next();
                toTransfer = toTransfer.toUpperCase();
            }

            System.out.println("Enter the amount of the stock you wish to transfer:");
            Integer amount = input.nextInt();

            Stock stock = activePortfolio.getStockInPortfolio(toTransfer);
            Integer total = stock.getQuantityOfStock();

            if (amount > total) {
                System.out.println("You do not have that much + " + toTransfer + " to sell");
                System.out.println("You only own" + total + " shares");
                System.out.println("Please enter a valid quantity:");
                amount = input.nextInt();
            }

            activePortfolio.transferStock(sm, investor.getPortfolioMap().get(other), toTransfer, amount);
            System.out.println("Successfully transferred " + amount + " "
                    + "shares of " + toTransfer + " to portfolio" + other);

        } else if (command.equals("f")) {
            System.out.println("This portfolio has " + activePortfolio.getPortfolioFunds() + " in liquid funds.");

        } else if (command.equals("n")) {
            System.out.println("The net worth of this portfolio (stock-only) is "
                    + activePortfolio.getPortfolioNetWorth());

        } else if (command.equals("i")) {
            System.out.println("Taking you back to the investor menu:");
            activeMenu = 1;
        } else if (command.equals("q")) {
            keepGoing = false;
        }
    }

    private void processCommandStockMarket(String command) {

    }
}
