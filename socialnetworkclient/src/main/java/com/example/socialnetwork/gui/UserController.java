package com.example.socialnetwork.gui;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.ServiceFriendship;
import com.example.socialnetwork.service.ServiceMessage;
import com.example.socialnetwork.service.ServiceRequests;
import com.example.socialnetwork.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class UserController {
    private ServiceUser serviceUser;
    private ServiceRequests serviceRequests;
    private ServiceMessage serviceMessage;
    private ServiceFriendship serviceFriendship;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableID;
    @FXML
    TableColumn<User,String> tableLastname;
    @FXML
    TableColumn<User,String> tableFirstname;
    @FXML
    TextField textFirstname;
    @FXML
    TextField textLastname;
    @FXML
    PasswordField textPassword;
    @FXML
    TextField textID;
    @FXML
    Pagination paginationFriendsTable;


    @FXML
    public void addUser(){
        String firstname = textFirstname.getText();
        String lastname = textLastname.getText();
        String password = textPassword.getText();
        serviceUser.add(firstname, lastname, password);
        initModel();
        paginationFriendsTable.setCurrentPageIndex(paginationFriendsTable.getMaxPageIndicatorCount() - 1);
    }

    @FXML
    public void deleteUser(){
        Long id = Long.parseLong(textID.getText());
        serviceUser.delete(id);
        initModel();
    }

    @FXML
    public void updateUser(){
        String firstname = textFirstname.getText();
        String lastname = textLastname.getText();
        String password = textPassword.getText();
        Long id = Long.parseLong(textID.getText());
        serviceUser.update(firstname,lastname, password, id);
        initModel();
    }

    @FXML
    public void openProfile() throws IOException {
        String password = textPassword.getText();
        String firstname = textFirstname.getText();
        String lastname = textLastname.getText();
        Long id = Long.parseLong(textID.getText());
        User user1 = serviceUser.findOne(id);
        if (!user1.getPassword().equals(password)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("Incorrect Password");

            // Aplicarea unui stil CSS la fereastra de alertă
            alert.getDialogPane().getStylesheets().add(
                    getClass().getResource("styledAlert.css").toExternalForm());

            alert.showAndWait();
        }
        else{
            User user = new User(firstname, lastname, password);
            user.setId(id);

            System.out.println(user);

            Stage profileStage = new Stage();
            FXMLLoader profileLoader = new FXMLLoader();
            profileLoader.setLocation(getClass().getResource("profile-view.fxml"));
            AnchorPane profileLayout = profileLoader.load();
            profileStage.setScene(new Scene(profileLayout));

            ProfileController profileController = profileLoader.getController();
            profileController.setProfileService(serviceUser, serviceFriendship, serviceRequests, serviceMessage, user, profileStage);

            profileStage.show();
        }
    }

    @FXML
    public void setTextFields(){
        User selected = tableView.getSelectionModel().getSelectedItem();
        textFirstname.setText(selected.getFirstName());
        textLastname.setText(selected.getLastName());
        textPassword.setText(selected.getPassword());
        textID.setText(selected.getId().toString());
    }
    public void setUserService(ServiceUser userService, ServiceFriendship friendshipService, ServiceRequests requestsService, ServiceMessage messageService) {
        serviceUser = userService;
        serviceFriendship = friendshipService;
        serviceRequests = requestsService;
        serviceMessage = messageService;
        initModel();
    }

    private void initModel() {
        final int ROWS_PER_PAGE = 5;
        final int TOTAL_ITEMS = 25;
        paginationFriendsTable.setPageCount(TOTAL_ITEMS/ROWS_PER_PAGE + 1);

        paginationFriendsTable.setPageFactory(pageIndex -> {
            //Pageable newPageable = new PageableImplementation(pageIndex, currentPage.getPageable().getPageSize());
            model = FXCollections.observableArrayList(new ArrayList<User>((Collection<? extends User>) serviceUser.findAllPage(pageIndex)));
            tableView.setItems(model);
            //System.out.println(tableView.getItems());
            return new VBox();
        });
    }
    @FXML
    public void initialize() {
        tableID.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
        tableFirstname.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableLastname.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
    }

}