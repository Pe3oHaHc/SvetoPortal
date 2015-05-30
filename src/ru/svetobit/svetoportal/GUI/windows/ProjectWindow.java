package ru.svetobit.svetoportal.GUI.windows;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import ru.svetobit.svetoportal.Project;
import ru.svetobit.svetoportal.SvetoKit;
import ru.svetobit.svetoportal.framework.Notion;

import java.io.IOException;

/**
 * Created by Иван on 28.05.2015.
 */
public class ProjectWindow extends Stage
{
	Project mProject;
	Parent mSiteStructure;
		Notion mCurrentNotion;
		ListView<Notion> mNotionsList;
		VBox mNotionAreaContent;
		StackPane mNotionArea;
		Label mNotionEngName;
		TextField mNotionCustomName;
		VBox mNotionContent;

	public ProjectWindow(Project project)
	{
		mProject = project;

		setWidth(800);
		setMinWidth(640);
		setHeight(600);
		setMinHeight(400);

		Parent root;
		try{
			root = FXMLLoader.load(SvetoKit.class.getResource("GUI/windows/ProjectWindow.fxml"));
		}catch(IOException e){e.printStackTrace(); return;}

		TabPane main_tabs = (TabPane)root.lookup("#main_tabs");

		//Настраиваем окно со структурой сайта
		Parent site_structure = (Parent)main_tabs.getTabs().get(0).getContent();
		initSiteStructure(site_structure);

		//Настраиваем главное меню
		MenuBar main_menu = (MenuBar)root.lookup("#main_menu");
		initMainMenu(main_menu);

		//Настраиваем панель инструментов
		ToolBar toolbar = (ToolBar)mNotionAreaContent.lookup("#toolbar");
		initToolbar(toolbar);

		//Отображаем окно проекта
		Scene scene = new Scene(root);
		scene.getStylesheets().add(SvetoKit.class.getResource("GUI/styles/interface.css").toExternalForm());

		setOnCloseRequest((event) -> close());
		setScene(scene);
	}

//Инициализация окна
	private void initSiteStructure(Parent site_structure)
	{
		mSiteStructure = site_structure;
		ScrollPane scroll_pane = (ScrollPane)mSiteStructure.lookup("#notion_content_scrollpane");
		mNotionContent = (VBox)scroll_pane.getContent();
		mNotionArea = (StackPane)mSiteStructure.lookup("#notion_area");
		mNotionAreaContent = (VBox)mSiteStructure.lookup("#notion_area_content");
		mNotionEngName = (Label)mNotionAreaContent.lookup("#eng_name");
		mNotionCustomName = (TextField)mNotionAreaContent.lookup("#notion_custom_name");
		mNotionArea.getChildren().clear();

		mNotionCustomName.textProperty().addListener((value, oldValue, newValue) -> {
			mCurrentNotion.setCustomName(newValue);
		});

		VBox pane = (VBox)site_structure.lookup("#notions_list_container");
		mNotionsList = new ListView<>();
		mNotionsList.setCellFactory((ListView<Notion> list) -> new NotionItem(list));
		mNotionsList.setItems(mProject.getNotions());
		mNotionsList.setPrefWidth(180);
		mNotionsList.setMinHeight(Region.USE_PREF_SIZE);
		mNotionsList.setPrefHeight(Region.USE_COMPUTED_SIZE);
		mNotionsList.setMaxHeight(Double.MAX_VALUE);
		Label placeholder = new Label("Добавьте понятия\nв проект");
		placeholder.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		placeholder.setTextAlignment(TextAlignment.CENTER);
		placeholder.setAlignment(Pos.CENTER);
		mNotionsList.setPlaceholder(placeholder);
		mNotionsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		mNotionsList.getSelectionModel().selectedItemProperty().addListener((value, oldValue, newValue) -> {
			showNotion(newValue);
		});
		VBox.setVgrow(mNotionsList, Priority.ALWAYS);
		pane.getChildren().add(mNotionsList);

		Hyperlink add_notion_link = (Hyperlink)site_structure.lookup("#add_notion_link");
		add_notion_link.setOnAction((event) -> {mProject.createNotion();});
	}
	private void initMainMenu(MenuBar main_menu)
	{
		Menu svetoSiteMenu = main_menu.getMenus().get(0);                           //СветоСайт
		svetoSiteMenu.getItems().get(0).setOnAction((event) -> mProject.close());        //Выйти
		Menu projectMenu = main_menu.getMenus().get(1);                             //Проект
		projectMenu.getItems().get(0).setOnAction((event) -> mProject.save());           //Сохранить
	}
	private void initToolbar(ToolBar toolbar)
	{
		toolbar.setPadding(new Insets(2, 0, 2, 0));

		Button add_content = getToolbarButton(new ImageView(SvetoKit.class.getResource("GUI/images/add_16x16.png").toExternalForm()), "Добавить содержимое");
		add_content.setOnAction((event) -> addNotionContent());
		toolbar.getItems().add(add_content);
	}
	private Button getToolbarButton(ImageView image, String tooltip)
	{
		Button returned = new Button();
		returned.setPadding(new Insets(2));
		returned.setGraphic(image);
		returned.setTooltip(new Tooltip(tooltip));

		return returned;
	}

//Действия окна
	private void addNotionContent()
	{

	}
	private void showNotion(Notion notion)
	{
		mCurrentNotion = notion;
		mNotionContent.getChildren().clear();
		mNotionArea.getChildren().clear();

		if(notion == null)
			return;

		mNotionEngName.setText(notion.getEngName());
		if(notion.getCustomName() == null)
			mNotionCustomName.setText("");
		else
			mNotionCustomName.setText(notion.getCustomName());

		mNotionArea.getChildren().add(mNotionAreaContent);

		populateNotionContent(mNotionContent, notion);
	}
	public void selectNotion(Notion notion)
	{
		mNotionsList.getSelectionModel().select(notion);
	}

//Различные методы
	private void populateNotionContent(VBox container, Notion notion)
	{

	}

//Детали
	public class NotionItem extends ListCell<Notion>
	{
		ListView<Notion> mParent;
		Notion mItem;
		HBox mCell;
		Label mNotionName;
		StackPane mCrossPane;
		ImageView mCross, mCrossGray;

