package sample;

import BazaDanych.Account;
import BazaDanych.DBConnection;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.*;


public class Controller {
    DBConnection dbc = new DBConnection();

    ArrayList<String> idColumn = new ArrayList();
    ArrayList<String> nameColumn = new ArrayList();
    ArrayList<String> surnameColumn = new ArrayList();
    ArrayList<String> peselColumn = new ArrayList();
    ArrayList<String> addressColumn = new ArrayList();
    ArrayList<String> cashColumn = new ArrayList();

    @FXML
    ListView<String> Lista = new ListView<>();
    @FXML
    TextField imie = new TextField();
    @FXML
    TextField nazwisko = new TextField();
    @FXML
    TextField pesel = new TextField();
    @FXML
    TextField adres = new TextField();
    @FXML
    Button GO = new Button();
    @FXML
    HBox insertClient = new HBox();
    @FXML
    HBox insertTransferMoney;
    @FXML
    Button AddClientButton;
    @FXML
    Button RemoveClientButton;
    @FXML
    Button MakeTransferButton;
    @FXML
    Button ShowAccountsButton;
    @FXML
    Button AddMoneyButton;
    @FXML
    Button SubMoneyButton;
    @FXML
    TextField idSource;
    @FXML
    TextField idDest;
    @FXML
    TextField Cash;
    @FXML
    HBox insertAadSub;
    @FXML
    TextField idSourceAS;
    @FXML
    TextField CashAS;
    @FXML
    TableView<Account> Tabela = new TableView<>();
    @FXML
    TableColumn<Account,String> IDColumn = new TableColumn<>("ID");
    @FXML
    TableColumn<Account,String> NameColumn;
    @FXML
    TableColumn<Account,String> SurnameColumn;
    @FXML
    TableColumn<Account,String> PeselColumn;
    @FXML
    TableColumn<Account,String> AddressColumn;
    @FXML
    TableColumn<Account,String> CashColumn;
    @FXML
    TextArea ErrorsText;



    private int choiceAction = 0;

    @FXML
    void showUsers()
    {
        ErrorsText.setVisible(false);
//                "Single", "Double", "Suite", "Family App");
        ObservableList<String> items = FXCollections.observableArrayList ();
        items.addAll(dbc.getAllUsers());
        Lista.setItems(items);
        if (choiceAction == 0) {
            clearInsertFields();
            choiceAction = 4;
            insertClient.setVisible(true);
        }
/*
        ArrayList<String> accounts = new ArrayList();
        accounts.addAll(dbc.getAllUsers());
        for (int i = 0; i < accounts.size()/6;i++)
        {
            idColumn.add(accounts.get(i*6));
            nameColumn.add(accounts.get((i*6)+1));
            surnameColumn.add(accounts.get((i*6)+2));
            peselColumn.add(accounts.get((i*6)+3));
            addressColumn.add(accounts.get((i*6)+4));
            cashColumn.add(accounts.get((i*6)+5));
        }
        for (int i = 0; i < accounts.size()/6; i++) {
            Tabela.getItems().add(new Account(Integer.parseInt(idColumn.get(i)),nameColumn.get(i),surnameColumn.get(i),peselColumn.get(i),addressColumn.get(i),Integer.parseInt(cashColumn.get(i))));*/
            //IDColumn.getColumns().add(i,Tabela.getVisibleLeafColumn(i));
        //}
        //IDColumn.setCellValueFactory(cellData -> cellData.getValue().clientNumberProperty());
       /* IDColumn.setCellValueFactory(cellData ->{
            Account rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(idColumn.get(Tabela.getItems().));
        });*/

        //Tabela.getColumns().add(IDColumn);

        //IDColumn.setCellFactory();//(dbc.getAllUsers().get(0).get(0));
    }
    void showAccurateAccount() {
        try {
            ObservableList<String> items = FXCollections.observableArrayList ();
            items.addAll(showAccounts());
            Lista.setItems(items);
        } catch (NullPointerException e) {
            setErrorText("Nie wprowadziłes danych do wyszukiwania");
        }
    }
    void setupAfterPressButton()
    {
        disablingButtons();
        clearInsertFields();
        insertClient.setVisible(false);
        insertTransferMoney.setVisible(false);
        insertAadSub.setVisible(false);
        ErrorsText.setVisible(false);
    }

