package ru.svetobit.svetoportal;

import com.sun.istack.internal.Nullable;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Interface
{
	/**
	 * Показывает диалог ввода строки и возвращает значение этой строки.
	 * @param owner - окно-владелец.
	 * @return - значение введенной строки или null при нажатии кнопки отмены.
	 */
	public static String showStringInputDialog(String header, String message, String init_string, Stage owner, @Nullable final Utility.StringChecker checker)
	{
		final Stage dialog = new Stage(StageStyle.UTILITY);
		final boolean[] ok_pressed = new boolean[1];
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(owner);
		dialog.initStyle(StageStyle.UTILITY);
		dialog.setTitle(header);

		Parent root;

		try{
			root = FXMLLoader.load(SvetoKit.class.getResource("GUI/dialogs/StringInput.fxml"));
		}catch(IOException e){e.printStackTrace(); return null;}

		final Label message_label = (Label)root.lookup("#message");
		message_label.setText(message);

		final Button ok_button = (Button)root.lookup("#ok_button");
		ok_button.setOnAction((event) -> {
			ok_pressed[0] = true;
			dialog.close();
		});

		final Button cancel_button = (Button)root.lookup("#cancel_button");
		cancel_button.setOnAction((event) -> {
			ok_pressed[0] = false;
			dialog.close();
		});

		dialog.setOnCloseRequest((event) -> {
			ok_pressed[0] = false;
		});

		final TextField text = (TextField)root.lookup("#string_input");

		if(init_string == null || init_string.length() == 0)
			ok_button.setDisable(true);
		else
		{
			ok_button.setDisable(false);
			text.setText(init_string);
		}
		text.textProperty().addListener((value, oldValue, newValue) -> {
			if(newValue.length() == 0)
				ok_button.setDisable(true);
			else
				ok_button.setDisable(false);
		});

		Scene scene = new Scene(root);
		scene.getStylesheets().add(SvetoKit.class.getResource("GUI/styles/interface.css").toExternalForm());
		dialog.setScene(scene);

		final VBox error_message = (VBox)root.lookup("#error");
		String error_string = "";
		if(checker != null)
			error_string = checker.mMessage;
		final Label error_label = new Label();
		error_label.getStyleClass().add("error_message");
		error_label.setTextAlignment(TextAlignment.CENTER);
		error_label.setWrapText(true);
		error_label.setPrefWidth(448);
		error_label.setMaxHeight(Double.MAX_VALUE);
		error_label.setText(error_string);

		//Жуткий костыль, но по другому не получается
		Text helper = new Text(error_string);
		helper.setFont(error_label.getFont());
		helper.setWrappingWidth(448);
		double height = helper.getLayoutBounds().getHeight();
		error_label.setMinHeight(height + 2);

		do
		{
			dialog.showAndWait();

			if(ok_pressed[0] == true)
			{
				if (!checker.check(text.getText()))
				{
					error_message.getChildren().clear();
					error_message.getChildren().add(error_label);
				}
				else
					return text.getText();;
			}
			else
				return null;
		}
		while(true);
	}
	public static Boolean showYesNoDialog(String header, String message, Stage owner)
	{
		final Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(owner);
		stage.initStyle(StageStyle.UTILITY);
		stage.setTitle(header);

		final Boolean[] answer = new Boolean[1];
		answer[0] = null;

		Parent root;

		try{
			root = FXMLLoader.load(SvetoKit.class.getResource("GUI/dialogs/YesNoDialog.fxml"));
		}catch(IOException e){e.printStackTrace(); return null;}

		Label message_label = (Label)root.lookup("#message");
		message_label.setText(message);

		final Button yes_button = (Button)root.lookup("#yes_button");
		yes_button.setOnAction((event) -> {
			answer[0] = new Boolean(true);
			stage.close();
		});
		Platform.runLater(() -> {
			yes_button.requestFocus();
		});

		Button no_button = (Button)root.lookup("#no_button");
		no_button.setOnAction((event) -> {
			answer[0] = new Boolean(false);
			stage.close();
		});

		Scene scene = new Scene(root);
		scene.getStylesheets().add(SvetoKit.class.getResource("GUI/styles/interface.css").toExternalForm());
		stage.setScene(scene);
		stage.showAndWait();

		return answer[0];
	}
	public static void showErrorDialog(Stage owner, String message)
	{
		final Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(owner);
		stage.initStyle(StageStyle.UTILITY);

		Parent root;
		try{
			root = FXMLLoader.load(SvetoKit.class.getResource("GUI/dialogs/ErrorDialog.fxml"));
		}catch(IOException e){e.printStackTrace(); return;}

		Label message_label = (Label)root.lookup("#message");
		message_label.setText(message);

		Button ok_button = (Button)root.lookup("#ok_button");
		ok_button.setOnAction((event) -> stage.close());

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.showAndWait();
	}
}
