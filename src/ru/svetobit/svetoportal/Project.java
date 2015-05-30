package ru.svetobit.svetoportal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import ru.svetobit.svetoportal.GUI.windows.ProjectWindow;
import ru.svetobit.svetoportal.framework.Notion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Project
{
	ProjectWindow mProjectWindow;
	ObservableList<Notion> mNotionsList;

	boolean mProjectIsEdited = false;

	public Project()
	{
		mNotionsList = FXCollections.observableArrayList();

		mProjectWindow = new ProjectWindow(this);
		mProjectWindow.show();
	}

	public ObservableList<Notion> getNotions()
	{
		return mNotionsList;
	}

//Методы работы с проектом
	public void close()
	{
		if(isProjectEdited())
		{
			Boolean save = Interface.showYesNoDialog("Сохранение проекта", "Сохранить изменения в проекте перед выходом?", mProjectWindow);
			if(save == null)
				return;
			else if(save)
				save();

			SvetoKit.closeProject(this);
		}
		else
		{
			SvetoKit.closeProject(this);
		}
	}
	public void save()
	{
		File project_folder = new File(SvetoKit.PROJECT_FOLDER);
		if(!project_folder.exists())
		{
			if(!project_folder.mkdirs())
				Interface.showErrorDialog(mProjectWindow, "При сохранении не удалось создать каталог проекта.");
		}

		File notions_folder = new File(SvetoKit.PROJECT_FOLDER + "/notions");

		if(!notions_folder.exists())
		{
			if(!notions_folder.mkdir())
				Interface.showErrorDialog(mProjectWindow, "При сохранении не удалось создать каталог понятий.");
		}

		try
		{
			saveNotions(notions_folder);
		}catch(IOException e){e.printStackTrace(); Interface.showErrorDialog(mProjectWindow, e.getMessage());}
	}
	public void createNotion()
	{
		Utility.StringChecker checker = new Utility.StringChecker(
			(String string) -> string.matches("[A-Za-z_][A-Za-z_0-9]{2,}"),
			"В этом поле допустимы только английские символы, цифры и символ подчеркивания. Первым символом не должна быть цифра. Поле должно содержать минимум три символа.");

		String notion_name = Interface.showStringInputDialog("Имя нового понятия", "Введите служебное (английское) имя для создаваемого понятия", null, mProjectWindow, checker);

		if(notion_name != null)
		{
			Notion new_notion = new Notion(notion_name);
			mNotionsList.add(new_notion);
			mProjectWindow.selectNotion(new_notion);
		}
	}
	public void deleteNotion(Notion notion)
	{
		Boolean delete = Interface.showYesNoDialog("Удаление понятия", "Удалить понятие \"" + notion.getCustomName() + "\"?", mProjectWindow);
		if(delete == null)
			return;
		if(!delete)
			return;

		mNotionsList.remove(notion);
	}
	public boolean isProjectEdited()
	{
		return mProjectIsEdited;
	}

//Приватные методы
	private void saveNotions(File notions_folder) throws IOException
	{
		for(Notion notion : mNotionsList)
		{
		//Создаём документ понятия
			Document doc = DocumentHelper.createDocument();
			doc.setRootElement(notion.getXML());

			File xml_file = new File(notions_folder.getAbsolutePath() + "/" + notion.getEngName() + ".xml");
			try
			{
				//Пересоздаём файл понятия
				if(!xml_file.exists())
					xml_file.createNewFile();
				else
				{
					xml_file.delete();
					xml_file.createNewFile();
				}
			}catch(IOException e){throw new IOException("Не удалось создать файл понятия \"" + notion.getEngName() + "\".");}

		//Пишем xml файл
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileWriter(xml_file), format);
			writer.write(doc);
			writer.close();
		}
	}
}
