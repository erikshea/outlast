package controller;

import java.io.IOException;
import animals.*;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class AnimalControl<T extends Animal> extends HBox {
	private T animal;
    @FXML private TextField textField;

    public AnimalControl(T a) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("animal_control.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        this.animal = a;
    }
/*
    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }

    @FXML
    protected void doSomething() {
        System.out.println("The button was clicked!");
    }*/
}