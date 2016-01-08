package com.jdc.book.app.controllers.utils;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.book.app.BookAppException;
import com.jdc.book.app.models.CategoryModel;
import com.jdc.book.app.models.imp.CategoryModelImp;
import com.jdc.book.db.entity.Category;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class CategoryList implements Initializable, Transportable<Category>{
	
	@FXML
	private ListView<Category> listView;
	@FXML
	private TextField searchBox;
	private CategoryModel model;
	
	public static Transportable<Category> showView(EventHandler<WindowEvent> handler) {
		
		try {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			
			FXMLLoader loader = new FXMLLoader(CategoryList.class.getResource("CategoryList.fxml"));
			Scene scene = new Scene(loader.load());
			CategoryList controller = loader.getController();
			stage.setScene(scene);
			stage.setOnHiding(handler);
			stage.show();
			
			return controller;
		} catch(Exception e) {
			throw new BookAppException(e.getMessage(), false);
		}
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new CategoryModelImp();

		searchBox.textProperty().addListener((a,b,c) -> {
			search();
		});
		search();
	}
	
	public void close(ActionEvent e) {
		try {
			Button b = (Button) e.getSource();
			
			if(b.getText().equals("Select") && null == listView.getSelectionModel().getSelectedItem()) {
				throw new BookAppException("Please Select One Category!!", true);
			}
			listView.getScene().getWindow().hide();
			
		} catch(BookAppException ex) {
			MessageUtils.handle(ex);
		}
	}
	
	private void search() {
		listView.getItems().clear();
		listView.getItems().addAll(model.find(searchBox.getText()));
	}

	@Override
	public Category transport() {
		return listView.getSelectionModel().getSelectedItem();
	}

}
