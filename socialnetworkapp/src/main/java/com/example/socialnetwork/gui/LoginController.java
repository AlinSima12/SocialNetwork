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

public class LoginController {
    private ServiceUser serviceUser;
    private ServiceRequests serviceRequests;
    private ServiceMessage serviceMessage;
    private ServiceFriendship serviceFriendship;
    @FXML
    TextField firstnameText;
    @FXML
    TextField lastnameText;
    @FXML
    TextField passwordText;
    //@FXML
    //TextField idText;

    @FXML
    public void registerUser() throws IOException {
        //Long id = Long.parseLong(idText.getText());
        String firstname = firstnameText.getText();
        String lastname = lastnameText.getText();
        String password = passwordText.getText();
        //User user = serviceUser.findOne(id);
        //if (user == null)
        User user = new User(firstname, lastname, password);
        user.setId(9L);
        openProfile(user);
        //openAdminProfile();
    }

    @FXML
    public void loginUser() throws IOException {
        String firstname = firstnameText.getText();
        String lastname = lastnameText.getText();
        String password = passwordText.getText();

//        //TODO if admin
//
//        //!!!!!!!!!!!!!
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Alert");
//        alert.setHeaderText("Incorrect Password");
//
//        // Aplicarea unui stil CSS la fereastra de alertă
//        alert.getDialogPane().getStylesheets().add(
//                getClass().getResource("styledAlert.css").toExternalForm());
//
//        alert.showAndWait();
        //!!!!!!!!!!!!!
        //else
        User user = new User(firstname, lastname, password);
        Long userId = serviceUser.getIdUser(firstname, lastname, password);
        user.setId(userId);
        //PROBLEMA!!!!!!!!!!!!!!!!!!!!!!!!TODO
        openProfile(user);

    }

    public void openAdminProfile() throws IOException {
        System.out.println("ADMIN");

        Stage profileStage = new Stage();
        FXMLLoader profileLoader = new FXMLLoader();
        profileLoader.setLocation(getClass().getResource("user-view.fxml"));
        AnchorPane profileLayout = profileLoader.load();
        profileStage.setScene(new Scene(profileLayout));

        UserController userController = profileLoader.getController();
        userController.setUserService(serviceUser, serviceFriendship, serviceRequests, serviceMessage);

        profileStage.show();

    }

    public void openProfile(User user) throws IOException {
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

    public void setUserService(ServiceUser userService, ServiceFriendship friendshipService, ServiceRequests requestsService, ServiceMessage messageService) {
        serviceUser = userService;
        serviceFriendship = friendshipService;
        serviceRequests = requestsService;
        serviceMessage = messageService;
        //initModel();
    }

    @FXML
    public void initialize() {

    }

}