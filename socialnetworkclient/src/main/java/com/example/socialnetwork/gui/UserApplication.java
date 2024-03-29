package com.example.socialnetwork.gui;

import com.example.socialnetwork.server.Client;
import com.example.socialnetwork.service.ServiceFriendship;
import com.example.socialnetwork.service.ServiceMessage;
import com.example.socialnetwork.service.ServiceRequests;
import com.example.socialnetwork.service.ServiceUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserApplication extends Application {
    ServiceUser serviceUser;
    ServiceRequests serviceRequests;
    ServiceFriendship serviceFriendship;
    ServiceMessage serviceMessage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Socket client  = new Socket("127.0.0.1", 7999);
        ObjectInputStream i = new ObjectInputStream(client.getInputStream());
        ObjectOutputStream o = new ObjectOutputStream(client.getOutputStream());
        Client c = new Client(client, i, o);

        serviceUser = new ServiceUser(c);
        serviceRequests = new ServiceRequests(c);
        serviceFriendship = new ServiceFriendship(c);
        serviceMessage = new ServiceMessage(c);

        initView(primaryStage);

        Image icon = new Image("com\\example\\socialnetwork\\gui\\icon.png");
        primaryStage.getIcons().add(icon);

        primaryStage.setTitle("Social Network");
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader socialNetworkLoader = new FXMLLoader();
        socialNetworkLoader.setLocation(getClass().getResource("user-view.fxml"));
        AnchorPane userLayout = socialNetworkLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UserController socialNetworkController = socialNetworkLoader.getController();
        socialNetworkController.setUserService(serviceUser, serviceFriendship, serviceRequests, serviceMessage);
    }
}