		public NotionItem(ListView<Notion> parent)
		{
			mParent = parent;
		}

		@Override
		protected void updateItem(Notion item, boolean empty)
		{
			super.updateItem(item, empty);

			if(empty)
			{
				setGraphic(null);
			}
			else
			{
				mItem = item;

				if (mCell == null)
				{
					mCell = new HBox();
					mCell.setCursor(Cursor.HAND);
					mNotionName = new Label();
					mNotionName.setMaxWidth(Double.MAX_VALUE);
					HBox.setHgrow(mNotionName, Priority.ALWAYS);

					mCrossGray = new ImageView(SvetoKit.class.getResource("GUI/images/delete_gray_16x16.png").toExternalForm());
					mCross = new ImageView(SvetoKit.class.getResource("GUI/images/delete_16x16.png").toExternalForm());

					mCrossPane = new StackPane();
					mCrossPane.getChildren().add(mCrossGray);
					mCrossPane.setOnMouseEntered((event) -> {
						mCrossPane.getChildren().clear();
						mCrossPane.getChildren().add(mCross);
					});
					mCrossPane.setOnMouseExited((event) -> {
						mCrossPane.getChildren().clear();
						mCrossPane.getChildren().add(mCrossGray);
					});
					mCrossPane.setOnMouseClicked((event) -> {
						mProject.deleteNotion(mItem);
					});

					mCell.getChildren().addAll(mNotionName, mCrossPane);

					mNotionCustomName.textProperty().addListener((value, oldValue, newValue) -> {
						if (mCurrentNotion == mItem)
						{
							setCustomName(newValue);
						}
					});
				}

				setCustomName(mNotionCustomName.getText());

				mCell.setOnMouseEntered((event) -> {
					mCrossPane.setVisible(true);
				});

				mCell.setOnMouseExited((event) -> {
					mCrossPane.setVisible(false);
				});

				setGraphic(mCell);
			}
		}

		private void setCustomName(String name)
		{
			if(name != null && name.length() > 0)
				mNotionName.setText(mItem.getEngName() + " (" + name + ")");
			else
				mNotionName.setText(mItem.getEngName());
		}
	}
}
