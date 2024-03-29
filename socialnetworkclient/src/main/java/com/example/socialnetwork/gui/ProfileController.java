package com.example.socialnetwork.gui;

import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.Reply;
import com.example.socialnetwork.domain.Tuple;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    private ServiceUser serviceUser;
    private ServiceRequests serviceRequest;
    private ServiceMessage serviceMessage;
    private ServiceFriendship serviceFriendship;
    private User user;
    private User friend;
    ObservableList<User> modelProfile = FXCollections.observableArrayList();
    ObservableList<User> modelSugestions = FXCollections.observableArrayList();
    ObservableList<Pair<User, String>> modelSentRequests = FXCollections.observableArrayList();
    ObservableList<Pair<User, String>> modelReceivedRequests = FXCollections.observableArrayList();
    ObservableList<Message> modelMessages = FXCollections.observableArrayList();
    Stage profileStage;
    @FXML
    Tab chatTab;
    @FXML
    TabPane profileTabPane;
    @FXML
    ListView<User> listViewFriends;
    @FXML
    Label labelUsername;
    @FXML
    Label labelId;
    @FXML
    Label chatFriendLabel;
    @FXML
    TableView<User> tableFriendsSugestions;
    @FXML
    TableColumn<User,String> tableSugestionsLastname;
    @FXML
    TableColumn<User,String> tableSugestionsFirstname;
    @FXML
    TableView<Pair<User, String>> tableSentRequests;
    @FXML
    TableColumn<Pair<User,String>,String> tableSentRequestsLastname;
    @FXML
    TableColumn<Pair<User,String>,String> tableSentRequestsFirstname;
    @FXML
    TableColumn<Pair<User,String>,String> tableSentRequestsStatus;
    @FXML
    TableView<Pair<User, String>> tableReceivedRequests;
    @FXML
    ListView<Message> listViewMessages;
    @FXML
    TableColumn<Pair<User,String>,String> tableReceivedRequestsLastname;
    @FXML
    TableColumn<Pair<User,String>,String> tableReceivedRequestsFirstname;
    @FXML
    TableColumn<Pair<User,String>,String> tableReceivedRequestsStatus;
    @FXML TextField textMessage;

    public void setProfileService(ServiceUser userService, ServiceFriendship friendshipService, ServiceRequests requestService, ServiceMessage messageService, User user, Stage profileStage) {
        this.serviceUser = userService;
        this.serviceFriendship = friendshipService;
        this.serviceRequest = requestService;
        this.profileStage = profileStage;
        this.serviceMessage = messageService;
        this.user = user;
        initModel(user);

        //Image icon = new Image("\\com\\example\\socialnetwork\\icon.png");
        //profileStage.getIcons().add(icon);

        profileStage.setTitle("Profile");
        labelUsername.setText(user.getFirstName() + " " + user.getLastName());
        labelId.setText("#" + user.getId().toString());
    }
    private void initModel(User user) {
        List<User> friendsList = serviceUser.getFriends(user.getId());
        modelProfile.setAll(friendsList);
        friendsList.add(user);
        Iterable<User> iterable = serviceUser.findAll();
        List<User> list = new ArrayList<>();
        for (User u : iterable) {
            if(!friendsList.contains(u))
                list.add(u);
        }
        modelSugestions.setAll(list);
        modelSentRequests.setAll(serviceRequest.getSentRequests(user.getId()));
        modelReceivedRequests.setAll(serviceRequest.getReceivedRequests(user.getId()));
    }
    @FXML
    public void initialize() {
        listViewFriends.setItems(modelProfile);
        this.listViewMessages.setCellFactory(param-> new ListCell<Message>(){
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);

                if (empty || message == null) {
                    setText(null);
                } else {
                    if(message.getTo().equals(user.getId())){
                        setStyle("-fx-alignment: CENTER_LEFT");
                    }
                    else if(message.getFrom().equals(user.getId())){
                        setStyle("-fx-alignment: CENTER_RIGHT");
                    }
                    if(message.getClass() == Message.class)
                        setText(message.toString());
                    else if((message.getClass() == Reply.class)){
                        String reply = serviceMessage.findOne(((Reply) message).getReplyMessageId()).getMessage();
                        setText("Reply: " + reply.substring(0,Math.min(reply.length(),7)) + "... ----- " + message.getMessage());
//                        setText("Reply: " + message.toString());
                    }
                }
            }
        });
        listViewMessages.setItems(modelMessages);
        tableSugestionsFirstname.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableSugestionsLastname.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableFriendsSugestions.setItems(modelSugestions);

        tableReceivedRequestsFirstname.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getKey();
            return new SimpleStringProperty(user.getFirstName());
        });

        tableReceivedRequestsLastname.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getKey();
            return new SimpleStringProperty(user.getLastName());
        });

        tableReceivedRequestsStatus.setCellValueFactory(cellData -> {
            String status = cellData.getValue().getValue(); // String from Pair<User, String>
            return new SimpleStringProperty(status);
        });
        tableReceivedRequests.setItems(modelReceivedRequests);
        //tableReceivedRequests.getColumns().addAll(tableReceivedRequestsFirstname, tableReceivedRequestsLastname, tableReceivedRequestsStatus);

        tableSentRequestsFirstname.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getKey();
            return new SimpleStringProperty(user.getFirstName());
        });

        tableSentRequestsLastname.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getKey();
            return new SimpleStringProperty(user.getLastName());
        });

        tableSentRequestsStatus.setCellValueFactory(cellData -> {
            String status = cellData.getValue().getValue(); // String from Pair<User, String>
            return new SimpleStringProperty(status);
        });
        tableSentRequests.setItems(modelSentRequests);
        //tableSentRequests.getColumns().addAll(tableSentRequestsFirstname, tableSentRequestsLastname, tableSentRequestsStatus);

    }
    @FXML
    public void sendRequest(){
        User u = tableFriendsSugestions.getSelectionModel().getSelectedItem();
        serviceRequest.add(user.getId(), u.getId());
        initModel(user);
    }
    @FXML
    public void acceptRequest(){
        User u = tableReceivedRequests.getSelectionModel().getSelectedItem().getKey();
        serviceRequest.acceptRequest(u.getId(), user.getId());
        initModel(user);
    }
    @FXML
    public void declineRequest(){
        User u = tableReceivedRequests.getSelectionModel().getSelectedItem().getKey();
        serviceRequest.declineRequest(u.getId(), user.getId());
        initModel(user);
    }
    @FXML
    public void refresh(){
        initModel(user);
    }

    @FXML
    public void cancelRequest(){
        User u = tableReceivedRequests.getSelectionModel().getSelectedItem().getKey();
        serviceRequest.delete(user.getId(),u.getId());
        initModel(user);
    }
    @FXML
    public void openChat(){
        this.friend = listViewFriends.getSelectionModel().getSelectedItem();
        chatFriendLabel.setText(friend.getLastName() + " " +friend.getFirstName());
        modelMessages.setAll(serviceMessage.getUserMessages(user.getId(), friend.getId()));
        profileTabPane.getSelectionModel().select(chatTab);
    }

    @FXML
    public void sendMessage(){
        serviceMessage.add(user.getId(), friend.getId(), textMessage.getText());
        modelMessages.setAll(serviceMessage.getUserMessages(user.getId(), friend.getId()));
    }

    @FXML
    public void replyMessage(){
        Message m = listViewMessages.getSelectionModel().getSelectedItem();
        serviceMessage.replyMessage(m.getId(), textMessage.getText());
        modelMessages.setAll(serviceMessage.getUserMessages(user.getId(), listViewFriends.getSelectionModel().getSelectedItem().getId()));
    }
    @FXML
    public void deleteFriend(){
        User friend = listViewFriends.getSelectionModel().getSelectedItem();
        // te iubesc
        serviceFriendship.delete(friend.getId(), user.getId());
        modelProfile.setAll(serviceUser.getFriends(user.getId()));
    }
}
