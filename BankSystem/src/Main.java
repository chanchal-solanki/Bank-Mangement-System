import java.util.*;

class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int choice;

        HashMap<String , BankAccount> map = new HashMap<>();
        ValidationUtil validation = new ValidationUtil();
        BankService service = new BankService(validation,sc,map);

        do{
            System.out.println("-------Welcome to Our Bank-------");
            System.out.println("Enter Choice ! What help you need..");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. View Account Details");
            System.out.println("6. Check Interest Rate");
            System.out.println("7. Exit");
            choice = sc.nextInt();
            
            switch(choice){
                case 1: 
                    service.CreateAccount();
                    break;
                case 2:
                    service.depositMoney();
                    break;
                case 3:
                    service.withdarwMoney();
                    break;
                case 4:
                    service.checkBalance();
                    break;
                case 5:
                    service.printDetails();
                    break;
                case 6 : 
                    service.applyInterset();
                    break;
                case 7 : 
                    break;
                default : System.out.println("Invalid Choice");
            }
            
        }while(choice != 7);
    }
    
}

class ValidationUtil{
    public  boolean accountExits(String accNo,HashMap<String,BankAccount> map){
        if (!map.containsKey(accNo)) {
            System.out.println("Account not found");
            return false;
        }
        return true;
    }

    public String accountValidation( Scanner sc){
        
        while(true){
            System.out.println(
                    "Enter 12 digit account number");

            String accNo = sc.next();
            String regex = "\\d{12}";

            if (accNo.matches(regex))
                return accNo;

            System.out.println(
                    "Invalid account number");
        }
    }

    public String accTypeValidation(String accType , Scanner sc){
        while(true){
            if(accType.equals("saving")  || accType.equals("current")) return accType;
            System.out.println("Enter valid account Type");
            accType = sc.next().toLowerCase();
        }
    }

    public BankAccount getAccount(
        Scanner sc,
        HashMap<String,BankAccount> map){

        String accNo = accountValidation(sc);

        if(!accountExits(accNo,map))
            return null;

        return map.get(accNo);
    }

}

class BankService{
    ValidationUtil validation ;
    Scanner sc;
    HashMap<String,BankAccount> map ;

    BankService(ValidationUtil validation ,Scanner sc ,HashMap<String,BankAccount> map){
        this.sc = sc;
        this.map = map;
        this.validation = validation;
    }

    public void CreateAccount(){
        System.out.println("Enter your username");
        String name = sc.next();
        
        String accNo = validation.accountValidation(sc);

        if (map.containsKey(accNo)) {
            System.out.println("Account already exists");
            return;
        }
        
        System.out.println("Enter your Account Type (saving/current)");
        String accType = sc.next();
        accType = validation.accTypeValidation(accType, sc);

        System.out.println("Enter your Initial Balance");
        double balance = sc.nextDouble();
        
        while((balance)<=0){ 
            System.out.println("Enter valid balance");
            balance = sc.nextDouble();
        }
        
        BankAccount account ;
        if(accType.equals("saving")){
            System.out.println("Enter your Initial Intrest");
            double interset = sc.nextDouble();
            account = new Saving(name,balance, accNo, interset);
        }
        else{
            System.out.println("Enter your Initial Intrest");
            double overdraftLimit = sc.nextDouble();
            account = new Current(name, balance, accNo, overdraftLimit);
        }

        map.put(accNo,account);
        System.out.println("Your Account Created Sucessfully ! Thank you ....");
         
    }
    public void depositMoney(){
        BankAccount acc = validation.getAccount(sc, map);
        if (acc == null) return;

        System.out.println("Enter amount");
        double amount = sc.nextDouble();
        acc.deposit(amount);
    }

    public void withdarwMoney(){
        BankAccount acc = validation.getAccount(sc, map);
        if (acc == null) return;

        System.out.println("Enter amount");
        double amount = sc.nextDouble();
        acc.withraw(amount); 
    }

     public  void checkBalance(){
        BankAccount acc = validation.getAccount(sc, map);
        if (acc == null) return;
       
        System.out.println("Total Balance  : " + acc.getBalance());
    }
    
    public void printDetails(){
        BankAccount acc = validation.getAccount(sc, map);
        if (acc == null) return;
       
        acc.printInfo();
    }

    public void applyInterset(){
        BankAccount acc = validation.getAccount(sc, map);
        if (acc == null) return;

        if (acc instanceof Saving) {
            Saving sa = (Saving) acc;
            System.out.println(sa.calculateInterset());   
        }
    }
}


abstract class BankAccount{
    private String name;
    private double balance ;
    private String accountNo;

    BankAccount(String name,double balance,String accountNo){
        this.name = name;
        this.balance = balance;
        this.accountNo = accountNo;
    }
    public void deposit(double amount){
        if(amount>0) {
            balance += amount;
            System.out.println("Your Total Balance is : " + balance);
        }
        else System.out.println("Enter Valid Amount");
    }

    abstract void withraw(double amount);

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }
    
    public void printInfo(){
        System.out.println("-----Account Details-----");
        System.out.println("Account Holder Name : " + name);
        System.out.println("Account Number : " + accountNo);
    }
}

class Saving extends BankAccount{
    private double intrestRate ;
    Saving(String name,double balance,String accountNo,double intrestRate){
        super(name, balance, accountNo);
        this.intrestRate = intrestRate;
    }

    public void withraw(double amount){
        if(amount > 0 && super.getBalance() >= amount) {
            super.setBalance(super.getBalance() - amount); 
            System.out.println("Your Total Balance is : " + super.getBalance());
        }
        else{
            System.out.println("Invalid Amount ");
        }
    }

    public double calculateInterset(){
        double interest = super.getBalance() * intrestRate / 100;
        super.setBalance(super.getBalance() + interest);
        return super.getBalance() ;
    }

    public void printInfo(){
        super.printInfo();
        System.out.println("Account Type :  Saving" );
        System.out.println("Intrest: " +  intrestRate);
        System.out.println("Total Balance : " + super.getBalance());
    }

}

class Current extends BankAccount{
    double overdraftLimit ;
    Current(String name,double amount,String accountNo,double overdraftLimit){
        super(name, amount, accountNo);
        this.overdraftLimit= overdraftLimit;
    }
    public void withraw(double amount){
        if(amount <= 0){
            System.out.println("Invalid Amount");
            return;
        }
         if(super.getBalance()+ overdraftLimit >= amount) {
            super.setBalance(super.getBalance() - amount); ;
            System.out.println("Your Total Balance is : " + super.getBalance());
        } else {
            System.out.println("Overdraft limit exceeded");
        }
    }
     public void printInfo(){
        super.printInfo();
        System.out.println("Account Type :  Current" );
        System.out.println("Over draft Limit : " +  overdraftLimit);
        System.out.println("Total Balance : " + super.getBalance());
    }

}