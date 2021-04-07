# Stonks!

## A stock market simulator

![Stonks!](https://github.students.cs.ubc.ca/CPSC210-2020W-T2/project_n1a0o/blob/master/data/images/stonks.png)

### About this application

This is a single player application which mimics a stock market envrionment. Designed as a simulator, the player beings with a certain amount of funds and immediately thrown into the wild west of stock trading. The investor will have to rely on their intuition along with a side of luck to grow their investment to the moon. This game invites all to come and experience the thrill of growing your investment fund through strategic stock purchases and sales. Or wallow in dispair as you see slowly see your money consumed by economic turnmoil.

The player begins as an investor with a certain amount of funds and access to a stock market. From there on, they are responsible for creating portfolios and within each portfolio, buy whichever stocks they think will be the most successful. Be careful though! Unless the investor found their TSLA, it might not be a good idea to hold on to the stock for a long time, so they may have to sell (remove) the stock for some profit and to buy the next big stock. 

Unfortunetly, do to project limitations, *APIs are not allowed and thus this simulator in no way reflects the current market*. To address this shortcoming, I had to make a fake stock market which automatically generates all stock prices randomly. In addition, all the stocks will fluctuate randomly with time, but overall, all the stocks will tend to have an upward trend (bullish). 

### Who is this for?

This application is open to anyone to use. Feedback is always appreciated :). 

### Project motivation

This is a neat little project that I am doing mainly because most of the stock trading apps that I found online were all pretty terrible and either infested with ads or require you to pay for more premium features. So I am being the change that I want to see and making a game that is ad-free and contains all the so-called "premium" features without any cost. 

### Some user stories of Stonks!

The following are some hypothetical user stories of this application.

- As a user, I am new to stock trading and I want to gain some more experience before I trade with real money. This app is perfect because I can simuate a real stock trading envrionment and I do not have to use real money.

- As a user, I may want to have different portfolios to store my stock in various categories. For example, I may want a "short-term" portfolio and and "long-term" portfolio. This app allows me to add and remove portfolios.

- As a user, I want to be able to control what stocks I buy and sell in each of my portfolios. With this app, I am able to buy and sell stocks from the stock market (adding/removing stocks from a portfolio), as well as transferring stock from one portfolio to another.

- As a user, I would like to see the performance of a stock with respect to time. This app keeps track of the changes in the stock price and it is very easy for me to extract that information to visualize and inform myself on whether or not I want to buy a particular stock.

- As a user, I would like to save my progress. In other words, I want to be able to store my invsestor profile (inculding portfolio, stocks, stock market state) before I close out of the app. Data persistance has been implemented in this lab such that progress can be stored in a JSON file.

- As a user, I want to be able to load an investor profile that I have saved so I do not have to restart every time I open this application. This program has the funcationality to read saved JSON files and load the profile from the point which I saved, so that I can continue making money!

### Phase 4: Task 2
A map interface was implemented in several of my classes.

- In the Investor class, a HashMap<String, Portfolio> was used to track the different portfolios by using a key/value pair of a String and Portfolio object.
- In the Portfolio class, a HashMap<String, Stock> was used to track the various stocks contained within the portfolio by using a key/value pair of a String and Stock object.
- In the Stock class, a HashMap<Integer, Double> was used to track the value of a stock by the day using a key/value pair of an Integer and Double object.

### Phase 4: Task 3
From the UML diagram, it is obvious that there are issues with cohesion and also instances of unnecessary coupling. Some adjustments that I can intoduce in future versions of my application are noted as follows:

- The class StonksAppRunner and StonksGUI have a bi-directional association. However, the two classes themselves have fields (associations) for the Stockmarket and Investor class even through they are dealing with the same Stockmarket and Investor object. To reduce unnecessary coupling, the association to Stockmarket and Investor should be removed from the StonksAppRunner class. The bi-direction association between StonksAppRunner and StonksGUI ensure that StonksAppRunner still "knows" about the Stockmarket and Investor.

- PortfolioPageGUI has an association with a Portfolio object. However in the context of the diagram, it looks very strange; it doesn't make a whole lot of sense from a design prespective to have fields of StockMarket and Investor in the StonksGUI class, but have a Portfolio field standalone in a subclass of StonksGUI. Thus, the association to Portfolio should be moved to the StonksGUI class. 

- Investor has two associations with Portfolio: a hashmap and a single field. Refactoring should be done so that there is only one association to Portfolio represented by a hashmap. 

- Another issue that is not so obvious from this UML diagram is the idea of single point of control. The model classes themselves contain methods to load data from file, which is technically not the duty of the model classes. Rather, it should be the job of the JsonReader class to load data from file. All code related to loading from file should be refactored into the JsonReader class. In the process of refactoring, I expect that there will be additional single associations from JsonReader to the Stockmarket class and the Investor class. 