    @FXML
    void addUser()
    {
        setupAfterPressButton();
        insertClient.setVisible(true);
        AddClientButton.setDisable(false);
        choiceAction = 1;
    }
    void userAdd()
    {
        if (checkUserAddCompletely()) {
            try {
                dbc.add(imie.getText(), nazwisko.getText(), pesel.getText(), adres.getText());
            } catch (MySQLIntegrityConstraintViolationException se) {
                setErrorText("Istnieje juz konto z takim peselem");
            } catch (SQLException se) {
                setErrorText("Nie udało sie dodac uzytkownika");
            }
        }
    }
    boolean checkUserAddCompletely()
    {
        if (!imie.getText().equals("") && !nazwisko.getText().equals("") && checkPesel(pesel.getText()) && !adres.getText().equals("")) {
            return true;
        }
        setErrorText("Nie poprawne wprowadzenie danych");
        return false;
    }
    private boolean checkPeselLength(String pesel) {
        if (pesel.length() == 11)
        {
            return true;
        }else
        {
            setErrorText("Nie poprawna dlugosc peselu");
            System.out.println("Nie poprawna dlugosc peselu");
            return false;
        }
    }
    private boolean checkPeselIsNumeric(String pesel)
    {
        return pesel.matches("-?\\d+(\\.\\d+)?");
    }
    private boolean checkPesel(String pesel)
    {
        return checkPeselLength(pesel) && (checkPeselIsNumeric(pesel));
    }
    @FXML
    void removeUser()
    {
        /*setupAfterPressButton();
        insertClient.setVisible(true);
        RemoveClientButton.setDisable(false);
        choiceAction = 2;*/
        userRemover();
    }
    void userRemover()
    {
        try {
            int id = findIDFromList();
            System.out.println(id);
            dbc.removeByID(id);
            ErrorsText.setVisible(false);
        } catch (NullPointerException e) {
            setErrorText("Nie wybrałes klienta z listy");
        }
    }
    int findIDFromList()
    {
        try {
            return Integer.parseInt(Lista.getSelectionModel().getSelectedItem().substring(0,Lista.getSelectionModel().getSelectedItem().indexOf(' ')));
        } catch (NullPointerException e) {
            System.out.println("uzyszkodnik nie wybral klienta z listy i nic sie nie stało");
        }
        return 0;
    }
    @FXML
    void MakeTransfer()
    {
        setupAfterPressButton();
        insertTransferMoney.setVisible(true);
        MakeTransferButton.setDisable(false);
        choiceAction = 3;
    }
    boolean checkPossibleMoneyTransfer(int idS, int idD, int cash)
    {
        int cashSource = dbc.getCashByID(idS);
        if (cashSource >= cash) {
            dbc.setCashByID(cashSource - cash, idS);
            dbc.setCashByID(dbc.getCashByID(idD) + cash, idD);
            return true;
        }
        return false;

    }
    ArrayList<String> showAccounts()
    {
        if (!imie.getText().equals("")) {
            return dbc.getUserByName(imie.getText());
        } else if (!nazwisko.getText().equals("")) {
            return dbc.getUserBySurname(nazwisko.getText());
        } else if (!pesel.getText().equals("")) {
            return dbc.getUserByPesel(pesel.getText());
        } else if (!adres.getText().equals("")) {
            return dbc.getUserByAdres(adres.getText());
        }
        return null;
    }
    @FXML
    void addMoney()
    {
        setupAfterPressButton();
        insertAadSub.setVisible(true);
        AddMoneyButton.setDisable(false);
        idSourceAS.setText(findIDFromList()+"");
        choiceAction = 5;
    }
    void addCash(int id, int cash)
    {
        dbc.setCashByID(dbc.getCashByID(id) + cash, id);
    }
    @FXML
    void subMoney()
    {
        setupAfterPressButton();
        insertAadSub.setVisible(true);
        SubMoneyButton.setDisable(false);
        idSourceAS.setText(findIDFromList()+"");
        choiceAction = 6;
    }
    boolean subCash(int id, int cash)
    {
        if (dbc.getCashByID(id) >= cash) {
            dbc.setCashByID(dbc.getCashByID(id) - cash, id);
            return true;
        }
        return false;
    }
    void clearInsertFields()
    {
        imie.setText("");
        nazwisko.setText("");
        pesel.setText("");
        adres.setText("");
        idSource.setText("");
        idDest.setText("");
        Cash.setText("");
        idSourceAS.setText("");
        CashAS.setText("");
    }
    void activatingButtons()
    {
        AddClientButton.setDisable(false);
        RemoveClientButton.setDisable(false);
        MakeTransferButton.setDisable(false);
        ShowAccountsButton.setDisable(false);
        AddMoneyButton.setDisable(false);
        SubMoneyButton.setDisable(false);
    }
    void disablingButtons()
    {
        AddClientButton.setDisable(true);
        RemoveClientButton.setDisable(true);
        MakeTransferButton.setDisable(true);
        //ShowAccountsButton.setDisable(true);
        AddMoneyButton.setDisable(true);
        SubMoneyButton.setDisable(true);
    }
    @FXML
    void setErrorText(String S)
    {
        ErrorsText.setVisible(true);
        ErrorsText.setText(S);
    }

    @FXML
    void wprowadz()
    {
        insertClient.setVisible(false);
        insertTransferMoney.setVisible(false);
        insertAadSub.setVisible(false);
        //ErrorsText.setVisible(true);
        try {
            switch (choiceAction)
            {
                case 1:
                    userAdd();
                    break;
                case 2:
                    /*if (!imie.getText().equals("")) {
                        dbc.removeByName(imie.getText());
                    } else if (!nazwisko.getText().equals("")) {
                        dbc.removeBySurname(nazwisko.getText());
                    } else if (!pesel.getText().equals("")) {
                        dbc.removeByPesel(pesel.getText());
                    } else if (!adres.getText().equals("")) {
                        dbc.removeByAdres(adres.getText());
                    }*/
                    break;
                case 3:
                    checkPossibleMoneyTransfer(Integer.parseInt(idSource.getText()),Integer.parseInt(idDest.getText()),Integer.parseInt(Cash.getText()));
                    break;
                case 4:
                    showAccurateAccount();
                    break;
                case 5:
                    addCash(Integer.parseInt(idSourceAS.getText()),Integer.parseInt(CashAS.getText()));
                    break;
                case 6:
                    subCash(Integer.parseInt(idSourceAS.getText()),Integer.parseInt(CashAS.getText()));
                    break;
            }
        } catch (NumberFormatException e) {
            setErrorText("Nie podałes poprawnych danych");
        }
        choiceAction = 0;
        activatingButtons();
    }

}
